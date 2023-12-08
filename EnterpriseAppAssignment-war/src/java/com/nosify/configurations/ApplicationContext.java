package com.nosify.configurations;

import com.nosify.services.MailSenderService;
import com.nosify.services.UserTokenService;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
public class ApplicationContext {
    private JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl bean = new JavaMailSenderImpl();
        bean.setHost("smtp.gmail.com");
        bean.setPort(587);
        bean.setUsername("nhsona21171@cusc.ctu.edu.vn");
        bean.setPassword("son@cu$c");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.auth", true);
        javaMailProperties.put("mail.smtp.starttls.enable", true);
        bean.setJavaMailProperties(javaMailProperties);
        
        return bean;
    }
    
    @Bean
    public MailSenderService mailSenderService() {
        MailSenderService bean = new MailSenderService();
        bean.setMailSender(mailSender());
        
        return bean;
    }
    
    @Bean
    public UserTokenService userTokenService() {
        return new UserTokenService();
    }
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
