package sqs.pageobjects.pages.web;

import sqs.core.actions.CommonActions;

import static sqs.pageobjects.PageFactory.cyclosLoginPage;

public class CyclosLoginPage extends CommonActions {

	public void enterUsername(String myUserID) {

		action.enterText(myUserID,"UserName");
	}

	public void enterPassword(String password) {
		action.enterText(password, "Password");
	}

	public void clickSignIn() {
		action.click("SignInLink");
	}

	public void clickSignInButton() {
		action.click("SignInButton");
	}

	public void enterCredentialsAndSignIn(String userName, String password){

		clickSignIn();
		enterUsername(userName);
		enterPassword(password);
		clickSignInButton();

	}

}
