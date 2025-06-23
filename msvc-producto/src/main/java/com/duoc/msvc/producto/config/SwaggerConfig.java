package com.duoc.msvc.producto.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        Contact contact = new Contact();
        contact.setName("Equipo Perfulandia");
        contact.setEmail("soporte@perfulandia.com");
        return new OpenAPI()
                .info(new Info()
                        .title("API - MSVC - Producto")
                        .version("1.0.0")
                        .description("Microservicio de Producto para operaciones CRUD y gestión de productos.")
                        .contact(contact)
                        .summary("API de productos del sistema Perfulandia")
        );
    }
} 