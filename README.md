# Perfulandia - Arquitectura de Microservicios

## Índice

- [Perfulandia - Arquitectura de Microservicios](#perfulandia---arquitectura-de-microservicios)
  - [Índice](#índice)
  - [Descripción General](#descripción-general)
  - [Arquitectura del Sistema](#arquitectura-del-sistema)
    - [Stack Tecnológico Principal](#stack-tecnológico-principal)
    - [Patrones de Diseño Implementados](#patrones-de-diseño-implementados)
  - [Microservicios](#microservicios)
    - [1. **msvc-usuario** (Puerto 8001)](#1-msvc-usuario-puerto-8001)
    - [2. **msvc-producto** (Puerto 8003)](#2-msvc-producto-puerto-8003)
    - [3. **msvc-sucursal** (Puerto 8002)](#3-msvc-sucursal-puerto-8002)
    - [4. **msvc-pedido** (Puerto 8004)](#4-msvc-pedido-puerto-8004)
    - [5. **msvc-envio** (Puerto 8005)](#5-msvc-envio-puerto-8005)
    - [6. **msvc-pago** (Puerto 8006)](#6-msvc-pago-puerto-8006)
  - [Estandarización de Endpoints](#estandarización-de-endpoints)
    - [Convención de Nomenclatura](#convención-de-nomenclatura)
    - [Ejemplos de Endpoints](#ejemplos-de-endpoints)
  - [Comunicación Entre Microservicios](#comunicación-entre-microservicios)
    - [OpenFeign Clients](#openfeign-clients)
    - [Flujo de Integración](#flujo-de-integración)
  - [HATEOAS (Hypermedia as the Engine of Application State)](#hateoas-hypermedia-as-the-engine-of-application-state)
    - [Implementación Mejorada](#implementación-mejorada)
    - [Enlaces HATEOAS Cruzados](#enlaces-hateoas-cruzados)
    - [Tipos de Enlaces Implementados](#tipos-de-enlaces-implementados)
  - [Validación y Manejo de Errores](#validación-y-manejo-de-errores)
    - [Bean Validation](#bean-validation)
    - [Global Exception Handler](#global-exception-handler)
    - [Estructura de Error](#estructura-de-error)
  - [Documentación de APIs](#documentación-de-apis)
    - [Swagger/OpenAPI](#swaggeropenapi)
    - [Descripciones Estandarizadas](#descripciones-estandarizadas)
    - [Anotaciones de Documentación](#anotaciones-de-documentación)
  - [Inicialización de Datos](#inicialización-de-datos)
    - [DataFaker Integration](#datafaker-integration)
    - [Datos Generados](#datos-generados)
  - [Configuración y Despliegue](#configuración-y-despliegue)
    - [Propiedades de Aplicación](#propiedades-de-aplicación)
    - [Puertos de Servicios](#puertos-de-servicios)
  - [Ejecución del Proyecto](#ejecución-del-proyecto)
    - [Requisitos Previos](#requisitos-previos)
    - [Comandos de Ejecución](#comandos-de-ejecución)
  - [Tests Unitarios](#tests-unitarios)
    - [Tests de Integración](#tests-de-integración)
  - [Monitoreo y Logging](#monitoreo-y-logging)
    - [Configuración de Logging](#configuración-de-logging)
    - [Estandarización de Logging](#estandarización-de-logging)
      - [**Global Exception Handlers**](#global-exception-handlers)
      - [**Servicios de Negocio**](#servicios-de-negocio)
      - [**Procesos de Inicialización**](#procesos-de-inicialización)
    - [Estado de Logging por Microservicio](#estado-de-logging-por-microservicio)
    - [Estandarización de Logging Implementada](#estandarización-de-logging-implementada)
      - [**Global Exception Handlers**](#global-exception-handlers-1)
      - [**Servicios de Negocio**](#servicios-de-negocio-1)
      - [**Procesos de Inicialización**](#procesos-de-inicialización-1)
    - [Ejemplos de Logging Implementado](#ejemplos-de-logging-implementado)
      - [**Operaciones CRUD**](#operaciones-crud)
      - [**Procesos de Negocio Complejos**](#procesos-de-negocio-complejos)
      - [**Comunicación Entre Microservicios**](#comunicación-entre-microservicios-1)
      - [**Inicialización de Datos**](#inicialización-de-datos-1)
    - [Beneficios del Logging Estandarizado](#beneficios-del-logging-estandarizado)
    - [Configuración de Niveles de Log](#configuración-de-niveles-de-log)
  - [Consideraciones de Seguridad](#consideraciones-de-seguridad)
    - [Validación de Entrada](#validación-de-entrada)
    - [Manejo de Errores](#manejo-de-errores)
    - [Autenticación y Autorización](#autenticación-y-autorización)
  - [Escalabilidad y Performance](#escalabilidad-y-performance)
    - [Optimizaciones Implementadas](#optimizaciones-implementadas)
    - [Consideraciones de Escalabilidad](#consideraciones-de-escalabilidad)
  - [Características Destacadas](#características-destacadas)
    - [Estandarización Completa](#estandarización-completa)
    - [Integración Avanzada](#integración-avanzada)
    - [Arquitectura Robusta](#arquitectura-robusta)
  - [Licencia](#licencia)

---

## Descripción General

Perfulandia es una plataforma de comercio electrónico especializada en la venta de perfumes, implementada mediante una arquitectura de microservicios robusta y escalable. El sistema está construido con Java 21, Spring Boot 3.4.5, Spring Cloud 2024.0.1 y utiliza patrones de diseño modernos para garantizar alta disponibilidad, escalabilidad y mantenibilidad.

## Arquitectura del Sistema

### Stack Tecnológico Principal

- **Java**: Versión 21 (LTS) con características modernas como Records, Pattern Matching y Virtual Threads
- **Spring Boot**: 3.4.5 con configuración automática y starters optimizados
- **Spring Cloud**: 2024.0.1 para gestión de microservicios
- **Spring Data JPA**: Persistencia de datos con Hibernate 6.x
- **Spring HATEOAS**: Implementación de REST con hipermedia
- **Spring Cloud OpenFeign**: Cliente HTTP declarativo para comunicación entre servicios
- **H2 Database**: Base de datos en memoria para desarrollo y pruebas
- **Lombok**: Reducción de código boilerplate
- **SpringDoc OpenAPI**: Documentación automática de APIs
- **DataFaker**: Generación de datos de prueba realistas

### Patrones de Diseño Implementados

- **Arquitectura de Microservicios**: Servicios independientes y desacoplados
- **DTO Pattern**: Separación entre entidades de dominio y objetos de transferencia
- **Repository Pattern**: Abstracción de la capa de persistencia
- **Service Layer Pattern**: Lógica de negocio centralizada
- **HATEOAS**: Hipermedia como motor del estado de la aplicación
- **Circuit Breaker Pattern**: Manejo de fallos en comunicación entre servicios
- **Global Exception Handling**: Gestión centralizada de errores

## Microservicios

### 1. **msvc-usuario** (Puerto 8001)

**Responsabilidades:**

- Gestión completa del ciclo de vida de usuarios
- Autenticación y autorización de usuarios
- Integración con servicios de pedidos y pagos
- Validación de datos de entrada con Bean Validation

**Entidades Principales:**

```java
@Entity
@Table(name = "usuarios")
public class Usuario {
    private Long idUsuario;
    private String nombre, apellido, region, comuna, ciudad;
    private String direccion, codigoPostal, correo, contrasena, telefono;
}
```

**APIs Expuestas:**

- `GET /api/v1/usuarios` - Listado simple (predeterminado)
- `GET /api/v1/usuarios-hateoas` - Listado con HATEOAS
- `POST /api/v1/usuarios` - Creación de usuarios
- `PUT /api/v1/usuarios/{id}` - Actualización
- `DELETE /api/v1/usuarios/{id}` - Eliminación
- `GET /api/v1/usuarios/{id}/misPedidos` - Pedidos por usuario
- `POST /api/v1/usuarios/{id}/realizarPedido` - Realizar pedido
- `GET /api/v1/usuarios/{id}/pagarPedido/{idPedido}` - Pagar pedido

**Integración:**

- Consume servicios de pedidos mediante OpenFeign
- Expone datos de usuario para otros microservicios

### 2. **msvc-producto** (Puerto 8003)

**Responsabilidades:**

- Catálogo completo de productos
- Gestión de inventarios y stock
- Categorización y búsqueda de productos
- Información detallada de fragancias

**Entidades Principales:**

```java
@Entity
@Table(name = "productos")
public class Producto {
    private Long idProducto;
    private String nombre, marca, descripcion, categoria;
    private BigDecimal precio;
    private String imagenRepresentativaURL;
    private Double porcentajeConcentracion;
}
```

**APIs Expuestas:**

- `GET /api/v1/productos` - Listado simple (predeterminado)
- `GET /api/v1/productos-hateoas` - Listado con HATEOAS
- `GET /api/v1/productos/categoria/{categoria}` - Filtrado por categoría
- `GET /api/v1/productos/marca/{marca}` - Filtrado por marca
- `POST /api/v1/productos` - Creación de productos
- `PUT /api/v1/productos/{id}` - Actualización
- `DELETE /api/v1/productos/{id}` - Eliminación

**Características Especiales:**

- Categorización automática de fragancias
- Gestión de concentración de perfumes (3% - 30%)
- URLs de imágenes representativas
- Precios con precisión decimal

### 3. **msvc-sucursal** (Puerto 8002)

**Responsabilidades:**

- Gestión de ubicaciones físicas
- Control de inventarios por sucursal
- Optimización de stock y disponibilidad
- Información de personal y horarios

**Entidades Principales:**

```java
@Entity
@Table(name = "sucursales")
public class Sucursal {
    private Long idSucursal;
    private String direccion, region, comuna, horariosAtencion;
    private Integer cantidadPersonal;
    @OneToMany(mappedBy = "sucursal")
    private List<Inventario> inventarios;
}

@Entity
@Table(name = "inventarios")
public class Inventario {
    private Long idInventario, idProducto;
    private Integer stock;
    @ManyToOne
    private Sucursal sucursal;
}
```

**APIs Expuestas:**

- `GET /api/v1/sucursales` - Listado simple (predeterminado)
- `GET /api/v1/sucursales-hateoas` - Listado con HATEOAS
- `GET /api/v1/sucursales/{id}` - Detalle de sucursal
- `POST /api/v1/sucursales` - Creación
- `PUT /api/v1/sucursales/{id}` - Actualización
- `DELETE /api/v1/sucursales/{id}` - Eliminación
- `GET /api/v1/sucursales/mejor-stock/{idProducto}` - Sucursal con mejor stock
- `GET /api/v1/sucursales/disponibilidad/{idProducto}` - Consultar disponibilidad
- `PUT /api/v1/sucursales/{idSuc}/inventario/{idInv}/stock` - Actualizar stock

**Funcionalidades Avanzadas:**

- Algoritmo de selección de sucursal con mejor stock
- Actualización automática de inventarios
- Integración con catálogo de productos

### 4. **msvc-pedido** (Puerto 8004)

**Responsabilidades:**

- Orquestación de todo el proceso de compra
- Gestión de carritos y detalles de pedido
- Integración con todos los microservicios
- Cálculo de totales y costos

**Entidades Principales:**

```java
@Entity
@Table(name = "pedidos")
public class Pedido {
    private Long idPedido, idCliente;
    private String fecha, metodoPago, estado;
    private BigDecimal costoEnvio, totalDetalles, montoFinal;
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<DetallePedido> detallesPedido;
}

@Entity
@Table(name = "detalles_pedido")
public class DetallePedido {
    private Long idDetallePedido, idProducto, idSucursal;
    private Integer cantidad;
    private BigDecimal precio;
    @ManyToOne
    private Pedido pedido;
}
```

**APIs Expuestas:**

- `GET /api/v1/pedidos` - Listado simple (predeterminado)
- `GET /api/v1/pedidos-hateoas` - Listado con HATEOAS
- `GET /api/v1/pedidos/{id}` - Detalle de pedido
- `POST /api/v1/pedidos` - Creación de pedido
- `PUT /api/v1/pedidos/{id}` - Actualización
- `DELETE /api/v1/pedidos/{id}` - Eliminación
- `GET /api/v1/pedidos/cliente/{id}` - Pedidos por cliente
- `PUT /api/v1/pedidos/{id}/actualizarEstado/{estado}` - Actualizar estado

**Integración Completa:**

- Valida existencia de usuario
- Verifica stock en sucursales
- Calcula costos de envío
- Gestiona pagos asociados
- Actualiza inventarios automáticamente
- Enlaces HATEOAS cruzados a productos y usuarios

### 5. **msvc-envio** (Puerto 8005)

**Responsabilidades:**

- Gestión completa del proceso de envío
- Cálculo de costos de transporte
- Seguimiento de estados de entrega
- Estimación de fechas de entrega

**Entidades Principales:**

```java
@Entity
@Table(name = "envios")
public class Envio {
    private Long idEnvio, idPedido;
    private BigDecimal costo;
    private String direccion, ciudad, comuna, region, codigoPostal, estado;
    private LocalDateTime fechaEstimadaEntrega;
}
```

**APIs Expuestas:**

- `GET /api/v1/envios` - Listado simple (predeterminado)
- `GET /api/v1/envios-hateoas` - Listado con HATEOAS
- `GET /api/v1/envios/{id}` - Detalle de envío
- `POST /api/v1/envios` - Creación de envío
- `PUT /api/v1/envios/{id}` - Actualización
- `DELETE /api/v1/envios/{id}` - Eliminación
- `GET /api/v1/envios/costoEnvio` - Cálculo de costo
- `PUT /api/v1/envios/actualizarEstado/{idPedido}/{estado}` - Actualización de estado

**Algoritmo de Costos:**

- Cálculo basado en distancia (5-100 km)
- Factores de peso (0.5-20 kg)
- Tarifa base + tarifa por km + tarifa por kg
- Redondeo a enteros para simplicidad

### 6. **msvc-pago** (Puerto 8006)

**Responsabilidades:**

- Procesamiento de transacciones financieras
- Gestión de métodos de pago
- Validación de pedidos
- Integración con envíos

**Entidades Principales:**

```java
@Entity
@Table(name = "pagos")
public class Pago {
    private Long idPago, idPedido;
    private String metodo, estado;
    private BigDecimal monto;
    private LocalDateTime fecha;
}
```

**APIs Expuestas:**

- `GET /api/v1/pagos` - Listado simple (predeterminado)
- `GET /api/v1/pagos-hateoas` - Listado con HATEOAS
- `GET /api/v1/pagos/{id}` - Detalle de pago
- `GET /api/v1/pagos/estado/{estado}` - Filtrado por estado
- `POST /api/v1/pagos` - Creación de pago
- `PUT /api/v1/pagos/{id}` - Actualización
- `DELETE /api/v1/pagos/{id}` - Eliminación
- `PUT /api/v1/pagos/actualizarEstado/{idPedido}/{estado}` - Actualización de estado

**Estados de Pago:**

- Pendiente
- Completado
- Rechazado
- Cancelado

## Estandarización de Endpoints

### Convención de Nomenclatura

El sistema implementa una convención estandarizada para todos los microservicios:

- **Endpoints Predeterminados**: `/api/v1/[microservicio]` - Respuestas simples sin HATEOAS
- **Endpoints HATEOAS**: `/api/v1/[microservicio]-hateoas` - Respuestas con enlaces HATEOAS

### Ejemplos de Endpoints

| Microservicio | Simple (Predeterminado) | HATEOAS                      |
| ------------- | ----------------------- | ---------------------------- |
| Usuario       | `/api/v1/usuarios`      | `/api/v1/usuarios-hateoas`   |
| Producto      | `/api/v1/productos`     | `/api/v1/productos-hateoas`  |
| Sucursal      | `/api/v1/sucursales`    | `/api/v1/sucursales-hateoas` |
| Pedido        | `/api/v1/pedidos`       | `/api/v1/pedidos-hateoas`    |
| Envío         | `/api/v1/envios`        | `/api/v1/envios-hateoas`     |
| Pago          | `/api/v1/pagos`         | `/api/v1/pagos-hateoas`      |

## Comunicación Entre Microservicios

### OpenFeign Clients

Cada microservicio implementa clientes OpenFeign para comunicación HTTP declarativa:

```java
@FeignClient(name = "pedido-service", url = "${msvc.pedido.url}")
public interface PedidoClient {
    @GetMapping("/{id}")
    ResponseEntity<PedidoClientDTO> findById(@PathVariable Long id);

    @PutMapping("/actualizarEstado/{id}/{estado}")
    String updateEstadoById(@PathVariable Long id, @PathVariable String estado);
}
```

### Flujo de Integración

1. **Creación de Pedido:**

   - Usuario → Pedido → Producto (validación)
   - Pedido → Sucursal (selección de stock)
   - Pedido → Envío (cálculo de costo)
   - Pedido → Pago (creación de transacción)

2. **Procesamiento de Pago:**

   - Pago → Pedido (validación)
   - Pago → Envío (activación)

3. **Gestión de Envío:**
   - Envío → Pedido (actualización de estado)

## HATEOAS (Hypermedia as the Engine of Application State)

### Implementación Mejorada

Cada microservicio expone dos versiones de APIs con enlaces HATEOAS optimizados:

- **Simple**: Respuestas JSON estándar para clientes que no requieren navegación
- **HATEOAS**: Respuestas enriquecidas con enlaces internos y cruzados entre microservicios

### Enlaces HATEOAS Cruzados

El sistema implementa enlaces HATEOAS que conectan diferentes microservicios:

```json
{
  "_embedded": {
    "pedidoList": [
      {
        "idPedido": 1,
        "idCliente": 5,
        "estado": "Pendiente",
        "_links": {
          "self": {
            "href": "http://localhost:8004/api/v1/pedidos-hateoas/1"
          },
          "pedidos": {
            "href": "http://localhost:8004/api/v1/pedidos-hateoas"
          },
          "cliente": {
            "href": "http://localhost:8001/api/v1/usuarios/5"
          },
          "producto_1": {
            "href": "http://localhost:8003/api/v1/productos/1"
          },
          "producto_2": {
            "href": "http://localhost:8003/api/v1/productos/3"
          }
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8004/api/v1/pedidos-hateoas"
    }
  }
}
```

### Tipos de Enlaces Implementados

1. **Enlaces Internos**: Navegación dentro del mismo microservicio
2. **Enlaces Cruzados**: Conexiones entre diferentes microservicios
3. **Enlaces de Acción**: Operaciones específicas como actualizar estado
4. **Enlaces de Relación**: Conexiones semánticas entre entidades

## Validación y Manejo de Errores

### Bean Validation

Todos los DTOs implementan validaciones robustas:

```java
public class UsuarioCreateDTO {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Email(message = "El formato del correo electrónico no es válido")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;
}
```

### Global Exception Handler

Manejo centralizado de excepciones con respuestas estructuradas:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UsuarioException.class)
    public ResponseEntity<ErrorDTO> handleUsuarioException(UsuarioException ex) {
        ErrorDTO error = new ErrorDTO();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setDate(new Date());
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        error.setErrors(errors);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
```

### Estructura de Error

```json
{
  "status": 400,
  "date": "2024-03-20T15:30:00.000Z",
  "errors": {
    "campo": "mensaje de error específico"
  }
}
```

## Documentación de APIs

### Swagger/OpenAPI

Cada microservicio incluye documentación automática con descripciones estandarizadas:

- **msvc-usuario**: http://localhost:8001/swagger-ui.html
- **msvc-producto**: http://localhost:8003/swagger-ui.html
- **msvc-sucursal**: http://localhost:8002/swagger-ui.html
- **msvc-pedido**: http://localhost:8004/swagger-ui.html
- **msvc-envio**: http://localhost:8005/swagger-ui.html
- **msvc-pago**: http://localhost:8006/swagger-ui.html

### Descripciones Estandarizadas

Todos los controladores implementan descripciones consistentes:

**Controladores Simple:**

```
"Endpoints para gestión de [microservicio] sin HATEOAS. Respuestas simples, ideales para clientes que no requieren enlaces."
```

**Controladores HATEOAS:**

```
"Endpoints para gestión de [microservicio] con HATEOAS. Las respuestas incluyen enlaces para navegación RESTful."
```

### Anotaciones de Documentación

```java
@Operation(summary = "Obtener usuario por ID",
          description = "Retorna un usuario específico por su identificador único")
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Usuario encontrado",
        content = @Content(mediaType = "application/json",
        schema = @Schema(implementation = UsuarioSimpleDTO.class))),
    @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
})
@GetMapping("/{id}")
public ResponseEntity<UsuarioSimpleDTO> findById(@PathVariable Long id)
```

## Inicialización de Datos

### DataFaker Integration

Cada microservicio incluye carga automática de datos de prueba:

```java
@Profile("dev")
@Component
public class LoadDatabase implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker(Locale.of("es", "CL"));

        if (usuarioRepository.count() == 0) {
            for (int i = 0; i < 100; i++) {
                Usuario usuario = new Usuario();
                usuario.setNombre(faker.name().firstName());
                usuario.setApellido(faker.name().lastName());
                // ... más campos
                usuarioRepository.save(usuario);
            }
        }
    }
}
```

### Datos Generados

- **Usuarios**: 100 registros con datos realistas chilenos
- **Productos**: 1000 perfumes con marcas y categorías
- **Sucursales**: 10 ubicaciones con inventarios completos
- **Pedidos**: Generados automáticamente durante pruebas

## Configuración y Despliegue

### Propiedades de Aplicación

```properties
# Configuración base
spring.application.name=msvc-usuario
server.port=8001
spring.profiles.active=dev

# Base de datos H2
spring.datasource.url=jdbc:h2:file:./data/msvc_usuario_dev
spring.datasource.username=sa
spring.datasource.password=sa
spring.datasource.driver-class-name=org.h2.Driver

# JPA/Hibernate
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Swagger
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.enable=true

# URLs de microservicios (endpoints predeterminados)
msvc.pedido.url=localhost:8004/api/v1/pedidos
msvc.producto.url=localhost:8003/api/v1/productos
msvc.sucursal.url=localhost:8002/api/v1/sucursales
msvc.usuario.url=localhost:8001/api/v1/usuarios
msvc.pago.url=localhost:8006/api/v1/pagos
msvc.envio.url=localhost:8005/api/v1/envios
```

### Puertos de Servicios

| Microservicio | Puerto | Descripción             | Endpoints Predeterminados |
| ------------- | ------ | ----------------------- | ------------------------- |
| msvc-usuario  | 8001   | Gestión de usuarios     | `/api/v1/usuarios`        |
| msvc-sucursal | 8002   | Gestión de sucursales   | `/api/v1/sucursales`      |
| msvc-producto | 8003   | Catálogo de productos   | `/api/v1/productos`       |
| msvc-pedido   | 8004   | Orquestación de pedidos | `/api/v1/pedidos`         |
| msvc-envio    | 8005   | Gestión de envíos       | `/api/v1/envios`          |
| msvc-pago     | 8006   | Procesamiento de pagos  | `/api/v1/pagos`           |

## Ejecución del Proyecto

### Requisitos Previos

- Java 21 (JDK)
- Maven 3.8+

### Comandos de Ejecución

```bash
# Clonar repositorio
git clone <repository-url>
cd perfulandia

# Compilar proyecto completo
mvn clean compile

# Ejecutar microservicios individualmente
cd msvc-usuario && mvn spring-boot:run
cd ../msvc-producto && mvn spring-boot:run
cd ../msvc-sucursal && mvn spring-boot:run
cd ../msvc-pedido && mvn spring-boot:run
cd ../msvc-envio && mvn spring-boot:run
cd ../msvc-pago && mvn spring-boot:run

# Ejecutar tests
mvn test
```

## Tests Unitarios

Cada microservicio incluye tests unitarios completos:

```java
@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Test
    void whenFindById_thenReturnUsuario() {
        Long id = 1L;
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        EntityModel<Usuario> result = usuarioService.findById(id);

        assertThat(result.getContent().getIdUsuario()).isEqualTo(id);
    }
}
```

### Tests de Integración

Tests que validan la comunicación entre microservicios:

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PedidoIntegrationTest {
    @Test
    void whenCreatePedido_thenValidateAllServices() {
        // Test de integración completa
    }
}
```

## Monitoreo y Logging

### Configuración de Logging

```properties
# Logging detallado para desarrollo
logging.level.org.hibernate.SQL=debug
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=trace
logging.level.org.springframework.transaction=debug
logging.level.org.springframework.orm.jpa=debug
logging.level.com.duoc.msvc=debug
```

### Estandarización de Logging

El sistema implementa un estándar completo de logging en todos los microservicios siguiendo estas convenciones:

#### **Global Exception Handlers**

- **Formato**: `"GlobalExceptionHandler - [tipo_excepción]: [mensaje]"`
- **Implementado en**: Todos los microservicios (msvc-usuario, msvc-producto, msvc-sucursal, msvc-pedido, msvc-envio, msvc-pago)

#### **Servicios de Negocio**

- **Formato**: `"Iniciando [operación] de [entidad]"`
- **Formato**: `"[Entidad] creado/a exitosamente con ID: [id]"`
- **Formato**: `"[Entidad] actualizado/a exitosamente"`
- **Formato**: `"[Entidad] eliminado/a exitosamente"`
- **Formato**: `"Error: [descripción_del_error]"`
- **Implementado en**: Todos los servicios principales

#### **Procesos de Inicialización**

- **Formato**: `"LoadDatabase - Inicialización: [descripción_del_proceso]"`
- **Formato**: `"LoadDatabase - Debug: [detalles_específicos]"`
- **Implementado en**: msvc-usuario, msvc-producto, msvc-sucursal, msvc-pedido

### Estado de Logging por Microservicio

| Microservicio     | GlobalExceptionHandler | Servicios        | Inicialización  | Estado       |
| ----------------- | ---------------------- | ---------------- | --------------- | ------------ |
| **msvc-usuario**  | ✅ Implementado        | ✅ Estandarizado | ✅ Implementado | **Completo** |
| **msvc-producto** | ✅ Implementado        | ✅ Estandarizado | ✅ Implementado | **Completo** |
| **msvc-sucursal** | ✅ Implementado        | ✅ Estandarizado | ✅ Implementado | **Completo** |
| **msvc-pedido**   | ✅ Implementado        | ✅ Estandarizado | ✅ Implementado | **Completo** |
| **msvc-envio**    | ✅ Implementado        | ✅ Estandarizado | ❌ No aplica    | **Completo** |
| **msvc-pago**     | ✅ Implementado        | ✅ Estandarizado | ❌ No aplica    | **Completo** |

### Estandarización de Logging Implementada

El sistema implementa un estándar completo de logging en todos los microservicios siguiendo estas convenciones:

#### **Global Exception Handlers**

- **Formato**: `"GlobalExceptionHandler - [tipo_excepción]: [mensaje]"`
- **Implementado en**: Todos los microservicios (msvc-usuario, msvc-producto, msvc-sucursal, msvc-pedido, msvc-envio, msvc-pago)

#### **Servicios de Negocio**

- **Formato**: `"Iniciando [operación] de [entidad]"`
- **Formato**: `"[Entidad] creado/a exitosamente con ID: [id]"`
- **Formato**: `"[Entidad] actualizado/a exitosamente"`
- **Formato**: `"[Entidad] eliminado/a exitosamente"`
- **Formato**: `"Error: [descripción_del_error]"`
- **Implementado en**: Todos los servicios principales

#### **Procesos de Inicialización**

- **Formato**: `"LoadDatabase - Inicialización: [descripción_del_proceso]"`
- **Formato**: `"LoadDatabase - Debug: [detalles_específicos]"`
- **Implementado en**: msvc-usuario, msvc-producto, msvc-sucursal, msvc-pedido

### Ejemplos de Logging Implementado

#### **Operaciones CRUD**

```java
logger.info("Iniciando búsqueda de todos los usuarios");
logger.info("Búsqueda completada. Se encontraron {} usuarios", usuarios.size());
logger.info("Usuario creado exitosamente con ID: {}", savedUsuario.getIdUsuario());
logger.error("Error: El usuario con id {} no existe", id);
```

#### **Procesos de Negocio Complejos**

```java
logger.info("Iniciando proceso de realización de pedido para cliente ID: {}", pedidoClientDTO.getIdCliente());
logger.info("Procesando {} detalles de pedido", pedido.getDetallesPedido().size());
logger.info("Stock disponible: {}, Cantidad solicitada: {}", inventario.getStock(), detalle.getCantidad());
logger.info("Totales calculados - Detalles: {}, Envío: {}, Final: {}", totalDetalles, costoEnvio, montoFinal);
```

#### **Comunicación Entre Microservicios**

```java
logger.info("Enviando pedido al servicio de pedidos. Estado: {}", pedidoClientDTO.getEstado());
logger.info("Consultando pedido con ID: {}", idPedido);
logger.warn("No se pudo obtener información del cliente: {}", e.getMessage());
logger.error("Error al procesar el pago: {}", e.getMessage(), e);
```

#### **Inicialización de Datos**

```java
logger.info("LoadDatabase - Inicialización: Generando 100 usuarios de prueba");
logger.debug("LoadDatabase - Debug: Usuario {} creado exitosamente", i+1);
logger.info("LoadDatabase - Inicialización: Se generaron 10 sucursales con 100 productos cada una");
```

### Beneficios del Logging Estandarizado

1. **Trazabilidad Completa**: Seguimiento de todas las operaciones del sistema
2. **Debugging Eficiente**: Identificación rápida de problemas y errores
3. **Monitoreo de Performance**: Seguimiento de tiempos de respuesta y operaciones
4. **Auditoría de Negocio**: Registro de todas las transacciones y cambios de estado
5. **Mantenimiento Simplificado**: Logs consistentes facilitan el soporte técnico

### Configuración de Niveles de Log

- **INFO**: Operaciones de negocio principales y resultados exitosos
- **DEBUG**: Detalles técnicos y información de desarrollo
- **WARN**: Situaciones que requieren atención pero no son errores críticos
- **ERROR**: Errores que afectan la funcionalidad del sistema

## Consideraciones de Seguridad

### Validación de Entrada

- Validación exhaustiva con Bean Validation
- Sanitización de datos de entrada
- Prevención de inyección SQL mediante JPA

### Manejo de Errores

- No exposición de información sensible en errores
- Logging seguro de excepciones
- Respuestas de error estructuradas

### Autenticación y Autorización

- Preparado para integración con Spring Security
- Tokens JWT compatibles
- Roles y permisos configurables

## Escalabilidad y Performance

### Optimizaciones Implementadas

- **Lazy Loading**: Carga diferida de relaciones JPA
- **Connection Pooling**: Pool de conexiones HikariCP
- **Caching**: Preparado para integración con Redis
- **Async Processing**: Operaciones asíncronas donde es apropiado

### Consideraciones de Escalabilidad

- Microservicios independientes y desacoplados
- Base de datos por servicio
- Comunicación HTTP stateless
- Configuración externalizada

## Características Destacadas

### Estandarización Completa

- **Endpoints Predeterminados**: Todos los microservicios usan endpoints simples como predeterminados
- **Convención HATEOAS**: Sufijo "-hateoas" para versiones con enlaces
- **Descripciones Consistentes**: Formato uniforme en toda la documentación
- **Enlaces Cruzados**: HATEOAS que conecta diferentes microservicios

### Integración Avanzada

- **Enlaces Dinámicos**: Generación automática de enlaces HATEOAS
- **Validación Cruzada**: Verificación entre microservicios
- **Gestión de Estados**: Sincronización automática de estados
- **Manejo de Errores**: Respuestas estructuradas y consistentes

### Arquitectura Robusta

- **Desacoplamiento**: Microservicios independientes
- **Escalabilidad**: Preparado para crecimiento horizontal
- **Mantenibilidad**: Código limpio y bien documentado
- **Testabilidad**: Tests unitarios y de integración completos

## Licencia

Este proyecto está bajo la licencia MIT. Ver el archivo LICENSE para más detalles.

**Perfulandia** - Arquitectura de Microservicios para E-commerce de Perfumes
_Desarrollado con Spring Boot, Java 21 y mejores prácticas de microservicios_
