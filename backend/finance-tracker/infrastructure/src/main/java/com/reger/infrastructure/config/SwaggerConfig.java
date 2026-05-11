package com.reger.infrastructure.config;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenApiCustomizer logoutEndpointCustomizer() {
        return openApi -> {
            PathItem logoutPath = new PathItem()
                    .post(new Operation()
                            .addTagsItem("Auth")
                            .summary("Logout")
                            .description("Invalidates the session and clears JSESSIONID cookie")
                            .parameters(List.of(
                                    new Parameter()
                                            .in("cookie")
                                            .name("JSESSIONID")
                                            .required(true)
                                            .description("Session cookie")
                                            .schema(new StringSchema())
                            ))
                            .responses(new ApiResponses()
                                    .addApiResponse("200", new ApiResponse()
                                            .description("Successfully logged out"))
                                    .addApiResponse("401", new ApiResponse()
                                            .description("Not authenticated"))
                            )
                    );

            openApi.getPaths().addPathItem("/api/auth/logout", logoutPath);
        };
    }
}