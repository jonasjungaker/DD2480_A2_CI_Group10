package group10;

import java.io.File;
import java.io.IOException;

import org.json.JSONObject;

import group10.util.Path;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class ReadTestResults {
    private Path p;
    SAXBuilder saxBuilder;

    public ReadTestResults() {
        p = new Path();
        saxBuilder = new SAXBuilder();
    }
    
    public JSONObject read() {
        File reportDirectory = p.fileDFS("/dump/", "surefire-reports");
        SAXBuilder saxBuilder = new SAXBuilder();

        File[] reports = reportDirectory.listFiles();
        for (File report : reports) {
            if (report.getName().startsWith("TEST")) {
                try {
                    Document document = saxBuilder.build(report);   
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return new JSONObject();
    }

    public static void main(String[] args) {
        ReadTestResults rtr = new ReadTestResults();
        rtr.read();
    }

}