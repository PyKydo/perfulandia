# Sistema de Gestión de Pedidos - Microservicios

Sistema distribuido de gestión de pedidos implementado con microservicios en Spring Boot.

## Arquitectura

El sistema está compuesto por los siguientes microservicios:

### 1. Microservicio de Usuarios (msvc-usuario)
- **Puerto**: 8001
- **URL Base**: `http://localhost:8001/api/v1/usuarios`
- **Responsabilidad**: Gestión de usuarios y clientes del sistema
- **Endpoints Principales**:
  - `GET /` - Listar todos los usuarios
  - `GET /{id}` - Obtener usuario por ID
  - `POST /` - Crear nuevo usuario
  - `PUT /{id}` - Actualizar usuario
  - `DELETE /{id}` - Eliminar usuario
  - `GET /{idCliente}/realizarPedido` - Realizar nuevo pedido
  - `GET /{idCliente}/pagarPedido/{idPedido}` - Pagar pedido
  - `GET /{idCliente}/misPedidos` - Ver pedidos de un cliente

### 2. Microservicio de Sucursales (msvc-sucursal)
- **Puerto**: 8002
- **URL Base**: `http://localhost:8002/api/v1/sucursales`
- **Responsabilidad**: Gestión de sucursales y puntos de venta
- **Endpoints Principales**:
  - `GET /` - Listar todas las sucursales
  - `GET /{id}` - Obtener sucursal por ID
  - `POST /` - Crear nueva sucursal
  - `PUT /{id}` - Actualizar sucursal
  - `DELETE /{id}` - Eliminar sucursal
  - `PUT /{idSuc}/inventario/{idInv}/stock` - Actualizar stock de inventario
  - `GET /mejor-stock/{idProducto}` - Obtener sucursal con mejor stock

### 3. Microservicio de Productos (msvc-producto)
- **Puerto**: 8003
- **URL Base**: `http://localhost:8003/api/v1/productos`
- **Responsabilidad**: Gestión de productos y catálogo
- **Endpoints Principales**:
  - `GET /` - Listar todos los productos
  - `GET /{id}` - Obtener producto por ID
  - `POST /` - Crear nuevo producto
  - `PUT /{id}` - Actualizar producto
  - `DELETE /{id}` - Eliminar producto
  - `GET /categoria/{categoria}` - Listar productos por categoría

### 4. Microservicio de Pedidos (msvc-pedido)
- **Puerto**: 8004
- **URL Base**: `http://localhost:8004/api/v1/pedidos`
- **Responsabilidad**: Gestión de pedidos y su ciclo de vida
- **Endpoints Principales**:
  - `GET /` - Listar todos los pedidos
  - `GET /{id}` - Obtener pedido por ID
  - `POST /` - Crear nuevo pedido
  - `GET /cliente/{id}` - Listar pedidos por cliente
  - `PUT /{id}/actualizarEstado/{nuevoEstado}` - Actualizar estado del pedido

### 5. Microservicio de Pagos (msvc-pago)
- **Puerto**: 8005
- **URL Base**: `http://localhost:8005/api/v1/pagos`
- **Responsabilidad**: Gestión de pagos y transacciones
- **Endpoints Principales**:
  - `GET /` - Listar todos los pagos
  - `GET /{id}` - Obtener pago por ID
  - `POST /` - Crear nuevo pago
  - `GET /estado/{estado}` - Listar pagos por estado
  - `PUT /actualizarEstado/{idPedido}/{nuevoEstado}` - Actualizar estado del pago

### 6. Microservicio de Envíos (msvc-envio)
- **Puerto**: 8006
- **URL Base**: `http://localhost:8006/api/v1/envios`
- **Responsabilidad**: Gestión de envíos y entregas
- **Endpoints Principales**:
  - `GET /` - Listar todos los envíos
  - `GET /{id}` - Obtener envío por ID
  - `POST /` - Crear nuevo envío
  - `GET /costoEnvio` - Obtener costo de envío
  - `PUT /actualizarEstado/{idPedido}/{nuevoEstado}` - Actualizar estado del envío

## Flujo de Proceso

1. **Creación de Pedido**:
   - Cliente realiza pedido a través del servicio de usuarios
   - Se verifica disponibilidad en el servicio de productos
   - Se valida la sucursal en el servicio de sucursales
   - Se crea el pedido en el servicio de pedidos
   - Se genera el pago en el servicio de pagos
   - Se crea el envío en el servicio de envíos

2. **Proceso de Pago**:
   - Cliente paga el pedido
   - Se actualiza estado del pedido a "Pagado"
   - Se actualiza estado del pago a "Completado"
   - Se actualiza estado del envío a "Enviado"

## Tecnologías Utilizadas

- Java 21
- Spring Boot 3
- Spring Cloud
- OpenFeign
- JPA/Hibernate
- MySQL
- Maven
- Intellij IDEA CE
- Postman

## Configuración

Cada microservicio tiene su propio archivo `application.properties` con la configuración necesaria:

- Conexión y configuración con la base de datos
- Puertos

## Notas Importantes

- Cada microservicio utiliza H2 Database como motor de base de datos
- La comunicación entre microservicios se realiza mediante OpenFeign
- Los estados de los pedidos son: Nuevo, Pagado, Enviado, Cancelado
- Los estados de los pagos son: Pendiente, Completado, Cancelado
- Los estados de los envíos son: Pendiente, Enviado 