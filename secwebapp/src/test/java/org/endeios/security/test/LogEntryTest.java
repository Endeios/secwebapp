package org.endeios.security.test;

import static org.junit.Assert.*;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.endeios.security.domain.LogEntry;
import org.endeios.security.service.LogEntryService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:testSpring.xml" })
public class LogEntryTest  extends AbstractJUnit4SpringContextTests {

	Log logger = LogFactory.getLog(LogEntryTest.class);
	@Autowired
	private LogEntryService logEntryService ;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		LogEntry entry = new LogEntry("Test","A");
		logEntryService.addEntry(entry);
		for(LogEntry log: logEntryService.getAllEntries()){
			logger.info(log.getTime()+" "+log.getEvent() +" in "+log.getZone());
		}
	}

}
