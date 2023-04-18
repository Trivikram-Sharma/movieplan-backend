package com.movieplan;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class MovieplanBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieplanBackendApplication.class, args);
	}
	
	
	
	@Bean
	   public Docket productApi() {
	      return new Docket(DocumentationType.SWAGGER_2).select()
	         .apis(RequestHandlerSelectors.basePackage("com.movieplan")).build();
	   }
	
//	@Bean
//	public Docket swaggerConfiguration() {
//		return new Docket(DocumentationType.SWAGGER_2)
//				.select()
//				.apis(RequestHandlerSelectors.basePackage("com.movieplan"))
//				.build()
//				.apiInfo(apiDetails());
//	}
//	
//	private ApiInfo apiDetails() {
//		return new ApiInfo(
//				"Spring Boot Movieplan Api",
//				"Spring Boot Movieplan application with swagger",
//				"1.0",
//				"Free to use",
//				new springfox.documentation.service.Contact("Trivikram Sharma", "https://github.com/Trivikram-Sharma/", "vickygosukonda@gmail.com"),
//				"API License",
//				"https://github.com/Trivikram-Sharma/",
//				Collections.emptyList()
//				);
//	}

}
