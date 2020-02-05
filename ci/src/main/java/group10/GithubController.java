package group10;

import spark.Request;
import spark.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.JSONException;
import org.json.JSONObject;

import group10.util.JsonUtil;
import group10.util.Path;

public class GithubController {
    private static JsonUtil json = new JsonUtil();
    private static CIServer ciServer = new CIServer();
    static Git git;
    File cloneDirectoryPath;

    /**
     * Handle the Github post event: get info, clone run tests and return response.
     * 
     * @param request  from github
     * @param response to send back
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static String handlePost(Request request, Response response) {
        System.out.println("Received a request...");
        // TODO: set commit pending

        // turn data string into a map structure
        System.out.println("Extracting relevant data...");
        JSONObject all_data = json.toMap(request.body());
        JSONObject relevant_data = json.getRelevantData(all_data);

        System.out.println("Saving build in database...");
        int buildID = CIServer.dbh.addBuild("pending", relevant_data.getString("author"), relevant_data.getString("ref"));

        // clone repo
        System.out.println("Began cloning repository...");
        File cloneDirectoryPath = new File("clone/");
        boolean cloned = false;
        try {
            cloned = cloneRepository(relevant_data.getString("clone_url"), relevant_data.getString("ref"),
                    cloneDirectoryPath);
        } catch (JSONException | IOException  e) {
            System.out.println("Failed cloning with: " + e.getMessage());
        }

        // only continue if we managed to clone
        if (cloned) {
            System.out.println("Finished cloning repository...");
            // build
            System.out.println("Started building project...");
            Path p = new Path();
            File pom = p.fileDFS("/clone", "pom.xml");
            String dir = pom.getAbsolutePath().replace("pom.xml", "");
            dir = dir.substring(0, dir.length()-1);
            CloneBuilder cb = new CloneBuilder(dir);
            boolean success = cb.rebuild();
            System.out.println("Finsihed building project...");
            System.out.println("Build success: " + cb.buildSuccess);

            // check results
            System.out.println("Fetching the test results...");
            ReadTestResults rts = new ReadTestResults();
            try {
                JSONObject testResults = rts.read("/clone", "surefire-reports");
                System.out.println("Adding test results to database...");
                CIServer.dbh.addTestsToBuild(buildID, testResults);
                if (testResults.getBoolean("success")) {
                    System.out.println("Passed all the tests!!");
                } else {
                    System.out.println("All tests did NOT pass.");
                }

            } catch (ParserConfigurationException e1) {
                e1.printStackTrace();
                return "failed";
            }

            //tear down the session
            try {
                tearDown(cloneDirectoryPath);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed tearDown "+ e.getMessage());
            }
            System.out.println("Job finished.");
            return "success";
        }
        System.out.println("Job finished.");
        return "failed";
    };

     /**
     * Clone the given repository & branch from git to the specified file
     * @param url the url of the repository to clone
     * @param branch the branch to clone
     * @param cloneDirectoryPath the file to clone the repo to
     * @return true if cloning was successful
     */
    public static boolean cloneRepository(String url, String branch, File cloneDirectoryPath) throws IOException {
        // Set which branch to clone
        ArrayList<String> branches = new ArrayList<>();
        String branchPath = branch;
        branches.add(branchPath);

        // Clone the repository
        try {
            CloneCommand gitclone = new CloneCommand();
            gitclone.setURI(url);
            gitclone.setDirectory(cloneDirectoryPath);
            gitclone.setBranchesToClone(branches);
            gitclone.setBranch(branch);
            git = gitclone.call();
            return true;
        } catch (GitAPIException e) {
            System.out.println("Failed cloning with: " + e.getMessage());
            return false;
        }
    }
        

    /**
     * Teardown session to prepare for next execution. 
     * Close the git session
     * Delete the cloned repo
     * @param file the cloned repo
     * @throws IOException
     */
    public static void tearDown(File file) throws IOException {
        if(git != null){
            git.close();
            git = null;
        }
        if(file.exists()){
            FileUtils.deleteDirectory(file);
        }
    }

    public static void main(String[] args) {
        
        //System.out.println(pom);
    }
}