package com.ticketPing.queue_manage.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Queue-Manage Service API",
                version = "v1"
        )
)
@Configuration
public class SwaggerConfig {
}
