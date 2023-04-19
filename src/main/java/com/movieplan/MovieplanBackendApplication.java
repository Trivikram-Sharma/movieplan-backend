package com.movieplan;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

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
	
//	@Value("${allowed.origin}")
//	private String allowedOrigin;
	
	
	@Bean
	   public Docket productApi() {
	      return new Docket(DocumentationType.SWAGGER_2).select()
	         .apis(RequestHandlerSelectors.basePackage("com.movieplan")).build();
	   }
	
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			public void addCorsMapping(CorsRegistry registry) {
				registry.addMapping("/**")
				.allowedOrigins("http://localhost:4200");
			}
		};
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
