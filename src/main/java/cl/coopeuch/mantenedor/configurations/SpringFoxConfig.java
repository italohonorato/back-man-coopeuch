package cl.coopeuch.mantenedor.configurations;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {
	
	private Environment env;

	public SpringFoxConfig(Environment env) {
		this.env = env;
	}

	@Bean
    public Docket api() { 
        return new Docket(DocumentationType.SWAGGER_2)  
        		.select()
        	      .apis(RequestHandlerSelectors.basePackage(env.getProperty("swagger.app.basepackage")))
        	      .paths(PathSelectors.ant(env.getProperty("swagger.app.path.selector")))
        	      .build()
        	      .apiInfo(apiInfo());                                         
    }
	
	private ApiInfo apiInfo() {
		return new ApiInfo(env.getProperty("swagger.app.title"), 
				env.getProperty("swagger.app.description"), 
				env.getProperty("swagger.app.version"),
				env.getProperty("swagger.app.termsOfServiceUrl"),
				new Contact(env.getProperty("swagger.app.contact.name"), env.getProperty("swagger.app.contact.url"), env.getProperty("swagger.app.contact.email")), 
				env.getProperty("swagger.app.license"),
				env.getProperty("swagger.app.license.url"), 
				Collections.emptyList());
	}
}
