package org.endeios.security.domain;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;

@Entity
@PersistenceContext(unitName = "security")
public class LogEntry {

	@Id
	private UUID uuid;
	private Date time;
	private String event;
	private String zone;

	public LogEntry() {
		this.uuid = UUID.randomUUID();
		this.time = new Date();
	}

	public LogEntry(String event, String zone) {
		this.uuid = UUID.randomUUID();
		this.time = new Date();
		this.event = event;
		this.zone = zone;
	}

	public Date getTime() {
		return time;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

}
