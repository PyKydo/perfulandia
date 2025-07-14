package com.duoc.msvc.pedido.init;

import com.duoc.msvc.pedido.models.entities.DetallePedido;
import com.duoc.msvc.pedido.models.entities.Pedido;
import com.duoc.msvc.pedido.services.PedidoService;
import com.duoc.msvc.pedido.clients.SucursalClient;
import com.duoc.msvc.pedido.dtos.pojos.SucursalClientDTO;
import com.duoc.msvc.pedido.dtos.pojos.InventarioClientDTO;
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
    private PedidoService pedidoService;

    @Autowired
    private SucursalClient sucursalClient;

    private static final Logger logger = LoggerFactory.getLogger(LoadDatabase.class);

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(new Locale("es", "CL"));
        Random random = new Random();

        if (pedidoService.findAll().getContent().isEmpty()) {
            logger.info("LoadDatabase - Inicialización: Generando 50 pedidos de prueba");
            for (int i = 0; i < 50; i++) {
                Pedido pedido = new Pedido();
                pedido.setIdCliente((long) faker.number().numberBetween(1, 100));
                pedido.setMetodoPago(faker.options().option("Efectivo", "Tarjeta", "Transferencia"));
                pedido.setEstado("Nuevo");

                int detallesCount = faker.number().numberBetween(1, 5);
                List<DetallePedido> detalles = new ArrayList<>();
                BigDecimal totalDetalles = BigDecimal.ZERO;

                int intentos = 0;
                while (detalles.size() < detallesCount && intentos < detallesCount * 3) {
                    intentos++;
                    Long idProducto = (long) faker.number().numberBetween(1, 100);
                    Long idSucursal = (long) faker.number().numberBetween(1, 20);
                    try {
                        SucursalClientDTO sucursal = sucursalClient.getSucursalBestStockByIdProducto(idProducto);
                        if (sucursal != null && sucursal.getInventarios() != null) {
                            InventarioClientDTO inventario = sucursal.getInventarios().stream()
                                .filter(inv -> inv.getIdProducto().equals(idProducto) && inv.getStock() > 0)
                                .findFirst().orElse(null);
                            if (inventario != null) {
                                DetallePedido detalle = new DetallePedido();
                                detalle.setIdProducto(idProducto);
                                detalle.setIdSucursal(sucursal.getId());
                                int cantidad = faker.number().numberBetween(1, Math.min(10, inventario.getStock()));
                                detalle.setCantidad(cantidad);
                                BigDecimal precio = BigDecimal.valueOf(faker.number().randomDouble(2, 1000, 20000));
                                detalle.setPrecio(precio);
                                detalle.setPedido(pedido);
                                detalles.add(detalle);
                                totalDetalles = totalDetalles.add(precio.multiply(BigDecimal.valueOf(cantidad)));
                            }
                        }
                    } catch (Exception e) {
                    }
                }

                if (detalles.isEmpty()) {
                    logger.warn("LoadDatabase - Advertencia: No se pudo crear pedido {} porque no se encontró inventario válido para los productos", i + 1);
                    continue;
                }

                pedido.setDetallesPedido(detalles);
                pedido.setTotalDetalles(totalDetalles);
                BigDecimal costoEnvio = BigDecimal.valueOf(faker.number().randomDouble(2, 2000, 10000));
                pedido.setCostoEnvio(costoEnvio);
                pedido.setMontoFinal(totalDetalles.add(costoEnvio));

                try {
                    pedidoService.save(pedido);
                    logger.debug("LoadDatabase - Debug: Pedido {} creado exitosamente", i + 1);
                } catch (Exception e) {
                    logger.warn("LoadDatabase - Advertencia: No se pudo guardar el pedido {} - {}", i + 1, e.getMessage());
                }
            }
            logger.info("LoadDatabase - Inicialización: Se generaron 50 pedidos de prueba exitosamente (o los que fue posible)");
        } else {
            logger.info("LoadDatabase - Inicialización: Ya existen pedidos en la base de datos, no se generaron datos de prueba");
        }
    }
} 