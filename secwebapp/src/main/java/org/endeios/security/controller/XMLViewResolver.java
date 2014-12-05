package org.endeios.security.controller;

import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.xml.MarshallingView;

public class XMLViewResolver implements ViewResolver {
	
	@Autowired
	private MarshallingView marshallingView;
	
	@Autowired
	private CastorMarshaller castorMarshaller;
	
	@PostConstruct
	public void init(){
	}

	@Override
	public View resolveViewName(String viewName, Locale locale)
			throws Exception {
		return marshallingView;
	}
	
}
