# Assignment 2: CI - Group 10
A minimal continuous integration server that contains the core features of an CI.

Link to builds: http://server.johvh.se/

## Group members
- Johan von Hacht (JohanKJIP)
- Jonas Jungåker (jonasjungaker)
- Diego Leon (dieflo4711)
- Emma Olsson (emmariaolsson)
- Olivia Stigeborn (seccret)
____ 
## Task
The task of assigment 2 was to implement a continuous integration server. The CI should, triggered by a push event, clone the repository, compile it, run some tests on it and update the status on Github if the tests passed or failed. An in depth description can be found [here](https://kth.instructure.com/courses/17627/assignments/102477?module_item_id=179216) 

## Installation, compilation and test 
Java and Maven must be installed to run the program. 

## Implementations

### How compilation was implementated and unit-tested
Compilation was implemented by creating a new process which runs a batch script called `run.sh`. This script moves into the cloned repository in the folder where the `pom.xml` file is located. In this directory it performs the command `mvn install` which downloads the necessary dependencies, compiles the program and executes the tests.

The compilation result was tested, making sure it returns the correct "success" code after compiling, i.e. returns false if compiling failed and true if it succeded.

### How execution was implementated and unit-tested
Execution of the tests have already been performed by `mvn install`. However, we get the results from the tests by reading the test result XML files found in the target folder of maven projects. 

This part of the program was tested by copying a few of these XML files into a test folder and making sure that all the test results were read correctly. 

### How notification was implementated and unit-tested
The notification system is implemented in two ways. First a status is sent to github of whether the commit contains functional code or not, this showing in there either being a green check mark or a red cross with the commit. The second is a notificatino sent to slack through their incoming webhook API. 

#### Github status check is implemented through
Notification was implemented using the Github Status api. The CI server sets the commit status by sending a POST request to the api. The POST request containes the following parameters:

* state: The state of the status. Either "failure" or "success"
* target_url: The target URL to associate with this status. Provides the link for the build output.
* description: A short description of the status.
* context: String that differentiates this status from the status of other systems.

The unit-testing consists of 3 tests. The first test createst a status check with the status "success" and expects method "setCommitStatus" to return true (a status has successfully been created). Test 2 createst a status check with the status "failure" and expects the method to also return true. The last test tries to create a status check using an invalid sha and expects the method to return false (faild to create a status check).

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
- Project setup
- Server setup with ngrok equivalent functionality where `<name>.johvh.se` is a webhook
- Database and databasehandler
- Parse Github requests
- Connect everythihng together
- Bug fixes

**Jonas Jungåker**
- Compile build and generate reports from clone
- Slack notifications


**Diego Leon**
- Implemented notification using the Github Status api
- Created the single build website using html, css, and javascript
- Code review

**Emma Olsson**
-
Website for displaying builds
Implemented templates for both build pages
Code review
