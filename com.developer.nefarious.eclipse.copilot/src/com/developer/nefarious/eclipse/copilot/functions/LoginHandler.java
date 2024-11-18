package com.developer.nefarious.eclipse.copilot.functions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Shell;

import com.developer.nefarious.eclipse.copilot.ui.ServiceKeyInputDialog;

public class LoginHandler extends Action {
	
	private Browser browser;
	private Shell shell;
	
	public LoginHandler(Browser browser, Shell shell) {
		this.browser = browser;
		this.shell = shell;
	}

	@Override
	public void run() {
		ServiceKeyInputDialog serviceKeyInputDialog = ServiceKeyInputDialog.create(shell, null, "Please, insert the SAP AI Core Service Key: ", null, null);
		serviceKeyInputDialog.open();
	}

}