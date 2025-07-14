package com.duoc.msvc.usuario.controllers;

import com.duoc.msvc.usuario.models.entities.Usuario;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioControllerTest {

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    @DirtiesContext
    public void shouldReturnAllUsuarioWhenListIsRequested() {
        Usuario usuario1 = new Usuario();
        usuario1.setNombre("Juan Perez");
        usuario1.setApellido("Perez");
        usuario1.setCorreo("juan.perez@mail.com");
        usuario1.setTelefono("+56 9 1234 5678");
        usuario1.setRegion("Región Metropolitana");
        usuario1.setComuna("Santiago");
        usuario1.setCiudad("Santiago");
        usuario1.setDireccion("Calle Falsa 123");
        usuario1.setCodigoPostal("1234567");
        ResponseEntity<String> postResponse1 = restTemplate.postForEntity("/api/v1/usuarios", usuario1, String.class);
        DocumentContext postContext1 = JsonPath.parse(postResponse1.getBody());
        Number idUsuario1 = postContext1.read("$.id");

        Usuario usuario2 = new Usuario();
        usuario2.setNombre("Pedro Gomez");
        usuario2.setApellido("Gomez");
        usuario2.setCorreo("pedro.gomez@mail.com");
        usuario2.setTelefono("+56 9 8765 4321");
        usuario2.setRegion("Valparaíso");
        usuario2.setComuna("Viña del Mar");
        usuario2.setCiudad("Viña del Mar");
        usuario2.setDireccion("Calle Nueva 456");
        usuario2.setCodigoPostal("7654321");
        ResponseEntity<String> postResponse2 = restTemplate.postForEntity("/api/v1/usuarios", usuario2, String.class);
        DocumentContext postContext2 = JsonPath.parse(postResponse2.getBody());
        Number idUsuario2 = postContext2.read("$.id");

        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/usuarios", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int usuariosCount = documentContext.read("$.length()");
        assertThat(usuariosCount).isGreaterThanOrEqualTo(2);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).contains(idUsuario1, idUsuario2);

        JSONArray nombres = documentContext.read("$..nombre");
        assertThat(nombres).contains("Juan Perez", "Pedro Gomez");
    }

    @Test
    @DirtiesContext
    public void shouldReturnAnUsuarioWhenFindById() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan Perez");
        usuario.setApellido("Perez");
        usuario.setCorreo("juan.perez@mail.com");
        usuario.setTelefono("+56 9 1234 5678");
        usuario.setRegion("Región Metropolitana");
        usuario.setComuna("Santiago");
        usuario.setCiudad("Santiago");
        usuario.setDireccion("Calle Falsa 123");
        usuario.setCodigoPostal("1234567");
        ResponseEntity<String> postResponse = restTemplate.postForEntity("/api/v1/usuarios", usuario, String.class);
        DocumentContext postContext = JsonPath.parse(postResponse.getBody());
        Number idUsuario = postContext.read("$.id");

        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/usuarios/" + idUsuario, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number idUsuarioResp = documentContext.read("$.id");
        assertThat(idUsuarioResp).isEqualTo(idUsuario);
        String nombre = documentContext.read("$.nombre");
        assertThat(nombre).isEqualTo("Juan Perez");
    }

    @Test
    public void shouldReturnAnUsuarioWithUnknownId() {
        ResponseEntity<String> response = restTemplate.getForEntity("/api/v1/usuarios/99999", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        if (documentContext.read("$.status", Integer.class) != null) {
            Number status = documentContext.read("$.status");
            assertThat(status).isEqualTo(404);
        }
    }

    @Test
    @DirtiesContext
    public void shouldCreateANewUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Alexander Carré");
        usuario.setApellido("Carré");
        usuario.setCorreo("alex.carre@mail.com");
        usuario.setTelefono("+56 9 1111 2222");
        usuario.setRegion("Antofagasta");
        usuario.setComuna("Antofagasta");
        usuario.setCiudad("Antofagasta");
        usuario.setDireccion("Calle Norte 789");
        usuario.setCodigoPostal("1112223");
        ResponseEntity<String> response = restTemplate.postForEntity("/api/v1/usuarios", usuario, String.class);
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number idUsuario = documentContext.read("$.id");
        assertThat(idUsuario).isNotNull();
        String nombre = documentContext.read("$.nombre");
        assertThat(nombre).isEqualTo("Alexander Carré");
    }
}