package org.endeios.security.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

public class JsonViewResolver implements ViewResolver{
	
	@Autowired
	private MappingJackson2JsonView mappingJackson2JsonView;

	@Override
	public View resolveViewName(String viewName, Locale locale)
			throws Exception {
		//suboptimal space footprint, optimal readability
		mappingJackson2JsonView.setPrettyPrint(true);
		//do i emit lists? yes please
		mappingJackson2JsonView.setExtractValueFromSingleKeyModel(true);
		return mappingJackson2JsonView;
	}

}
