package sqs.core.reports;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import org.apache.log4j.Logger;
import sqs.core.constants.PropertyConstants;
import sqs.framework.FrameworkBase;
import sqs.framework.FrameworkData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * @ScriptName : GenerateCustomeReport
 * @Description : This class generate test execution reports using net.masterthought
 * @Author : Automation Tester (SQS)
 * @Creation Date : 17 Jun 2015   @Modified Date:
 */

public class ExtentReporter extends FrameworkBase {
	// Local variables
	private static Logger logger = Logger.getLogger(ExtentReporter.class);


	public static void generateCustomeReport() {
		try {
			String reportPath = config.getProperty(PropertyConstants.REPORT_FOLDER_PATH);
			String report = config.getProperty(PropertyConstants.REPORT_FOLDER_PATH);
			String jsonFilePath = report + "/cucumber.json";
			List<String> jsonReportFiles = new ArrayList<>();
			try{
				boolean checkFileExists = new File(jsonFilePath).exists();
				if (!checkFileExists) {
					jsonFilePath = FrameworkData.getReportDirPath()+"/cucumber.json";
				}
				jsonReportFiles.add(jsonFilePath);
				String buildNumber = "1";
				String buildProjectName = config.getProperty(PropertyConstants.PROJECT_NAME);
				Boolean runWithJenkins = false;
				File file = new File(reportPath);
				Configuration configuration = new Configuration(file, buildProjectName);
				configuration.setRunWithJenkins(runWithJenkins);
				configuration.setBuildNumber(buildNumber);
				ReportBuilder reportBuilder = new ReportBuilder(jsonReportFiles, configuration);
				reportBuilder.generateReports();
			}catch (net.masterthought.cucumber.ValidationException e){
				System.out.println("Cucumber Json Reports are not available either in " + report + "\n in Default path" );
				e.printStackTrace();
			}



		} catch (Exception exception) {
			logger.error("Report Not Generated, Kindly check testSuite Executed completely :" ,exception);
		}
	}
	
}
