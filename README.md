# Assignment 2: CI - Group 10
A minimal continuous integration server that contains the core features of an CI.

## Group members
- Johan von Hacht (JohanKJIP)
- Jonas Jungåker (jonasjungaker)
- Diego Leon (dieflo4711)
- Emma Olsson (emmariaolsson)
- Olivia Stigeborn (seccret)
____ 
## Task
The task of assigment 2 was to implement a continuous integration server. The CI should, triggered by a pull request, clone the repository, compile it, run some tests on it and update the status on Github if the tests passed or failed. An in depth description can be found [here](https://kth.instructure.com/courses/17627/assignments/102477?module_item_id=179216) 

## Installation, compilation and test 
Java and Maven must be installed to run the program. 

## Implementations

### How compilation was implementated and unit-tested

### How execution was implementated and unit-tested

### How notification was implementated and unit-tested
The notification system is implemented in two ways. First a status is sent to github of whether the commit contains functional code or not, this showing in there either being a green check mark or a red cross with the commit. The second is a notificatino sent to slack through their incoming webhook API. 

#### Github status check is implemented through
The test success state is sent to Github by sending a post to github with json data containing information about the test and build results of the commit and the reference to that build sha. The commit status is set by invoking the method `GithubController.setCommitStatus(relevant_data, testResults, state, buildID);` where the relevant_data is the json object containing the relevant build data recieved from github, testResults is the json object containing the testresults from the compilation of the project, state is the string describing the state of the commit and buildID is the reference to the build status on the server. The build status is then shown at server interface at `http://server.johvh.se/build/<buildID>`

#### Slack notifications are implemented through
The slack notifications are implemented by setting up an incoming webhook to the shared workspace of all contributors. A package os json data is sent to the webhook url with message details. 

The notifier is set up by calling 
```java
Notifier notifier = new Notifier();
notifier.webHook = "webhook url";
// A post to slack is sent by invoking the following method
String message = Notifier.createMessage(buildSuccess, testSuccess, buildURL); // (boolean, boolean, String)
notifier.sendNotification(message); // throws IOException if notification fails to post
```

The message creator is tested by generating different message outputs and making sure the correct json output string is obtained.

## Statement of contributions

**Olivia Stigeborn**
- Clone repository functionality
- Convert test results to JSON format
- Connect the different parts together
- Bug fixes
- Generate API documetation using javadoc


**Johan von Hacht**
-


**Jonas Jungåker**
- Compile build and generate reports from clone
- Slack notifications


**Diego Leon**
-

**Emma Olsson**
-

