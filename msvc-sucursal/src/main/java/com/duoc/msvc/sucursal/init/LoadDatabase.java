package com.duoc.msvc.sucursal.init;

import com.duoc.msvc.sucursal.models.entities.Inventario;
import com.duoc.msvc.sucursal.models.entities.Sucursal;
import com.duoc.msvc.sucursal.repositories.SucursalRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
            logger.info("LoadDatabase - Inicialización: Generando datos de prueba para sucursales e inventarios");
            for (long i = 1L; i <= 10L; i++) {
                Sucursal sucursal = new Sucursal();
                sucursal.setDireccion(faker.address().streetAddress());
                sucursal.setRegion(faker.address().state());
                sucursal.setComuna(faker.address().city());
                sucursal.setCantidadPersonal(faker.number().numberBetween(5, 20));
                sucursal.setHorariosAtencion("9:00-18:00");

                List<Inventario> inventarios = new ArrayList<>();
                for (long j = 1L; j <= 100L; j++) {
                    Inventario inventario = new Inventario();
                    inventario.setSucursal(sucursal);
                    inventario.setIdProducto(j);
                    if (j <= 20) {
                        inventario.setStock(faker.number().numberBetween(50, 200)); // Stock alto para productos populares
                    } else if (j <= 60) {
                        inventario.setStock(faker.number().numberBetween(10, 80)); // Stock medio
                    } else {
                        if (faker.number().numberBetween(1, 10) <= 8) {
                            inventario.setStock(faker.number().numberBetween(1, 15)); // Stock bajo
                        } else {
                            inventario.setStock(0); // Sin stock
                        }
                    }
                    inventarios.add(inventario);
                }

                sucursal.setInventarios(inventarios);
                Sucursal savedSucursal = sucursalRepository.save(sucursal);
                logger.debug("LoadDatabase - Debug: Sucursal {} creada con {} inventarios", i, savedSucursal.getInventarios().size());
                int totalStock = savedSucursal.getInventarios().stream()
                    .mapToInt(Inventario::getStock)
                    .sum();
                logger.debug("LoadDatabase - Debug: Sucursal {} - Total stock: {}", i, totalStock);
            }

            logger.info("LoadDatabase - Inicialización: Se generaron 10 sucursales con 100 productos cada una (1000 inventarios totales)");
        } else {
            logger.info("LoadDatabase - Inicialización: Las sucursales ya existen en la base de datos");
        }
    }
}
