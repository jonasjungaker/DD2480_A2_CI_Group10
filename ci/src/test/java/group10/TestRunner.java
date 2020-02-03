package group10;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import group10.util.Path;

public class TestRunner {

    /**
     * Recursively searches for .class files in a directory
     * and adds them to a list.
     * @param classFiles empty list that will contain .class files
     * @param dir directory where search is started
     */
    void findClasses(List<File> classFiles, File dir) {
        // traverse directory recursively and add files to list
        if (dir.listFiles() != null) {
            for (File file : dir.listFiles()) {
                if (file.isDirectory()) {
                    findClasses(classFiles, file);
                }
                else if (file.getName().toLowerCase().endsWith(".class")) {
                    classFiles.add(file);
                }
            }
        }
    }

    /**
     * Loads .class files from "classfiles" into memory
     * so that JUnit can execute them.
     * @param classFiles list of .class files
     * @param dir directory where search is started
     */
    List<Class<?>> convertToClasses(List<File> classFiles, File dir) throws MalformedURLException {
        // used to load classes
        URL[] cp = {dir.toURI().toURL()};
        URLClassLoader classLoader = new URLClassLoader(cp);

        List<Class<?>> classes = new ArrayList<Class<?>>();
        for (File file : classFiles) {
            // only try to load tests
            if (!file.getName().endsWith("Test.class")) {
                 continue;
            }

            String[] packages = dir.getName().split("test-classes/");
            // if package runs deep, i.e. group.util
            String packageName = packages[packages.length-1].replace("/", ".");

            // build the name, remove .class and prepend package
            String name = file.getPath().substring(dir.getPath().length() + 1)
                .replace('/', '.')
                .replace('\\', '.');
            name = packageName + "." + name.substring(0, name.length() - 6);
            // actual loading
            Class<?> c;
            try {
                c = classLoader.loadClass(name);
            } catch (ClassNotFoundException e) {
                throw new AssertionError(e);
            }
            // no abstracts
            if (!Modifier.isAbstract(c.getModifiers())) {
                classes.add(c);
            }
        }
        return classes;
    }
    
    /** 
     * Generates a JSON response for the list
     * of results created from JUnit.
     * @param results list of Result
     * @return JSONObject a JSON object containing results
     */
    JSONObject resultsToJSON(List<Result> results)  {
        //todo turn tests into json
        return new JSONObject();
    }
    
    /** 
     * Method that finds tests in a given 
     * directory.
     * @param path location of starting point
     * @return JSONObject results
     */
    public JSONObject runTests(String path) {
        JUnitCore jUnitCore = new JUnitCore();

        // this is the location of the compiled tests
        // should be path to the cloned repos target
        File dir = new File(path);

        // find class files
        List<File> classFiles = new ArrayList<File>();
        findClasses(classFiles, dir);

        // turn these files (more like load) into things JUnit can execute/test
        List<Class<?>> classes = null;
        try {
            classes = convertToClasses(classFiles, dir);
        } catch (MalformedURLException e) {
            System.out.println("Invalid url");
            e.printStackTrace();
        }
        
        // run each test and store result
        List<Result> results = new ArrayList<>(); 
        for (Class<?> test : classes) {
            results.add(jUnitCore.run(test));
        }

        return resultsToJSON(results);
    }

    public static void main(String[] args) {
        // Example use of test runner
        Path p = new Path();
        TestRunner tr = new TestRunner();
        // look for test-classes in /dump/
        File target = p.fileDFS("/dump/", "test-classes");
        if (target != null) {
            JSONObject results = tr.runTests(target.getPath());
        } else {
            System.out.println("No test classes found!");
        }
    }
}