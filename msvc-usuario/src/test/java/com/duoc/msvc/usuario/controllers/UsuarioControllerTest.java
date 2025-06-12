package com.duoc.msvc.usuario.controllers;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.swing.text.Document;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    @DisplayName("Debe entregar una lista de todos los usuarios")
    public void shouldReturnAllUsuarioWhenListIsquired(){
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/usuarios", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int usuarioCount = documentContext.read("$.length()");
        assertThat(usuarioCount).isEqualTo(1000);
    }
}
