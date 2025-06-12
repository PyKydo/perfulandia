package com.duoc.msvc.usuario.init;


import com.duoc.msvc.usuario.models.entities.Usuario;
import com.duoc.msvc.usuario.repositories.UsuarioRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Profile("dev")
@Component
public class LoadDatabase implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoadDatabase.class);
    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(Locale.of("es", "CL"));

        if(usuarioRepository.count()==0){
            for(int i=0;i<100;i++){
                Usuario usuario = new Usuario();
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

                logger.info("El nombre del usuario que agregaste es {}", usuario.getNombre());
                usuario = usuarioRepository.save(usuario);
                logger.info("El Nombre usuario creado es: {}", usuario);

                logger.info("El apellido del usuario que agregaste es {}", usuario.getApellido());
                usuario = usuarioRepository.save(usuario);
                logger.info("El Apellido creado es: {}", usuario);

                logger.info("La ciudad del usuario que agregaste es {}", usuario.getCiudad());
                usuario = usuarioRepository.save(usuario);
                logger.info("La Ciudad agregada del usuario creado es: {}", usuario);

                logger.info("La region del usuario que agregaste es {}", usuario.getRegion());
                usuario = usuarioRepository.save(usuario);
                logger.info("La Region del usuario creado es: {}", usuario);

                logger.info("La Comuna del usuario que agregaste es {}", usuario.getComuna());
                usuario = usuarioRepository.save(usuario);
                logger.info("La Comuna del usuario creado es: {}", usuario);

                logger.info("El Correo del usuario que agregaste es {}", usuario.getCorreo());
                usuario = usuarioRepository.save(usuario);
                logger.info("El Correo del  usuario creado es: {}", usuario);

                logger.info("La Contraseña del usuario que agregaste es {}", usuario.getContrasena());
                usuario = usuarioRepository.save(usuario);
                logger.info("La Contraseña del usuario creado es: {}", usuario);

                logger.info("El Telefono del usuario que agregaste es {}", usuario.getTelefono());
                usuario = usuarioRepository.save(usuario);
                logger.info("El Telefono del usuario creado es: {}", usuario);

                logger.info("La Direccion del usuario que agregaste es {}", usuario.getDireccion());
                usuario = usuarioRepository.save(usuario);
                logger.info("La Direccion del usuario creado es: {}", usuario);

                logger.info("El Codigo Postal del usuario que agregaste es {}", usuario.getCodigoPostal());
                usuario = usuarioRepository.save(usuario);
                logger.info("El Codigo postal usuario creado es: {}", usuario);
            }
        }
    }
}
