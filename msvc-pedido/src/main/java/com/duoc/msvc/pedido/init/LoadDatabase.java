package com.duoc.msvc.pedido.init;

import com.duoc.msvc.pedido.models.entities.DetallePedido;
import com.duoc.msvc.pedido.models.entities.Pedido;
import com.duoc.msvc.pedido.repositories.PedidoRepository;
import net.datafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Profile("dev")
@Component
public class LoadDatabase implements CommandLineRunner {
    @Autowired
    private PedidoRepository pedidoRepository;

    private static final Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(new Locale("es", "CL"));
        Random random = new Random();

        if (pedidoRepository.count() == 0) {
            logger.info("Generando 50 pedidos de prueba...");
            for (int i = 0; i < 50; i++) {
                Pedido pedido = new Pedido();
                pedido.setIdCliente((long) faker.number().numberBetween(1, 100));
                pedido.setMetodoPago(faker.options().option("Efectivo", "Tarjeta", "Transferencia"));
                pedido.setEstado("Nuevo");

                int detallesCount = faker.number().numberBetween(1, 5);
                List<DetallePedido> detalles = new ArrayList<>();
                BigDecimal totalDetalles = BigDecimal.ZERO;

                for (int j = 0; j < detallesCount; j++) {
                    DetallePedido detalle = new DetallePedido();
                    detalle.setIdProducto((long) faker.number().numberBetween(1, 100));
                    detalle.setIdSucursal((long) faker.number().numberBetween(1, 20));
                    int cantidad = faker.number().numberBetween(1, 10);
                    detalle.setCantidad(cantidad);
                    BigDecimal precio = BigDecimal.valueOf(faker.number().randomDouble(2, 1000, 20000));
                    detalle.setPrecio(precio);
                    detalle.setPedido(pedido);
                    detalles.add(detalle);
                    totalDetalles = totalDetalles.add(precio.multiply(BigDecimal.valueOf(cantidad)));
                }

                pedido.setDetallesPedido(detalles);
                pedido.setTotalDetalles(totalDetalles);
                BigDecimal costoEnvio = BigDecimal.valueOf(faker.number().randomDouble(2, 2000, 10000));
                pedido.setCostoEnvio(costoEnvio);
                pedido.setMontoFinal(totalDetalles.add(costoEnvio));

                pedidoRepository.save(pedido);
                logger.debug("Pedido {} creado: {}", i + 1, pedido.toString());
            }
            logger.info("Se generaron 50 pedidos de prueba exitosamente");
        } else {
            logger.info("Ya existen pedidos en la base de datos, no se generaron datos de prueba");
        }
    }
} 