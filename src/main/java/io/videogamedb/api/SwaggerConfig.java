package io.videogamedb.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Collections;
import java.util.List;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket openApiDocket() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .paths(regex("/api.*"))
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false)
                .securitySchemes(Collections.singletonList(authenticationScheme()))
                .securityContexts(Collections.singletonList(securityContext()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Video Game DB 2022 API")
                .description("This is an API for a simple, fictional Video Game Database. The application is running in READ ONLY mode, which means any changes like creating, updating or deleting games does not get persisted.")
                .contact(new Contact("James Willett", "https://james-willett.com", null))
                .version("1.0.0")
                .build();
    }

    private HttpAuthenticationScheme authenticationScheme() {
        return HttpAuthenticationScheme
                .JWT_BEARER_BUILDER
                .name("JWT")
                .build();
    }

    private SecurityContext securityContext() {
        AuthorizationScope[] authorizationScopes = {new AuthorizationScope("global", "accessEverything")};
        List<SecurityReference> securityReferences =
                Collections.singletonList(new SecurityReference("JWT", authorizationScopes));
        return SecurityContext.builder()
                .securityReferences(securityReferences)
                .operationSelector(this::isSecuredOperation)
                .build();
    }

    private boolean isSecuredOperation(OperationContext context) {
        return context.httpMethod() != HttpMethod.GET
                && !context.getName().equals("createAuthenticationToken");
    }
}
