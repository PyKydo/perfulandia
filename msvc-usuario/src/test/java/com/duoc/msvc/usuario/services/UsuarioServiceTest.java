package com.duoc.msvc.usuario.services;

import com.duoc.msvc.usuario.clients.PedidoClient;
import com.duoc.msvc.usuario.exceptions.UsuarioException;
import com.duoc.msvc.usuario.models.entities.Usuario;
import com.duoc.msvc.usuario.repositories.UsuarioRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PedidoClient pedidoClient;

    @InjectMocks
    private UsuarioService usuarioService;

    private List<Usuario> usuarioList = new ArrayList<>();

    private Usuario usuarioPrueba;

    @BeforeEach
    public void setUp(){
        Faker faker = new Faker(Locale.of("es", "CL"));
        for(int i=0;i<100;i++){
            Usuario usuario = new Usuario();
            usuario.setIdUsuario((long) i+1);
            usuario.setNombre(faker.name().firstName());
            usuario.setApellido(faker.name().lastName());
            usuario.setCiudad(faker.address().city());
            usuario.setRegion(faker.address().state());
            usuario.setComuna(faker.address().cityName());
            usuario.setCorreo(faker.internet().username()+"@gmail.com");
            usuario.setContrasena(faker.internet().password());
            usuario.setTelefono(faker.phoneNumber().phoneNumberNational());
            usuario.setDireccion(faker.address().streetAddress());
            usuario.setCodigoPostal(faker.address().zipCode());

            this.usuarioList.add(usuario);
        }
        this.usuarioPrueba = this.usuarioList.get(0);
    }

}
