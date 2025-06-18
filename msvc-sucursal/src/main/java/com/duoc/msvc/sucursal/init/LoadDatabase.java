package com.duoc.msvc.sucursal.init;

import com.duoc.msvc.sucursal.models.entities.Sucursal;
import com.duoc.msvc.sucursal.repositories.SucursalRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class LoadDatabase implements CommandLineRunner {
    @Autowired
    private SucursalRepository sucursalRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        if (sucursalRepository.count() == 0) {
            for (int i = 0; i < 10; i++) {
                Sucursal sucursal = new Sucursal();
                sucursal.setDireccion(faker.address().streetAddress());
                sucursal.setRegion(faker.address().state());
                sucursal.setComuna(faker.address().city());
                sucursal.setCantidadPersonal(faker.number().numberBetween(5, 20));
                sucursal.setHorariosAtencion("9:00-18:00");

                sucursalRepository.save(sucursal);
            }
            logger.info("Se generaron 10 sucursales de prueba");
        }

    }
}
