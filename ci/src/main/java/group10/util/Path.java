package group10.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.stream.Stream;

public class Path {
    public static final String HOME = "/";
    public static final String GITHUB = "/api/github";
    public static final String BUILD = "/build/:buildid";

    /**
     * Method to find a File with the name 
     * fileName starting from "root" directory.
     * @param root directory/location to begin search
     * @param fileName name of the file to search for
     * @return File with the name fileName, returns null 
     * if no file was found
     */
    public File fileDFS(String root, String fileName) {
        File dir = new File(System.getProperty("user.dir") + root);

        Stream<java.nio.file.Path> stream= null;
        try {
            stream = Files.find(dir.toPath(), 30, (path, basicFileAttributes) -> {
                File file = path.toFile();
                //System.out.println(file.getName());
                //System.out.println(fileName);
                return file.getName().equals(fileName);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // get File from stream
        if (stream != null) {
            Optional<java.nio.file.Path> p = stream.findAny();
            if (p.isPresent()) {
                File f = p.get().toFile();  
                stream.close();
                if (p.isPresent()) {
                    return f;
                }
            }
            stream.close();
        }

        // did not find file
        return null;
    }
}