Cucumber BDD framework, that supports the automation of Web,Mobile & API using Selenium, Appium & RESTAssured tools & technologies.
## Get the code

Git:

    git clone https://gitlab.com/sqs_bdd_framework/sqs_bdd_framework.git
    cd sqs_bdd_framework


Or simply [download a zip](https://gitlab.com/sqs_bdd_framework/sqs_bdd_framework/-/archive/CyclosWeb/sqs_bdd_framework-CyclosWeb.zip) file.

## Use Maven

Open a command window and run:

    mvn test

This runs Cucumber features using Cucumber's JUnit runner. The `@RunWith(Cucumber.class)` annotation on the `RunCukesTest`
class tells JUnit to kick off Cucumber.


## CyclosWeb Demo
To execute sample scenario's on test application ##Cyclos , check the branch ##CyclosWeb

Open a command window and run

    mvn test

This will execute two BDD scenarios with `Scenario1 @UserPaymentValidation` resulting with status ***PASS*** and `Scenario2 Login` resulting as ***FAIL***

## System Requirements

* JAVA JRE Installed 
* Working Chrome Browser - Since chrome is given as default browser for tests
 