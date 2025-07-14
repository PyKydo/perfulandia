package com.duoc.msvc.producto.init;

import com.duoc.msvc.producto.models.entities.Producto;
import com.duoc.msvc.producto.repositories.ProductoRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Locale;

@Profile("dev")
@Component
public class LoadDatabase implements CommandLineRunner {
    @Autowired
    private ProductoRepository productoRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    @Override
    public void run(String... args) throws Exception {
    Faker faker = new Faker(Locale.of("es","CL"));

    if (productoRepository.count()==0){
        logger.info("LoadDatabase - Inicialización: Generando 100 productos de prueba");
        for(int i=0;i<100;i++){
            Producto producto = new Producto();

            String nombreBase = faker.options().option(
                    faker.artist().name(),
                    faker.space().constellation(),
                    faker.ancient().hero(),
                    faker.gameOfThrones().character(),
                    faker.weather().description()
            );

            producto.setNombre(faker.options().option(nombreBase) + " " + faker.options().option("Extrait", "Eau de Parfum", "Eau de Toilette", "Eau de Cologne"));
            producto.setMarca(faker.options().option("Channel", "Dior", "Victoria's Secrets", "Giorgio Armani", "Perfume", "Ralph Lauren", "Versace", "Rabanne"));
            producto.setPrecio(BigDecimal.valueOf(faker.number().numberBetween(5000, 120000)));
            producto.setDescripcion(faker.lorem().sentence());
            producto.setPorcentajeConcentracion(faker.number().randomDouble(0, 1, 40));

            producto = productoRepository.save(producto);

            logger.debug("LoadDatabase - Debug: Producto {} creado exitosamente", i+1);
        }
        logger.info("LoadDatabase - Inicialización: Se generaron 100 productos de prueba exitosamente");
    } else {
        logger.info("LoadDatabase - Inicialización: Ya existen productos en la base de datos, no se generaron datos de prueba");
    }

    }
}
