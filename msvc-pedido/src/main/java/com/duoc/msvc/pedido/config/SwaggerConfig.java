package com.duoc.msvc.pedido.config;

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
                        .title("Perfulandia - Microservicio de Pedido")
                        .version("1.0.0")
                        .summary("API para la gestión de pedidos en Perfulandia")
                        .description("""
Este microservicio permite gestionar los pedidos de Perfulandia, incluyendo:
- Creación, actualización, obtención y eliminación de pedidos
- Búsqueda por usuario
- Respuestas simples y con HATEOAS para facilitar la integración

Incluye validaciones, ejemplos realistas y documentación detallada para facilitar el desarrollo y la integración.
""")
                );
    }
} 