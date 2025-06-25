package com.duoc.msvc.envio.config;

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
                        .title("Perfulandia - Microservicio de Envío")
                        .version("1.0.0")
                        .summary("API para la gestión de envíos en Perfulandia")
                        .description("""
Este microservicio permite gestionar los envíos de Perfulandia, incluyendo:
- Creación, actualización, obtención y eliminación de envíos
- Cálculo de costo de envío y actualización de estado
- Respuestas simples y con HATEOAS para facilitar la integración

Incluye validaciones, ejemplos realistas y documentación detallada para facilitar el desarrollo y la integración.
""")
                );
    }
} 