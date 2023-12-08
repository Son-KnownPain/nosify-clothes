package com.nosify.configurations;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class ApplicationInitializer implements WebApplicationInitializer {
    
    private final String TMP_FOLDER = "/resources/images";
    private final int MAX_UPLOAD_SIZE = 5 * 1024 * 1024; // 5MB

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(ApplicationContext.class);
        
        servletContext.addListener(new ContextLoaderListener(applicationContext));
        
        AnnotationConfigWebApplicationContext dispatcherContext
                = new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(WebMvcConfig.class);
        
        ServletRegistration.Dynamic registration
                = servletContext.addServlet(
                        "dispatcher", 
                        new DispatcherServlet(dispatcherContext)
                );
        
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
                TMP_FOLDER, 
                MAX_UPLOAD_SIZE, 
                MAX_UPLOAD_SIZE * 2, 
                MAX_UPLOAD_SIZE / 2
        );
        
        registration.setMultipartConfig(multipartConfigElement);
        
        registration.setAsyncSupported(true);
        registration.setLoadOnStartup(2);
        registration.addMapping("/"); // or *.htm
    }
    
}
