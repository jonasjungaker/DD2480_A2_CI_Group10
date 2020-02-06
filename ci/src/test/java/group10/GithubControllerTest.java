package group10;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.junit.After;
import org.junit.Test;
import spark.Request;
import spark.Response;

/**
 * Unit test for the github controller
 */
public class GithubControllerTest {

    @After
    public void tearDown() throws IOException {
        GithubController.tearDown(new File("testCloneDirectory"));
    }

    /**
     * Check that the handlePost handles when
     * the repo to clone does not exist.
     * 
     * @throws IOException
     * @throws JSONException
     */
    @Test
    public void handlePostTest() throws JSONException, IOException
    {
        String post = "{\n  \"ref\": \"refs/heads/master\",\n  \"before\": \"6259e9872cee8fd7613bf0ff23168137c136e7d0\",\n  \"after\": \"9243470dc79355fe8b7bb3119ae77fb9c9b24731\",\n  \"repository\": {\n    \"id\": 237233339,\n    \"node_id\": \"MDEwOlJlcG9zaXRvcnkyMzcyMzMzMzk=\",\n    \"name\": \"testwebhooks\",\n    \"full_name\": \"JohanKJIP/testwebhooks\",\n    \"private\": true,\n    \"owner\": {\n      \"name\": \"JohanKJIP\",\n      \"email\": \"JohanKJIP@gmail.com\",\n      \"login\": \"JohanKJIP\",\n      \"id\": 19364769,\n      \"node_id\": \"MDQ6VXNlcjE5MzY0NzY5\",\n      \"avatar_url\": \"https://avatars3.githubusercontent.com/u/19364769?v=4\",\n      \"gravatar_id\": \"\",\n      \"url\": \"https://api.github.com/users/JohanKJIP\",\n      \"html_url\": \"https://github.com/JohanKJIP\",\n      \"followers_url\": \"https://api.github.com/users/JohanKJIP/followers\",\n      \"following_url\": \"https://api.github.com/users/JohanKJIP/following{/other_user}\",\n      \"gists_url\": \"https://api.github.com/users/JohanKJIP/gists{/gist_id}\",\n      \"starred_url\": \"https://api.github.com/users/JohanKJIP/starred{/owner}{/repo}\",\n      \"subscriptions_url\": \"https://api.github.com/users/JohanKJIP/subscriptions\",\n      \"organizations_url\": \"https://api.github.com/users/JohanKJIP/orgs\",\n      \"repos_url\": \"https://api.github.com/users/JohanKJIP/repos\",\n      \"events_url\": \"https://api.github.com/users/JohanKJIP/events{/privacy}\",\n      \"received_events_url\": \"https://api.github.com/users/JohanKJIP/received_events\",\n      \"type\": \"User\",\n      \"site_admin\": false\n    },\n    \"html_url\": \"https://github.com/JohanKJIP/testwebhooks\",\n    \"description\": null,\n    \"fork\": false,\n    \"url\": \"https://github.com/JohanKJIP/testwebhooks\",\n    \"forks_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/forks\",\n    \"keys_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/keys{/key_id}\",\n    \"collaborators_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/collaborators{/collaborator}\",\n    \"teams_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/teams\",\n    \"hooks_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/hooks\",\n    \"issue_events_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/issues/events{/number}\",\n    \"events_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/events\",\n    \"assignees_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/assignees{/user}\",\n    \"branches_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/branches{/branch}\",\n    \"tags_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/tags\",\n    \"blobs_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/git/blobs{/sha}\",\n    \"git_tags_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/git/tags{/sha}\",\n    \"git_refs_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/git/refs{/sha}\",\n    \"trees_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/git/trees{/sha}\",\n    \"statuses_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/statuses/{sha}\",\n    \"languages_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/languages\",\n    \"stargazers_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/stargazers\",\n    \"contributors_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/contributors\",\n    \"subscribers_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/subscribers\",\n    \"subscription_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/subscription\",\n    \"commits_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/commits{/sha}\",\n    \"git_commits_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/git/commits{/sha}\",\n    \"comments_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/comments{/number}\",\n    \"issue_comment_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/issues/comments{/number}\",\n    \"contents_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/contents/{+path}\",\n    \"compare_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/compare/{base}...{head}\",\n    \"merges_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/merges\",\n    \"archive_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/{archive_format}{/ref}\",\n    \"downloads_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/downloads\",\n    \"issues_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/issues{/number}\",\n    \"pulls_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/pulls{/number}\",\n    \"milestones_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/milestones{/number}\",\n    \"notifications_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/notifications{?since,all,participating}\",\n    \"labels_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/labels{/name}\",\n    \"releases_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/releases{/id}\",\n    \"deployments_url\": \"https://api.github.com/repos/JohanKJIP/testwebhooks/deployments\",\n    \"created_at\": 1580395336,\n    \"updated_at\": \"2020-01-31T07:56:49Z\",\n    \"pushed_at\": 1580562404,\n    \"git_url\": \"git://github.com/JohanKJIP/testwebhooks.git\",\n    \"ssh_url\": \"git@github.com:JohanKJIP/testwebhooks.git\",\n    \"clone_url\": \"https://github.com/jonasjungaker/DD2480_A2_CI_Group10\",\n    \"svn_url\": \"https://github.com/JohanKJIP/testwebhooks\",\n    \"homepage\": null,\n    \"size\": 14,\n    \"stargazers_count\": 0,\n    \"watchers_count\": 0,\n    \"language\": null,\n    \"has_issues\": true,\n    \"has_projects\": true,\n    \"has_downloads\": true,\n    \"has_wiki\": true,\n    \"has_pages\": false,\n    \"forks_count\": 0,\n    \"mirror_url\": null,\n    \"archived\": false,\n    \"disabled\": false,\n    \"open_issues_count\": 0,\n    \"license\": null,\n    \"forks\": 0,\n    \"open_issues\": 0,\n    \"watchers\": 0,\n    \"default_branch\": \"master\",\n    \"stargazers\": 0,\n    \"master_branch\": \"master\"\n  },\n  \"pusher\": {\n    \"name\": \"JohanKJIP\",\n    \"email\": \"JohanKJIP@gmail.com\"\n  },\n  \"sender\": {\n    \"login\": \"JohanKJIP\",\n    \"id\": 19364769,\n    \"node_id\": \"MDQ6VXNlcjE5MzY0NzY5\",\n    \"avatar_url\": \"https://avatars3.githubusercontent.com/u/19364769?v=4\",\n    \"gravatar_id\": \"\",\n    \"url\": \"https://api.github.com/users/JohanKJIP\",\n    \"html_url\": \"https://github.com/JohanKJIP\",\n    \"followers_url\": \"https://api.github.com/users/JohanKJIP/followers\",\n    \"following_url\": \"https://api.github.com/users/JohanKJIP/following{/other_user}\",\n    \"gists_url\": \"https://api.github.com/users/JohanKJIP/gists{/gist_id}\",\n    \"starred_url\": \"https://api.github.com/users/JohanKJIP/starred{/owner}{/repo}\",\n    \"subscriptions_url\": \"https://api.github.com/users/JohanKJIP/subscriptions\",\n    \"organizations_url\": \"https://api.github.com/users/JohanKJIP/orgs\",\n    \"repos_url\": \"https://api.github.com/users/JohanKJIP/repos\",\n    \"events_url\": \"https://api.github.com/users/JohanKJIP/events{/privacy}\",\n    \"received_events_url\": \"https://api.github.com/users/JohanKJIP/received_events\",\n    \"type\": \"User\",\n    \"site_admin\": false\n  },\n  \"created\": false,\n  \"deleted\": false,\n  \"forced\": false,\n  \"base_ref\": null,\n  \"compare\": \"https://github.com/JohanKJIP/testwebhooks/compare/6259e9872cee...9243470dc793\",\n  \"commits\": [\n    {\n      \"id\": \"9243470dc79355fe8b7bb3119ae77fb9c9b24731\",\n      \"tree_id\": \"635dd092600cb359bd261a64b8177a9b68805ae1\",\n      \"distinct\": true,\n      \"message\": \"ndsajndk\",\n      \"timestamp\": \"2020-02-01T14:06:34+01:00\",\n      \"url\": \"https://github.com/JohanKJIP/testwebhooks/commit/9243470dc79355fe8b7bb3119ae77fb9c9b24731\",\n      \"author\": {\n        \"name\": \"Johan von Hacht\",\n        \"email\": \"johvh@kth.se\",\n        \"username\": \"JohanKJIP\"\n      },\n      \"committer\": {\n        \"name\": \"Johan von Hacht\",\n        \"email\": \"johvh@kth.se\",\n        \"username\": \"JohanKJIP\"\n      },\n      \"added\": [\n\n      ],\n      \"removed\": [\n\n      ],\n      \"modified\": [\n        \"yes.txt\"\n      ]\n    }\n  ],\n  \"head_commit\": {\n    \"id\": \"9243470dc79355fe8b7bb3119ae77fb9c9b24731\",\n    \"tree_id\": \"635dd092600cb359bd261a64b8177a9b68805ae1\",\n    \"distinct\": true,\n    \"message\": \"ndsajndk\",\n    \"timestamp\": \"2020-02-01T14:06:34+01:00\",\n    \"url\": \"https://github.com/JohanKJIP/testwebhooks/commit/9243470dc79355fe8b7bb3119ae77fb9c9b24731\",\n    \"author\": {\n      \"name\": \"Johan von Hacht\",\n      \"email\": \"johvh@kth.se\",\n      \"username\": \"JohanKJIP\"\n    },\n    \"committer\": {\n      \"name\": \"Johan von Hacht\",\n      \"email\": \"johvh@kth.se\",\n      \"username\": \"JohanKJIP\"\n    },\n    \"added\": [\n\n    ],\n    \"removed\": [\n\n    ],\n    \"modified\": [\n      \"yes.txt\"\n    ]\n  }\n}";
        Request request = new RequestStub(post);
        Response response = new ResponseStub();
        String s = GithubController.handlePost(request, response);
        assertTrue( s.equals("failed") );
    }

    class RequestStub extends Request{
        private String _body;
        RequestStub(String body){
            _body = body;
        }

        public String body(){
            return _body;
        }
    }
    class ResponseStub extends Response{
    }
}
