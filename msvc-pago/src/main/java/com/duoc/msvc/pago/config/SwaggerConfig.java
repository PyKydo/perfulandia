package com.duoc.msvc.pago.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Perfulandia - Microservicio de Pago")
                        .version("1.0.0")
                        .summary("API para la gestión de pagos en Perfulandia")
                        .description("""
Este microservicio permite gestionar los pagos de Perfulandia, incluyendo:
- Creación, actualización, obtención y eliminación de pagos
- Asociación de pagos a pedidos y envíos
- Respuestas simples y con HATEOAS para facilitar la integración

Incluye validaciones, ejemplos realistas y documentación detallada para facilitar el desarrollo y la integración.
""")
                );
    }
} 