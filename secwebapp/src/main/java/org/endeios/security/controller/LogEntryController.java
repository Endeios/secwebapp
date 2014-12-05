package org.endeios.security.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.endeios.security.domain.LogEntry;
import org.endeios.security.service.LogEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.XmlMappingException;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/v1/API")
public class LogEntryController {

	Log logger = LogFactory.getLog(LogEntryController.class);
	
	@Autowired
	private LogEntryService logEntryService;
	
	@Autowired
	private ObjectMapper jacksonObjectMapper;
	
	@Autowired
	private CastorMarshaller castorMarshaller;

	private static  Log log = LogFactory.getLog(LogEntryController.class);
	
	@RequestMapping(value="/entries")
	public List<LogEntry> getAllEntries(){
		List<LogEntry> entries = logEntryService.getAllEntries();
		logger.info(entries);
		return entries;
	}
	
	@RequestMapping(value="/entries.pdf")
	public void getAllEntriesAsPdf(HttpServletResponse response) throws FOPException, IOException{
		List<LogEntry> entries = logEntryService.getAllEntries();
		logger.info(entries);
		FopFactory fopFactory = FopFactory.newInstance();
		FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
		ServletOutputStream out = response.getOutputStream();

		Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
		
		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			ClassLoader cl = this.getClass().getClassLoader();
			InputStream res = cl.getResourceAsStream("AllEvents2Pdf.xsl");
			Transformer transformer = factory.newTransformer(new StreamSource(
					res));
			StringWriter sw = new StringWriter();
			castorMarshaller.marshal(entries, new StreamResult(sw));
			// Source source = sw.getBuffer();
			InputStream inputStream = new ByteArrayInputStream(sw.toString()
					.getBytes());
			Source xmlSource = new StreamSource(inputStream);
			StringWriter fopSw = new StringWriter();
			Result fopRes = new SAXResult(fop.getDefaultHandler());
			// transformer.transform(xmlSource , new StreamResult(fopSw) );
			transformer.transform(xmlSource, fopRes);

			log .info(fopSw.toString());
		} catch (TransformerException | XmlMappingException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
		
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
