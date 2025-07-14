package com.duoc.msvc.sucursal.config;

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
                        .title("Perfulandia - Microservicio de Sucursal")
                        .version("1.0.0")
                        .summary("API para la gestión de sucursales en Perfulandia")
                        .description("""
Este microservicio permite gestionar las sucursales de Perfulandia, incluyendo:
- Creación, actualización, obtención y eliminación de sucursales
- Gestión de inventarios asociados a cada sucursal
- Respuestas simples y con HATEOAS para facilitar la integración

Incluye validaciones, ejemplos realistas y documentación detallada para facilitar el desarrollo y la integración.
""")
                );
    }


} 