package group10;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.apache.maven.cli.MavenCli;

public class CloneBuilder
{
    public static String build(String path, MavenCli cli){
        // MavenCli cli = new MavenCli();

        // Running build and logging output
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(baos);
        cli.doMain(new String[]{"clean", "install"}, path, output, output);
        output.close();
        return baos.toString();
    }
        
    public static BuildData checkBuildOutput(String buildString)
    {
        // Checking if build was successful
        if (buildString.contains("BUILD SUCCESS")){
            return new BuildData(true, true, "Build successful");
        } 
        else if (buildString.contains("COMPILATION ERROR")) {
            // Finding output error message
            int beginIndex = buildString.indexOf("[ERROR] COMPILATION ERROR :");
            int endIndex = buildString.indexOf("[INFO] BUILD FAILURE");
            String message = "Compilation failure at build-time with errors:\n".concat(buildString.substring(beginIndex, endIndex));
            return new BuildData(false, false, message);
        }
        else {
            // Finding output error message
            int beginIndex = buildString.indexOf("[ERROR] Failures:");
            int endIndex = buildString.indexOf("[INFO] BUILD FAILURE");
            String message = "Test failure at build-time with errors:\n".concat(buildString.substring(beginIndex, endIndex));
            return new BuildData(true, false, message);
        }
    }
}