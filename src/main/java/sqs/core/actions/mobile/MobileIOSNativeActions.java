package sqs.core.actions.mobile;

import cucumber.runtime.CucumberException;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.touch.TouchActions;

public class MobileIOSNativeActions extends MobileIOSActions {
	private static Logger logger = Logger.getLogger(MobileIOSNativeActions.class);

	@Override
	public boolean clickAndHoldTextboxByIndex(int index) {

		TouchActions touchAction = new TouchActions(driver);
		WebElement element;
		try {
			element = getElement(By.xpath("//UITextField[" + index + "]"), true, true);
		} catch (Exception e) {
			throw new CucumberException(e);
		}
		touchAction.longPress(element).release().perform();
		logger.info("Longpressed on  element");
		return false;
	}

	@Override
	public void openApplication() {
		logger.debug("Open Application is not required for native Apps");
	}
}
