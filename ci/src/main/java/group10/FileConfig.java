package group10;
import java.io.FileReader;
import java.io.BufferedReader;

public class FileConfig {
    /**
     * Method that returns the element from a specific row in the config file
     * 
     * @param row the row of the wanted element
     * @return element on the row
     */
    public static String getRow(int row) {
        String line = "";
        String path = System.getProperty("user.dir") + "/../../";
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
}