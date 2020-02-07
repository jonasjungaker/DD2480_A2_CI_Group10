package group10;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class CloneBuilder {
    public String path;
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
     * Builds the project and creates a buildoutput at this.buildOutput
     * Also updates the builddata of the build in order to see if the build and tests were successful
     * @return true if rebuild succeded false otherwise
     */
    public boolean rebuild() {
        this.buildOutput = this.build();
        if (this.buildOutput.length() > 1)
            return this.checkBuildOutput();
        return false;
    }

    /**
     * Builds a maven project at the designated path
     * 
     * @return a string of the output of the build process for checking with the
     *         checkBuildOutput method
     */
    private String build() {

        Process p;
        try {
            p = new ProcessBuilder(System.getProperty("user.dir") + "/run.sh", this.path).start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ( (line = reader.readLine()) != null ) builder.append(line).append(System.getProperty("line.separator"));
            p.waitFor();
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
        return (this.buildOutput.contains("Failures: 0"));
    }
}