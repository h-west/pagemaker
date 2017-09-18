package com.hsjang.pagemaker.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.accept.PathExtensionContentNegotiationStrategy;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.BeanNameViewResolver;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Configuration
public class ViewConfig implements InitializingBean{
	
	@Autowired
	ContentNegotiatingViewResolver contentNegotiatingViewResolver;
	
	@Autowired
	MustacheViewResolver mustacheViewResolver;
	
	@Autowired
	InternalResourceViewResolver internalResourceViewResolver;
	
	@Autowired
	BeanNameViewResolver beanNameViewResolver;
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Map< String, MediaType > mediaTypes = new HashMap< String, MediaType >();
		mediaTypes.put( "json", MediaType.APPLICATION_JSON );
		mediaTypes.put( "jsonp", MediaType.APPLICATION_JSON );
		PathExtensionContentNegotiationStrategy pathExts = new PathExtensionContentNegotiationStrategy( mediaTypes );
			
		List<ViewResolver> resolvers = new ArrayList<ViewResolver>();
		
		resolvers.add(mustacheViewResolver);
		//resolvers.add(internalResourceViewResolver);
		//resolvers.add(beanNameViewResolver);

		List< View > views = new ArrayList< View >();
		views.add( new MappingJackson2JsonView() );

		contentNegotiatingViewResolver.setViewResolvers( resolvers );
		contentNegotiatingViewResolver.setDefaultViews( views );
		contentNegotiatingViewResolver.setContentNegotiationManager( new ContentNegotiationManager( pathExts )  );
		
	}
}
