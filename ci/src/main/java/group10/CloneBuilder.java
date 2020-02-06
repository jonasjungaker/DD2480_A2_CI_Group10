package group10;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.maven.cli.MavenCli;

public class CloneBuilder {
    public String path;
    public boolean buildSuccess;
    public boolean testSuccess;
    public String message;
    public String buildOutput;

    /**
     * creates a CloneBuilder object that can create the binaries of a maven project
     * at path
     * 
     * @param path local path to maven project which contains the pom file
     */
    public CloneBuilder(String path) {
        this.path = path;
        // this.rebuild();
    }

    /**
<<<<<<< HEAD
     * Builds the project and creates a buildoutput at this.buildOutput Also updates
     * the builddata of the build in order to see if the build and tests were
     * successful
=======
     * Builds the project and creates a buildoutput at this.buildOutput
     * Also updates the builddata of the build in order to see if the build and tests were successful
     * @return true if rebuild succeded false otherwise
>>>>>>> docs: fixed javadoc errors #11
     */
    public boolean rebuild() {
        this.buildOutput = this.build();
        if (this.buildOutput.length() < 1)
            return this.checkBuildOutput();
        return false;
    }

    /**
<<<<<<< HEAD
     * builds a maven project at the designated path
     * 
     * @return a string of the output of the build process for checking with the
     *         checkBuildOutput method
=======
     * Builds a maven project at the designated path
     * @return a string of the output of the build process for checking with the checkBuildOutput method
>>>>>>> docs: fixed javadoc errors #11
     */
    private String build() {

        Process p;
        try {
            p = new ProcessBuilder(System.getProperty("user.dir") + "/run.sh", this.path).start();
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ( (line = reader.readLine()) != null ) builder.append(line).append(System.getProperty("line.separator"));
            return builder.toString();
        } catch (IOException | InterruptedException  e) {
            e.printStackTrace();
        }
        return "failed";
    }
        
    /**
     * Checks the build output of the maven command line interface command doMain()
     * updates the CloneBuilder's builddata
     * @return whether or not the build and tests were successful
     */
    public boolean checkBuildOutput()
    {
        // Checking if build was successful
        if (this.buildOutput.contains("BUILD SUCCESS")){
            this.buildSuccess = true;
            this.testSuccess = true;
            this.message = "Build successful with no errors\n";
            return true;
        } 
        else if (this.buildOutput.contains("COMPILATION ERROR")) {
            // Finding output error message
            int beginIndex = this.buildOutput.indexOf("[ERROR] COMPILATION ERROR :");
            int endIndex = this.buildOutput.indexOf("[INFO] BUILD FAILURE");
            this.message = "Compilation failure at build-time with errors:\n".concat(this.buildOutput.substring(beginIndex, endIndex));
            this.buildSuccess = false;
            this.testSuccess = false;
            return false;
        }
        else if (this.buildOutput.contains("[ERROR] Failures:")) {
            // Finding output error message
            int beginIndex = this.buildOutput.indexOf("[ERROR] Failures:");
            int endIndex = this.buildOutput.indexOf("[INFO] BUILD FAILURE");
            this.message = "Test failure at build-time with errors:\n".concat(this.buildOutput.substring(beginIndex, endIndex));
            this.buildSuccess = true;
            this.testSuccess = false;
            return false;
        }
        else if (this.buildOutput.contains("there is no POM in this directory")) {
            this.buildSuccess = false;
            this.testSuccess = false;
            this.message = "The goal you specified requires a project to execute but there is no POM in this directory\n";
            return false;
        }
        else {
            this.buildSuccess = false;
            this.testSuccess = false;
            this.message = "Unidentified error while building the project\n";
            return false;
        }
    }
}