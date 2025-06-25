package com.duoc.msvc.usuario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Perfulandia - Microservicio de Usuario")
                        .version("1.0.0")
                        .description("""
                            API REST para la gestión de usuarios en Perfulandia.
                            
                            Funcionalidades principales:
                            * Gestión de usuarios (CRUD)
                            * Autenticación y autorización
                            * Gestión de pedidos por usuario
                            * Integración con otros microservicios
                            
                            Disponible en versiones Simple y HATEOAS.""")
                        .summary("API para la gestión de usuarios y sus operaciones relacionadas")
        );
    }
} 