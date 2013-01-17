package org.endeios.security.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.endeios.security.domain.LogEntry;

public class LogEntryService {
	
	@PersistenceContext(unitName="security")
	EntityManager entityManager;
	
	public LogEntry addEntry(LogEntry logEntry){
		entityManager.persist(logEntry);
		return logEntry;
	}
	
	public LogEntry updateEntry(LogEntry logEntry){
		entityManager.merge(logEntry);
		return logEntry;
	}
	
	/*
	 * bastava select * from LogEntry
	 * */
	public List<LogEntry> getAllEntries(){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<LogEntry> q = cb.createQuery(LogEntry.class);
		Root<LogEntry> r = q.from(LogEntry.class);
		//q.where(cb.equal(r.get("zone"), ""));
		q.orderBy(cb.asc(r.get("time")));
		return entityManager.createQuery(q).getResultList();
	}
	
	public List<LogEntry> getEntrysByZone(String zone){
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<LogEntry> q = cb.createQuery(LogEntry.class);
		Root<LogEntry> r = q.from(LogEntry.class);
		q.where(cb.equal(r.get("zone"), zone));
		q.orderBy(cb.asc(r.get("time")));
		return entityManager.createQuery(q).getResultList();
	}
}
