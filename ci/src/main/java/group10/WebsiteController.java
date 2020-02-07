package group10;

import spark.Request;
import spark.Response;

import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class WebsiteController {

    /**
     * Handle the Website get event: get info about build history from database
     * return response.
     * 
     * @param request  from website to server
     * @param response send back info from database
     * @return build history from database
     */
    public static String handleGet(Request request, Response response) {
        JSONArray builds = CIServer.dbh.getBuilds(0, 25);
        Map<String, Object> buildModel = new HashMap<>();
        buildModel.put("builds", builds);
        
        return new VelocityTemplateEngine().render(new ModelAndView(buildModel, "public/exampleTemplate.html"));
    } 

    /**
     * Handle the Website get event: get info on specific build
     * @param request from website to server
     * @param response send back info from database
     * @return VelocityTemplateEngine with the hmtl page for a specific build
     */
    public static String handleGetBuild(Request request, Response response) 
    {
        Map<String, Object> model = new HashMap<>();
        JSONObject build = new JSONObject("{\"date\":\"2020-02-06\",\"elapsed\":4.492999999999999,\"passed_tests\":[{\"elapsed\":\"0.006\",\"name\":\"fileDFSTest\",\"className\":\"group10.PathTest\"},{\"elapsed\":\"0.004\",\"name\":\"fileDoesNotExistTest\",\"className\":\"group10.PathTest\"},{\"elapsed\":\"0.074\",\"name\":\"resultToJsonPassingTest\",\"className\":\"group10.ReadTestResultTest\"},{\"elapsed\":\"0.013\",\"name\":\"resultToJsonTest\",\"className\":\"group10.ReadTestResultTest\"},{\"elapsed\":\"0.0\",\"name\":\"parseTest\",\"className\":\"group10.JsonUtilTest\"},{\"elapsed\":\"0.0\",\"name\":\"parseEmptyTest\",\"className\":\"group10.JsonUtilTest\"},{\"elapsed\":\"0.0\",\"name\":\"parseExamleString\",\"className\":\"group10.JsonUtilTest\"},{\"elapsed\":\"2.028\",\"name\":\"handlePostTest\",\"className\":\"group10.GithubControllerTest\"},{\"elapsed\":\"0.0\",\"name\":\"messageSuccessfulTest\",\"className\":\"group10.NotifierTest\"},{\"elapsed\":\"0.0\",\"name\":\"messageFailureTest\",\"className\":\"group10.NotifierTest\"},{\"elapsed\":\"0.0\",\"name\":\"messageUrlTest\",\"className\":\"group10.NotifierTest\"},{\"elapsed\":\"0.241\",\"name\":\"addBuildTest\",\"className\":\"group10.DatabaseHandlerTest\"},{\"elapsed\":\"0.035\",\"name\":\"getSingleBuildTest\",\"className\":\"group10.DatabaseHandlerTest\"},{\"elapsed\":\"0.011\",\"name\":\"getBuildsTest\",\"className\":\"group10.DatabaseHandlerTest\"},{\"elapsed\":\"0.028\",\"name\":\"expectNoAddedBuild\",\"className\":\"group10.DatabaseHandlerTest\"},{\"elapsed\":\"0.01\",\"name\":\"addTestsToBuild\",\"className\":\"group10.DatabaseHandlerTest\"},{\"elapsed\":\"0.008\",\"name\":\"addTestsEmptyTests\",\"className\":\"group10.DatabaseHandlerTest\"},{\"elapsed\":\"1.121\",\"name\":\"cloneTest\",\"className\":\"group10.CloneTest\"},{\"elapsed\":\"0.385\",\"name\":\"branchNotExistTest\",\"className\":\"group10.CloneTest\"},{\"elapsed\":\"0.43\",\"name\":\"covertClassesTest\",\"className\":\"group10.TestRunnerTest\"},{\"elapsed\":\"0.004\",\"name\":\"convertClassesExpectClassNotFound\",\"className\":\"group10.TestRunnerTest\"},{\"elapsed\":\"0.006\",\"name\":\"findClassFilesInsideDirectory\",\"className\":\"group10.TestRunnerTest\"},{\"elapsed\":\"0.003\",\"name\":\"findClassNoClassFilesInsideDirectory\",\"className\":\"group10.TestRunnerTest\"},{\"elapsed\":\"0.004\",\"name\":\"findClassFilesSameDirectory\",\"className\":\"group10.TestRunnerTest\"},{\"elapsed\":\"0.0\",\"name\":\"successfulTest\",\"className\":\"group10.BuildTest\"},{\"elapsed\":\"0.0\",\"name\":\"testFailTest\",\"className\":\"group10.BuildTest\"},{\"elapsed\":\"0.0\",\"name\":\"compileFailTest\",\"className\":\"group10.BuildTest\"},{\"elapsed\":\"0.08\",\"name\":\"parseTest\",\"className\":\"group10.ParseTest\"},{\"elapsed\":\"0.001\",\"name\":\"parseEmptyTest\",\"className\":\"group10.ParseTest\"}],\"number_failed\":1,\"author\":\"JohanKJIP\",\"buildID\":89,\"number_passed\":29,\"branch\":\"refs/heads/dnsakndk\",\"failed_tests\":[{\"elapsed\":\"0.001\",\"name\":\"shouldAnswerWithTrue\",\"className\":\"group10.AppTest\",\"message\":\"blabla\"}],\"status\":\"failed\"}");
        model.put("buildID", request.params(":buildID"));
        model.put("build", build);
                
        return new VelocityTemplateEngine().render(new ModelAndView(model, "public/build.html"));
    } 
    
    public static String render(JSONArray model, String templatePath) {
        return new VelocityTemplateEngine().render(
            new ModelAndView(model, templatePath)
            );
    }

}