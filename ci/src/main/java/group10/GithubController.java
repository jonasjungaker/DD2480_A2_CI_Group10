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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import group10.util.JsonUtil;
import group10.util.Path;

import group10.FileConfig;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Base64;
import java.net.URL;

public class GithubController {
    private static JsonUtil json = new JsonUtil();
    static Git git;
    File cloneDirectoryPath;

    /**
     * Handle the Github post event: get info, clone run tests and return response.
     * 
     * @param request  from github
     * @param response to send back
     * @return string "success" if all succeded "failed" otherwise
     */
    public static String handlePost(Request request, Response response) {
        System.out.println("Received a request...");

        // turn data string into a map structure
        System.out.println("Extracting relevant data...");
        JSONObject all_data = json.toMap(request.body());
        JSONObject relevant_data = json.getRelevantData(all_data);

        // clone repo
        System.out.println("Began cloning repository...");
        File cloneDirectoryPath = new File("clone/");
        boolean cloned = false;
        try {
            cloned = cloneRepository(relevant_data.getString("clone_url"), relevant_data.getString("ref"),
                    cloneDirectoryPath);
        } catch (JSONException e) {
            System.out.println("Failed cloning with: " + e.getMessage());
        }
        // compile repo
        // run tests
        // set commit
        setCommitStatus("test", "success", relevant_data.get("sha").toString());

        //tear down the session
        try {
            tearDown(cloneDirectoryPath);
        } catch (IOException IOe) {
            IOe.printStackTrace();
            System.out.println("Failed tearDown "+ IOe.getMessage());
        }

        // only continue if we managed to clone
        if (cloned) {
            System.out.println("Finished cloning repository...");

            
            // set pending statuses
            boolean exists = setCommitStatus(relevant_data, null, "pending", 0);
            if (exists) {
                // build
                System.out.println("Saving build in database...");
                int buildID = CIServer.dbh.addBuild("pending", relevant_data.getString("author"), relevant_data.getString("ref"));

                System.out.println("Started building project...");
                Path p = new Path();
                File pom = p.fileDFS("/clone", "pom.xml");
                String dir = pom.getAbsolutePath().replace("pom.xml", "");
                dir = dir.substring(0, dir.length()-1);
                CloneBuilder cb = new CloneBuilder(dir);
                boolean success = cb.rebuild();
                System.out.println("Finsihed building project...");
                System.out.println("Build success: " + success);

                // check results
                System.out.println("Fetching the test results...");
                ReadTestResults rts = new ReadTestResults();
                try {
                    JSONObject testResults = rts.read("/clone", "surefire-reports");
                    System.out.println("Adding test results to database...");
                    CIServer.dbh.addTestsToBuild(buildID, testResults);
                    if(false) {
                        setCommitStatus(relevant_data, null, "buildFailed", buildID);
                    }else if (testResults.getBoolean("success")) {
                        System.out.println("Passed all the tests!!");
                        setCommitStatus(relevant_data, testResults, "success", buildID);
                    } else {
                        System.out.println("All tests did NOT pass.");
                        setCommitStatus(relevant_data, testResults, "testsFailed", buildID);
                    }

                } catch (ParserConfigurationException e1) {
                    e1.printStackTrace();
                    return "failed";
                }

                //tear down the session
                try {
                    tearDown(cloneDirectoryPath);
                } catch (IOException exception) {
                    exception.printStackTrace();
                    System.out.println("Failed tearDown "+ exception.getMessage());
                }
                System.out.println("Job finished.");
                return "success";
            }
        }
        System.out.println("Job finished.");
        return "failed";
    }


    /**
     * Method that sends pass and fail for the build results.
     * 
     * @param test is test name
     * @param state is the test result 
     * @param sha is the commit id
     */
    public static boolean setCommitStatus(JSONObject relevant_data, JSONObject testResults, String state, int buildID) {
        String statuses_url = relevant_data.getString("statuses_url");
        String sha = relevant_data.getString("sha");
        String user = FileConfig.getRow(0);
        String token = FileConfig.getRow(1);
        String url = statuses_url.replace("{sha}", sha);
        String description = "";
        String target_url = "http://johvh.se/build/"+ buildID;

        if(state.equals("buildFailed")){
            state = "failure";
            description = "Failed while building project";
        }else if(state.equals("testsFailed")){
            state = "failure";
            JSONArray failedTests = testResults.getJSONArray("failed");
            description = "Failed " + failedTests.length() + "tests";
        }else if(state.equals("success")){
            state = "success";
            description = "All tests passed";
        }else{
            state = "pending";
            description = "waiting for result";
        }
        
        String json = "{\"state\": \"" + state + "\", \"description\": \""+ description +"\", \"context\": \"G10ci" +
                    "\", \"target_url\": \""+ target_url+"\"}";
        System.out.println(json); 
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty ("Authorization", "Basic " + Base64.getEncoder().encodeToString((user+":"+token).getBytes()));
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(json);
            wr.flush();
            wr.close();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            in.close();
            return true;
        } catch(Exception e) {
            System.out.println("Failed to set commit message: " + e.getMessage());
        }
        return false;
    }

     /**
     * Clone the given repository and branch from git to the specified file
     * @param url the url of the repository to clone
     * @param branch the branch to clone
     * @param cloneDirectoryPath the file to clone the repo to
     * @return true if cloning was successful
     */
    public static boolean cloneRepository(String url, String branch, File cloneDirectoryPath){
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
     * @throws IOException if deleting directory failed
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
}
