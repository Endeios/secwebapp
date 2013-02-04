package org.endeios.security.vaadin;

import java.util.Date;

import org.endeios.security.service.LogEntryService;
import org.endeios.security.vaadin.helper.SpringContextHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

//@Configurable(preConstruction = true,dependencyCheck=true)
public class SecWebApp extends Application {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4439997529796272044L;
	
	//@Autowired(required=true)
	private LogEntryService logEntryService;

	private SpringContextHelper sch;

	@Override
	public void init() {
		final Window mainWindow = new Window("Sec Web  Application");
		sch = new SpringContextHelper(this);
		logEntryService = (LogEntryService) sch.getBean("logEntryService");
		setMainWindow(mainWindow);
		mainWindow.addComponent(new SecWebAppMainWindow());
		mainWindow.addComponent(new Label(logEntryService.getAllEntries()+""));
	}

}
