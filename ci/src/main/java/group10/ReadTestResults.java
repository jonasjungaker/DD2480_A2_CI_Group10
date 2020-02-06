package group10;

import java.io.File;

import org.json.JSONArray;
import org.json.JSONObject;

import group10.util.Path;
import org.w3c.dom.*;
import javax.xml.parsers.*;

public class ReadTestResults {
    private Path p;

    public ReadTestResults() {
        p = new Path();
    }
    
    /**
     * Read the XMl files generated after runnings tests and 
     * return the relevant information as a JSONObject
     * @param fileName the file name to the result xml files from tests
     * @param root the root of the file with results
     * @return jsonobject with info from tests
     * @throws ParserConfigurationException
     */
    public JSONObject read(String root, String fileName) throws ParserConfigurationException {
        File reportDirectory = p.fileDFS(root, fileName);

        //Prepare JSONObject to return
        JSONObject json = new JSONObject();
        boolean success = false;
        int number_failed = 0;
        int number_success = 0;
        JSONArray failed = new JSONArray();
        JSONArray succeded = new JSONArray();
        
        //Get the values needed from the xml file
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        File[] reports = reportDirectory.listFiles();
        for (File report : reports) {
            if (report.getName().startsWith("TEST")) {
                try {
                    Document document = builder.parse(report);
                    document.getDocumentElement().normalize();
                    NodeList nodeTestCases = document.getElementsByTagName("testcase");
                    for(int i = 0; i < nodeTestCases.getLength(); i++){
                        Node n = nodeTestCases.item(i);
                        Node child = n.getFirstChild();

                        //Add details about the test
                        JSONObject test = new JSONObject();
                        test.put("name", nodeTestCases.item(i).getAttributes().getNamedItem("name").getTextContent());
                        test.put("test_number", i);
                        test.put("time", nodeTestCases.item(i).getAttributes().getNamedItem("time").getTextContent());
                        test.put("classname", nodeTestCases.item(i).getAttributes().getNamedItem("classname").getTextContent());
                        //If test failed add details of fail
                        if(child != null){
                            number_failed++;
                            String causeString = nodeTestCases.item(i).getTextContent();
                            int maxLength = (causeString.length() < 190?causeString.length():190);
                            causeString = causeString.substring(0, maxLength);
                            test.put("cause", causeString);
                            failed.put(test);                       
                        }else{
                            number_success++;
                            succeded.put(test);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        //Fill the json with the results
        if(number_failed == 0) success = true;
        json.put("success", success);
        json.put("number_failed",number_failed);
        json.put("number_success", number_success);
        json.put("failed", failed);
        json.put("succeded", succeded);

        return json;
    }

    public static void main(String[] args) throws ParserConfigurationException {
        ReadTestResults rtr = new ReadTestResults();
        JSONObject json = rtr.read("/ci/target/", "surefire-reports");
        System.out.println(json.toString());
    }

}