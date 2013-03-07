package org.endeios.security.service

import org.springframework.beans.factory.annotation.Autowired

class EchoService {
  
  @Autowired
  var logEntryService: LogEntryService = null 
  
  def echo(s: String): String = {
    s
  }
  
  def echoAllEntries() = {
    logEntryService.getAllEntries()
  }
}