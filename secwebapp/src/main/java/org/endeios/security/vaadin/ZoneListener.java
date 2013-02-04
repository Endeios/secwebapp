package org.endeios.security.vaadin;

import org.endeios.security.domain.LogEntry;
import org.endeios.security.service.LogEntryService;

import com.vaadin.event.Action.Listener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class ZoneListener implements Listener, ClickListener, BlurListener {
	
	private String zone;
	private LogEntryService logEntryService;
	private SecWebAppMainWindow window;

	public ZoneListener(String zone,LogEntryService logEntryService,SecWebAppMainWindow window) {
		this.logEntryService = logEntryService;
		this.zone = zone;
		this.window = window;
	}

	public void handleAction(Object sender, Object target) {
			logEntryService.addEntry(new LogEntry("intrusione!!", this.zone));
			window.refreshTable();
	}
	
	public void handleAction(){
		logEntryService.addEntry(new LogEntry("intrusione!!", this.zone));
		window.refreshTable();
	}

	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		handleAction();
	}

	public void blur(BlurEvent event) {
		// TODO Auto-generated method stub
		handleAction();
	}

}
