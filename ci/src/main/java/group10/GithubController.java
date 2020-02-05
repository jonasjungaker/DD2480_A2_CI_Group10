package group10;

import spark.Request;
import spark.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.JSONException;
import org.json.JSONObject;

import group10.util.JsonUtil;

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
    public static String handlePost(Request request, Response response){
        // turn data string into a map structure
        System.out.println(request.body());
        JSONObject all_data = json.toMap(request.body());   
        JSONObject relevant_data = json.getRelevantData(all_data);
        System.out.println(relevant_data.toString());
        
        // todo set commit pending
        // clone repo
        File cloneDirectoryPath = new File("clone");
        boolean cloned = false;
        try {
            cloned = cloneRepository(relevant_data.getString("clone_url"), relevant_data.getString("ref"),
                    cloneDirectoryPath);
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("Failed cloning with: "+ e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed cloning with: "+ e.getMessage());
        }
        
        // compile repo
        // run tests
        // set commit

        //tear down the session
        try {
            tearDown(cloneDirectoryPath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed tearDown "+ e.getMessage());
        }

        //Check that everything went as it should
        if(cloned){
            return "success";
        }
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
            e.printStackTrace();
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
}