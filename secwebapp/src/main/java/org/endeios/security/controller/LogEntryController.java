package org.endeios.security.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.endeios.security.domain.LogEntry;
import org.endeios.security.service.LogEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/v1/API")
public class LogEntryController {

	Log logger = LogFactory.getLog(LogEntryController.class);
	
	@Autowired
	private LogEntryService logEntryService;
	
	@Autowired
	private ObjectMapper jacksonObjectMapper;
	
	@RequestMapping(value="/entries")
	public List<LogEntry> getAllEntries(){
		List<LogEntry> entries = logEntryService.getAllEntries();
		logger.info(entries);
		return entries;
	}
	
	@RequestMapping(value="/entry",method=RequestMethod.GET,produces={"application/json"})
	public void getAllEntries2(HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException{
		List<LogEntry> entries = logEntryService.getAllEntries();
		logger.info(entries);
		response.addHeader("Content-type", "application/json");
		jacksonObjectMapper.writeValue(response.getOutputStream(), entries);
	}
	@RequestMapping(value="/entry",method=RequestMethod.POST,produces={"application/json"})
	public void putEntry(HttpServletRequest request, HttpServletResponse response) throws JsonParseException, JsonMappingException, IOException{
		Map<String, String> logEntryFromStream = jacksonObjectMapper.readValue(
				request.getInputStream(),
				new TypeReference<Map<String, String>>() {
				});
		LogEntry entry = new LogEntry(logEntryFromStream.get("event"),logEntryFromStream.get("zone"));
		LogEntry returnEntry = logEntryService.addEntry(entry);
		response.addHeader("Content-type", "application/json");
		jacksonObjectMapper.writeValue(response.getOutputStream(), returnEntry );
		
	}
}
