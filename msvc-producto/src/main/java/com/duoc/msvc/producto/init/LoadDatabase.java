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
        for(int i=0;i<1000;i++){
            Producto producto = new Producto();
            producto.setNombre(faker.commerce().productName());
            producto.setMarca(faker.company().name());
            String precioStr = faker.commerce().price(1000, 10000);
            BigDecimal precio = new BigDecimal(precioStr.replace(",",""));
            producto.setPrecio(precio);
            producto.setDescripcion(faker.lorem().sentence());
            producto.setPorcentajeConcentracion(faker.number().randomDouble(0,1,40));

            logger.info("prueba 1 Nombre {}",producto.getNombre());
            producto = productoRepository.save(producto);
            logger.info("prueba 1 exitosa {}",producto);

            logger.info("prueba 2 Nombre Compañia {}",producto.getMarca());
            producto = productoRepository.save(producto);
            logger.info("prueba 2 exitosa {}",producto);

            logger.info("prueba 3 Precio de Producto {}",producto.getPrecio());
            producto = productoRepository.save(producto);
            logger.info("prueba 3 exitosa {}",producto);

            logger.info("prueba 4 Descripcion de Producto  {}",producto.getDescripcion());
            producto = productoRepository.save(producto);
            logger.info("prueba 4 exitosa {}",producto);

            logger.info("prueba 5  {}",producto.getPorcentajeConcentracion());
            producto = productoRepository.save(producto);
            logger.info("prueba 5 exitosa {}",producto);
        }
    }
    }
}
