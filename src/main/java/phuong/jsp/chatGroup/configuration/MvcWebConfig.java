package phuong.jsp.chatGroup.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import phuong.jsp.chatGroup.configuration.security.PermissionEvaluator;
import phuong.jsp.chatGroup.configuration.security.UserDeserializer;
import phuong.jsp.chatGroup.entities.User;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebMvc
@ComponentScans(value = {@ComponentScan("phuong.jsp.chatGroup.controller")})
@EnableAsync
@ComponentScan("phuong.jsp.chatGroup.service")
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class MvcWebConfig extends GlobalMethodSecurityConfiguration implements WebMvcConfigurer {

    //ServiceConfiguration
    private final ApplicationContext applicationContext;
    private final PermissionEvaluator permissionEvaluator;

    public MvcWebConfig(ApplicationContext applicationContext, PermissionEvaluator permissionEvaluator) {
        this.applicationContext = applicationContext;
        this.permissionEvaluator = permissionEvaluator;
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper ();
        SimpleModule module = new SimpleModule ();
        module.addDeserializer (User.class, new UserDeserializer ());
        mapper.registerModule (module);
        mapper.registerModule (new Hibernate5Module ());
        mapper.enable (MapperFeature.DEFAULT_VIEW_INCLUSION);
        mapper.setSerializationInclusion (JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion (JsonInclude.Include.NON_EMPTY);
        mapper.configure (DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

    @Override
    public void configureDefaultServletHandling(
            DefaultServletHandlerConfigurer configurer) {
        configurer.enable ();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor (new ThymeleafLayoutInterceptor ());
    }

    @Bean
    public StringHttpMessageConverter stringHttpMessageConverter() {
        return new StringHttpMessageConverter (Charset.forName ("UTF-8"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder ();
    }

    @Override
    public void configureContentNegotiation(
            ContentNegotiationConfigurer configurer) {
        final Map<String, String> parameterMap = new HashMap<> ();
        parameterMap.put ("charset", "utf-8");

        configurer.defaultContentType (new MediaType (
                MediaType.APPLICATION_JSON, parameterMap));
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler (
                "/webjars/**",
                "/vendor/**",
                "/css/**",
                "/js/**",
                "/scss/**",
                "/pug/**")
                .addResourceLocations (
                        "/webjars/",
                        "classpath:/static/vendor/",
                        "classpath:/static/css/",
                        "classpath:/static/js/",
                        "classpath:/static/scss/",
                        "classpath:/static/pug/")
                .setCacheControl (CacheControl
                        .maxAge (2, TimeUnit.HOURS)
                        .cachePublic ());
    }

    /*--------------------------------------------------*/
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        // SpringResourceTemplateResolver automatically integrates with Spring's own
        // resource resolution infrastructure, which is highly recommended.
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver ();
        templateResolver.setApplicationContext (this.applicationContext);
        templateResolver.setPrefix ("/WEB-INF/views/");
        templateResolver.setSuffix (".html");
        // HTML is the default value, added here for the sake of clarity.
        templateResolver.setTemplateMode (TemplateMode.HTML);
        templateResolver.setCharacterEncoding ("UTF-8");
        templateResolver.setCheckExistence (true);
        // Template cache is true by default. Set to false if you want
        // templates to be automatically updated when modified.
        templateResolver.setCacheable (false);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine ();
        templateEngine.setTemplateResolver (templateResolver ());
        templateEngine.setEnableSpringELCompiler (true);
        templateEngine.addDialect (new LayoutDialect ());
        return templateEngine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver ();
        resolver.setTemplateEngine (templateEngine ());
        registry.viewResolver (resolver);
    }

    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver ();
        viewResolver.setTemplateEngine (templateEngine ());
        // NOTE 'order' and 'viewNames' are optional
        viewResolver.setOrder (1);
        viewResolver.setViewNames (new String[]{".html", ".xhtml"});
        return viewResolver;
    }
//ServiceConfiguration


    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler ();
        handler.setPermissionEvaluator (permissionEvaluator);
        handler.setApplicationContext (applicationContext);
        return handler;
    }

}
