package com.wmba.mortar.generate;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;

public class ScreenGenerator {

  private final ScreenGeneratorPrefs mPrefs;

  public static class ScreenGeneratorPrefs {

    private String baseOutDirPath;
    private String layoutXMLDirPath;
    private String androidAppPackage;
    private String screenPackage;
    private String viewPackage;

    // Optional
    private ClassRepresentation defaultBaseModuleClass;
    private String presenterPackage;
    private ClassRepresentation customPresenterClass;

    public ScreenGeneratorPrefs(File baseOutDir, File layoutXMLDir,
        String androidAppPackage, String screenPackage, String viewPackage) {
      if (baseOutDir == null)
        throw new NullPointerException("The Base Out directory cannot be null.");
      else if (layoutXMLDir == null)
        throw new NullPointerException("The Layout XML directory cannot be null.");
      else if (screenPackage == null)
        throw new NullPointerException("The Screen package cannot be null.");
      else if (viewPackage == null)
        throw new NullPointerException("The View package cannot be null.");
      else if (androidAppPackage == null || androidAppPackage.length() == 0)
        throw new NullPointerException("The Android App package cannot be null or empty.");

      String baseOutDirPath = baseOutDir.getAbsolutePath();
      String layoutXMLDirPath = layoutXMLDir.getAbsolutePath();

      if (!baseOutDirPath.endsWith("/"))
        baseOutDirPath += "/";
      if (!layoutXMLDirPath.endsWith("/"))
        layoutXMLDirPath += "/";

      this.baseOutDirPath = baseOutDirPath;
      this.layoutXMLDirPath = layoutXMLDirPath;

      this.androidAppPackage = androidAppPackage;
      this.screenPackage = screenPackage;
      this.viewPackage = viewPackage;
    }

    public ScreenGeneratorPrefs defaultBaseModule(String packageName, String className) {
      this.defaultBaseModuleClass = new ClassRepresentation(packageName, className);
      return this;
    }

    public ScreenGeneratorPrefs presenterPackage(String presenterPackage) {
      this.presenterPackage = presenterPackage;
      return this;
    }

    public ScreenGeneratorPrefs customPresenter(String packageName, String className) {
      this.customPresenterClass = new ClassRepresentation(packageName, className);
      return this;
    }

  }

  public static class ClassRepresentation {
    private final String packageName;
    private final String className;

    public ClassRepresentation(String packageName, String className) {
      this.packageName = packageName;
      this.className = className;
    }

    public String getName() {
      return packageName + "." + className;
    }

    public String getSimpleName() {
      return className;
    }

  }

  public ScreenGenerator(ScreenGeneratorPrefs screenGenPrefs) {
    new File(screenGenPrefs.baseOutDirPath).mkdirs();
    new File(screenGenPrefs.layoutXMLDirPath).mkdirs();

    mPrefs = screenGenPrefs;
  }

  public void start() {
    BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      System.out.println("1. Generate New Screen");
      System.out.println("2. Delete Existing Screen");
      System.out.println("3. Exit");
      System.out.println("Should the presenter be generated as a separate file?");

      String choiceString;
      try {
        choiceString = inputReader.readLine().trim();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      if (choiceString.equals("1")) {
        generate();
        break;
      } else if (choiceString.equals("2")) {
        delete();
        break;
      } else if (choiceString.equals("3")) {
        System.exit(0);
      } else {
        System.out.println("Invalid choice.");
      }
    }
  }

  private Configuration getFreemarkerConfig() {
    Configuration config = new Configuration();
    config.setClassForTemplateLoading(ScreenGenerator.class, "template");
    config.setIncompatibleImprovements(new Version(2, 3, 20));
    config.setDefaultEncoding("UTF-8");
    config.setLocale(Locale.US);
    config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    return config;
  }

  public void generate() {
    Configuration config = getFreemarkerConfig();

    TemplatePrefs templatePrefs;
    Map<Template, File> files;
    while (true) {
      String baseFileName = getBaseFileName();
      boolean isPresenterInSeparateFile = mPrefs.presenterPackage != null && isPresenterInSeparateFile();

      templatePrefs = getTemplatePrefs(baseFileName, isPresenterInSeparateFile);
      files = getFiles(config, templatePrefs);

      Set<Map.Entry<Template, File>> entrySet = files.entrySet();
      List<File> filesThatExist = new ArrayList<File>(entrySet.size());

      for (Map.Entry<Template, File> entry : entrySet) {
        File file = entry.getValue();
        if (file.exists()) {
          filesThatExist.add(file);
        }
      }

      if (!filesThatExist.isEmpty()) {
        if (handleFilesThatAlreadyExist(filesThatExist)) {
          break;
        }
      } else {
        break;
      }
    }

    SimpleHash data = preferencesToHash(templatePrefs);

    try {
      for (Map.Entry<Template, File> entry : files.entrySet()) {
        Template template = entry.getKey();
        File file = entry.getValue();

        Writer writer = null;
        try {
          file.getParentFile().mkdirs();
          writer = new FileWriter(file);
          template.process(data, writer);
        } finally {
          writer.flush();
          writer.close();
        }
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void rename() {
    Configuration config = getFreemarkerConfig();
    String oldBaseFileName = getBaseFileName();

    TemplatePrefs oldTemplatePrefs = getTemplatePrefs(oldBaseFileName, true);
    Map<Template, File> oldFiles = getFiles(config, oldTemplatePrefs);

    System.out.println("The following files will be renamed:");
    for (Map.Entry<Template, File> entry : oldFiles.entrySet()) {
      if (entry.getValue().exists())
        System.out.println(entry.getValue().getName());
    }

    String newBaseFileName = getBaseFileName();
    TemplatePrefs newTemplatePrefs = getTemplatePrefs(newBaseFileName, true);
    Map<Template, File> newFiles = getFiles(config, newTemplatePrefs);

    System.out.println("Are you sure you want to proceed?");
    System.out.println("1. Yes");
    System.out.println("2. Exit");

    BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
    while (true) {

      String choiceString;
      try {
        choiceString = inputReader.readLine().trim();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      if (choiceString.equals("1")) {

        // Rename the old files
        for (Map.Entry<Template, File> oldEntry : oldFiles.entrySet()) {

          if (oldEntry.getValue().exists()) {
            File newFile = null;
            String templateName = oldEntry.getKey().getName();

            for (Map.Entry<Template, File> newEntry : newFiles.entrySet()) {
              if (newEntry.getKey().getName().equals(templateName)) {
                // Is the same template
                newFile = newEntry.getValue();
              }
            }

            if (newFile != null) {
              boolean success = oldEntry.getValue().renameTo(newFile);
              if (!success)
                System.out.println("There was an issue deleting " + oldEntry.getValue().getName());
            }
          }

        }

        break;
      } else if (choiceString.equals("2")) {
        System.exit(0);
      } else {
        System.out.println("Invalid choice.");
      }
    }
  }

  private void delete() {
    Configuration config = getFreemarkerConfig();
    String oldBaseFileName = getBaseFileName();

    TemplatePrefs oldTemplatePrefs = getTemplatePrefs(oldBaseFileName, true);
    Map<Template, File> oldFiles = getFiles(config, oldTemplatePrefs);

    List<File> filesToDelete = new ArrayList<File>(oldFiles.size());
    for (Map.Entry<Template, File> entry : oldFiles.entrySet()) {
      if (entry.getValue().exists())
        filesToDelete.add(entry.getValue());
    }

    System.out.println("The following files will be deleted:");
    for (File file : filesToDelete)
      System.out.println(file.getName());

    System.out.println("Are you sure you want to proceed?");
    System.out.println("1. Yes");
    System.out.println("2. Exit");

    BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
    while (true) {

      String choiceString;
      try {
        choiceString = inputReader.readLine().trim();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      if (choiceString.equals("1")) {
        for (File file : filesToDelete) {
          boolean success = file.delete();
          if (!success)
            System.out.println("There was an issue deleting " + file.getName());
        }

        break;
      } else if (choiceString.equals("2")) {
        System.exit(0);
      } else {
        System.out.println("Invalid choice.");
      }
    }
  }

  private boolean handleFilesThatAlreadyExist(List<File> filesThatExist) {
    System.out.println("These files already exist: ");
    for (File file : filesThatExist) {
      System.out.println(file.getName());
    }

    BufferedReader mInputReader = new BufferedReader(new InputStreamReader(System.in));
    int userChoice = -1;
    while (true) {
      System.out.println("Do you want to overwrite?");
      System.out.println("1. Yes");
      System.out.println("2. Rename");
      System.out.println("3. Exit");

      try {
        String intString = mInputReader.readLine().trim();
        userChoice = Integer.parseInt(intString);
      } catch (NumberFormatException e) {
        // Do nothing
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      switch (userChoice) {
        case 1:
          return true;
        case 2:
          return false;
        case 3:
          System.exit(0);
          break;
        default:
          System.out.println("Invalid input.");
          break;
      }
    }
  }

  private Map<Template, File> getFiles(Configuration config, TemplatePrefs templatePrefs) {
    Template screenTemplate;
    Template presenterTemplate;
    Template viewTemplate;
    Template layoutXMLTemplate;
    try {
      screenTemplate = config.getTemplate("screen.ftl");
      viewTemplate = config.getTemplate("view.ftl");
      layoutXMLTemplate = config.getTemplate("layout_xml.ftl");
      presenterTemplate = config.getTemplate("presenter.ftl");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    String screenPath = templatePrefs.screenPackage.replace(".", "/") + "/";
    String viewPath = templatePrefs.viewPackage.replace(".", "/") + "/";

    File screenFile = new File(mPrefs.baseOutDirPath + screenPath + templatePrefs.screenFileName + ".java");
    File viewFile = new File(mPrefs.baseOutDirPath + viewPath + templatePrefs.viewFileName + ".java");
    File layoutXMLFile = new File(mPrefs.layoutXMLDirPath + templatePrefs.layoutXMLFileName + ".xml");

    Map<Template, File> fileMap = new HashMap<Template, File>();
    fileMap.put(screenTemplate, screenFile);
    fileMap.put(viewTemplate, viewFile);
    fileMap.put(layoutXMLTemplate, layoutXMLFile);

    if (templatePrefs.isPresenterSeparateFile) {
      String presenterPath = templatePrefs.presenterPackage.replace(".", "/") + "/";
      File presenterFile = new File(mPrefs.baseOutDirPath + presenterPath + templatePrefs.presenterFileName + ".java");
      fileMap.put(presenterTemplate, presenterFile);
    }

    return fileMap;
  }

  private TemplatePrefs getTemplatePrefs(String baseFileName, boolean isPresenterInSeparateFile) {
    TemplatePrefs templatePrefs = new TemplatePrefs();
    templatePrefs.screenFileName = getScreenFileName(baseFileName);
    templatePrefs.viewFileName = getViewFileName(baseFileName);
    templatePrefs.androidAppPackage = mPrefs.androidAppPackage;
    templatePrefs.layoutXMLFileName = getLayoutXMLFileName(baseFileName);
    templatePrefs.isPresenterSeparateFile = isPresenterInSeparateFile;
    templatePrefs.presenterFileName = getPresenterFileName(baseFileName, templatePrefs.isPresenterSeparateFile);
    templatePrefs.screenPackage = mPrefs.screenPackage;
    templatePrefs.viewPackage = mPrefs.viewPackage;
    templatePrefs.presenterPackage = mPrefs.presenterPackage;
    templatePrefs.customPresenterClass = mPrefs.customPresenterClass;
    templatePrefs.defaultBaseModuleClass = mPrefs.defaultBaseModuleClass;
    return templatePrefs;
  }

  private String getBaseFileName() {
    BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

    String baseFileName;
    while (true) {
      System.out.print("Please enter the base file name: ");

      try {
        baseFileName = inputReader.readLine().trim();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      String errorMessage = null;
      if (baseFileName.length() == 0) {
        errorMessage = "Base File Name must not be empty.";
      } else if (baseFileName.contains(" ")) {
        errorMessage = "Base File Name must not have spaces.";
      }

      if (errorMessage == null)
        return baseFileName;
      else
        System.out.println(errorMessage);
    }
  }

  private boolean isPresenterInSeparateFile() {
    BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

    while (true) {
      System.out.println("Should the presenter be generated as a separate file?");
      System.out.println("1. Yes");
      System.out.println("2. No");

      String choiceString;
      try {
        choiceString = inputReader.readLine().trim();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      if (choiceString.equals("1")) {
        return true;
      } else if (choiceString.equals("2")) {
        return false;
      } else {
        System.out.println("Invalid choice.");
      }
    }
  }

  protected String getPresenterFileName(String baseFileName, boolean isPresenterSeparateFile) {
    if (isPresenterSeparateFile)
      return baseFileName + "Presenter";
    else
      return "Presenter";
  }

  protected String getLayoutXMLFileName(String baseFileName) {
    return "screen_" + baseFileName.toLowerCase(Locale.US);
  }

  protected String getScreenFileName(String baseFileName) {
    return baseFileName + "Screen";
  }

  protected String getViewFileName(String baseFileName) {
    return baseFileName + "View";
  }

  public static class TemplatePrefs {
    public String screenFileName;
    public String viewFileName;
    public String layoutXMLFileName;
    public String presenterFileName;

    public String androidAppPackage;
    public String screenPackage;
    public String viewPackage;
    public String presenterPackage;
    public boolean isPresenterSeparateFile;

    public ClassRepresentation defaultBaseModuleClass;
    public ClassRepresentation customPresenterClass;
  }

  private SimpleHash preferencesToHash(TemplatePrefs prefs) {
    SimpleHash hash = new SimpleHash();
    Field[] declaredFields = prefs.getClass().getDeclaredFields();
    try {
      for (Field field : declaredFields) {
        hash.put(field.getName(), field.get(prefs));
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    return hash;
  }
}
