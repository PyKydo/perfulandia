package com.duoc.msvc.pedido.config;

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
                        .title("API - MSVC - Pedido")
                        .version("1.0.0")
                        .description("Microservicio de Pedido para operaciones CRUD y gestión de pedidos.")
                        .contact(contact)
                        .summary("API de pedidos del sistema Perfulandia")
        );
    }
} 