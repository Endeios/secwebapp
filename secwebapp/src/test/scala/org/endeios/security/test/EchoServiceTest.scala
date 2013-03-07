package org.endeios.security.test

import org.springframework.test.context.ContextConfiguration
import org.junit.runner.RunWith
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.specs2.matcher.JUnitMustMatchers
import org.specs2.mutable.Specification
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.endeios.security.service.EchoService


@ContextConfiguration(
  Array("classpath:testSpring.xml"))
@RunWith(classOf[SpringJUnit4ClassRunner])
class EchoServiceTest extends JUnitMustMatchers with Specification{
  
  @Autowired
  var echoService:EchoService = null
  
	@Test
	def testEcho{
	  "Echo test" should {
	    "Echo Strings " in {
	      echoService.echo("Hello World") must be equalTo("Hello World")
	    }
	  } 
	}
}