package com.duoc.msvc.sucursal.init;

import com.duoc.msvc.sucursal.dtos.SucursalCreateDTO;
import com.duoc.msvc.sucursal.models.entities.Inventario;
import com.duoc.msvc.sucursal.models.entities.Sucursal;
import com.duoc.msvc.sucursal.repositories.SucursalRepository;
import com.duoc.msvc.sucursal.services.SucursalService;
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

    @Autowired
    private SucursalService sucursalService;

    private static final Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        if (sucursalRepository.count() == 0) {
            logger.info("Generando datos de prueba para sucursales e inventarios...");

            // Generar 10 sucursales
            for (long i = 1L; i <= 10L; i++) {
                Sucursal sucursal = new Sucursal();
                sucursal.setDireccion(faker.address().streetAddress());
                sucursal.setRegion(faker.address().state());
                sucursal.setComuna(faker.address().city());
                sucursal.setCantidadPersonal(faker.number().numberBetween(5, 20));
                sucursal.setHorariosAtencion("9:00-18:00");

                List<Inventario> inventarios = new ArrayList<>();

                // Asegurar al menos 20 productos con stock > 0
                for (long j = 1L; j <= 100L; j++) {
                    Inventario inventario = new Inventario();
                    inventario.setSucursal(sucursal);
                    inventario.setIdProducto(j);
                    if (j <= 20) {
                        inventario.setStock(faker.number().numberBetween(10, 150)); // Siempre stock positivo
                    } else {
                        inventario.setStock(faker.number().numberBetween(0, 2) == 0 ? 0 : faker.number().numberBetween(1, 20));
                    }
                    inventarios.add(inventario);
                }

                sucursal.setInventarios(inventarios);

                sucursalService.save(toCreateDTO(sucursal));
                logger.debug("Sucursal {} creada con {} inventarios", i, inventarios.size());
            }

            logger.info("Se generaron 10 sucursales con 100 productos cada una");
        } else {
            logger.info("Las sucursales ya existen en la base de datos");
        }
    }

    private SucursalCreateDTO toCreateDTO(Sucursal sucursal) {
        SucursalCreateDTO dto = new SucursalCreateDTO();
        dto.setDireccion(sucursal.getDireccion());
        dto.setRegion(sucursal.getRegion());
        dto.setComuna(sucursal.getComuna());
        dto.setCantidadPersonal(sucursal.getCantidadPersonal());
        dto.setHorariosAtencion(sucursal.getHorariosAtencion());
        return dto;
    }
}
