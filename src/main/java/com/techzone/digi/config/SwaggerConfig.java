package com.techzone.digi.config;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.models.auth.In;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.techzone.digi.resource")).paths(PathSelectors.any())
				.build()
				.securitySchemes(Arrays.asList(new ApiKey("Token da API", HttpHeaders.AUTHORIZATION, In.HEADER.name())))
				.apiInfo(apiInfo()).securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Arrays.asList(apiKey()));

	}

	private ApiKey apiKey() {
		return new ApiKey("JWT", "Authorization", "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
	}

	private ApiInfo apiInfo() {
		return new ApiInfo("API do curso Spring Boot",
				"Esta API ?? utilizada no curso de Spring Boot do prof. Nelio Alves", "Vers??o 1.0",
				"https://www.udemy.com/terms",
				new Contact("Nelio Alves", "udemy.com/user/nelio-alves", "nelio.cursos@gmail.com"),
				"Permitido uso para estudantes", "https://www.udemy.com/terms", Collections.emptyList() // Vendor
																										// Extensions
		);
	}
}
