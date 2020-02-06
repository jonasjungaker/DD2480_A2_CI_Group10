package group10;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.NoSuchElementException;

public class FileConfig {
    /**
     * Method that returns the element from a specific row in the config file
     * 
     * @param test is test name
     * @return element on the row
     */
    public static String getRow(int row){
        String line = "";
        String path = getPath();
        int i = -1;
        try {
            BufferedReader br = new BufferedReader(new FileReader(path+"config.txt"));
            
            while ((line = br.readLine()) != null && i < row) {
                if(i == row-1) {
                    return line;
                }
                i++;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        throw new IndexOutOfBoundsException("Row out of bounds");
    }

    /**
     * Method that returns the path to the config file depending on the OS
     * 
     * @return the path is returned
     */
    public static String getPath() {
        String os = System.getProperty("os.name").toLowerCase();
        String user = System.getProperty("user.name");
        if(os.indexOf("win") >= 0) {
            return "C:"+"\\Users\\"+user+"\\"+"Documents\\";
        } else if(os.indexOf("mac") >= 0) {
            return "/Users/"+user+"/Sites/";
        }

        throw new NoSuchElementException("Could not find file");
    }
}