package group10;

import static spark.Spark.*;

import group10.util.Path;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.json.JSONObject;

public class CIServer {

    Git git;
    File cloneDirectoryPath;

    // used to start the CI server in command line
    public static void main(String[] args) throws Exception
    {
        port(8080);
        // this is where HTML, CSS and images are stored
        staticFiles.location("/public");
        // this is the end point for displaying list of previous builds
        get(Path.HOME, (req, res) -> "todo make a list of builds!");
        // this is the github post entrypoint
        post(Path.GITHUB, (req, res) -> GithubController.handlePost(req, res));

        // print if an error occurs
        exception(Exception.class, (exception, request, response) -> {
            exception.printStackTrace();
        });
    }

        /**
     * Parse a HttpServletRequest into JSONObject
     * @param request POST request
     * @return JSONObject parsed request
     * @throws IOException
     */
    public JSONObject parseRequest(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        StringBuffer buf = new StringBuffer();
        String line;
        while ((line = reader.readLine()) != null) {
            buf.append(line);
        }
        String data = buf.toString();
        if (data.length() == 0 || data.charAt(0) != '{') {
            return new JSONObject();
        } else  {
            return new JSONObject(data);
        }
    }
    
     /**
     * Clone the given repository & branch from git to the specified file
     * @param url the url of the repository to clone
     * @param branch the branch to clone
     * @param cloneDirectoryPath the file to clone the repo to
     * @return true if cloning was successful
     */
    public boolean cloneRepository(String url, String branch, File cloneDirectoryPath) throws IOException {
        // Set which branch to clone
        ArrayList<String> branches = new ArrayList<>();
        String branchPath = "refs/heads/" + branch;
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
    public void tearDown(File file) throws IOException {
        if(git != null){
            git.close();
            git = null;
        }
        if(file.exists()){
            FileUtils.deleteDirectory(file);
        }
    }
 
}