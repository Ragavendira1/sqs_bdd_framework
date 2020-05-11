package sqs;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.runner.RunWith;
import sqs.framework.FrameworkBase;

import java.io.IOException;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty",
                "html:Result/Reports",
                "junit:Result/Reports/cucumber.xml",
                "json:Result/Reports/cucumber.json",
                "usage:Result/Reports/cucumber-usage.json",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:report.html",
                "io.qameta.allure.cucumber4jvm.AllureCucumber4Jvm"
        },
        strict = false,
        monochrome = true,
        features = "src/test/resources/Features/",
        glue = {"sqs.cucumber.hooks",
                "sqs.cucumber.stepdefinitions"
        },
        tags = {"@UserPaymentValidation,@Login"})

public class TestRunner {
    private TestRunner() {
    }

    @AfterClass
    public static void generateReports() throws IOException {
        FrameworkBase.afterTestSuite();
        // RunnerClassGenerator.generateRunnerClass("@target/rerun.txt","", FrameworkData.getExtentReportPath());
    }
}
