# Perfulandia - Arquitectura de Microservicios

Este proyecto implementa una arquitectura de microservicios para la gestión de una tienda de perfumes, utilizando Java Spring Boot, Spring Data JPA, Spring HATEOAS y OpenAPI (Swagger). Cada microservicio es autónomo, desacoplado y expone su propia API RESTful documentada y navegable.

---

## Tabla de Contenidos

- [Perfulandia - Arquitectura de Microservicios](#perfulandia---arquitectura-de-microservicios)
  - [Tabla de Contenidos](#tabla-de-contenidos)
  - [Microservicios](#microservicios)
    - [1. **msvc-usuario**](#1-msvc-usuario)
    - [2. **msvc-producto**](#2-msvc-producto)
    - [3. **msvc-sucursal**](#3-msvc-sucursal)
    - [4. **msvc-pedido**](#4-msvc-pedido)
    - [5. **msvc-envio**](#5-msvc-envio)
    - [6. **msvc-pago**](#6-msvc-pago)
  - [Estructura de Carpetas](#estructura-de-carpetas)
  - [DTOs y Patrones de Diseño](#dtos-y-patrones-de-diseño)
  - [Manejo de Errores](#manejo-de-errores)
  - [Inicialización de Datos](#inicialización-de-datos)
  - [Seguridad y Buenas Prácticas](#seguridad-y-buenas-prácticas)
  - [Ejecución y Pruebas](#ejecución-y-pruebas)

---

## Microservicios

### 1. **msvc-usuario**

- **Propósito:** Gestión de usuarios (CRUD, pedidos asociados, pagos, etc.).
- **Endpoints principales:**
  - `GET /api/v1/usuarios` — Listar usuarios
  - `GET /api/v1/usuarios/{id}` — Obtener usuario por ID
  - `POST /api/v1/usuarios` — Crear usuario
  - `PUT /api/v1/usuarios/{id}` — Actualizar usuario
  - `DELETE /api/v1/usuarios/{id}` — Eliminar usuario
- **DTOs:** UsuarioCreateDTO, UsuarioUpdateDTO, UsuarioSimpleDTO, UsuarioHateoasDTO, ErrorDTO
- **Swagger UI:** [http://localhost:8001/swagger-ui.html](http://localhost:8001/swagger-ui.html)
- **HATEOAS:** Respuestas enriquecidas con links usando ensamblador `UsuarioDTOModelAssembler`.
- **Integración:** Consume servicios de pedidos y pagos.
- **Manejo de errores:** Excepciones personalizadas y DTO de error documentado.

### 2. **msvc-producto**

- **Propósito:** Gestión de productos (CRUD, consulta de stock, etc.).
- **Endpoints principales:**
  - `GET /api/v1/productos` — Listar productos
  - `GET /api/v1/productos/{id}` — Obtener producto por ID
  - `POST /api/v1/productos` — Crear producto
  - `PUT /api/v1/productos/{id}` — Actualizar producto
  - `DELETE /api/v1/productos/{id}` — Eliminar producto
- **DTOs:** ProductoCreateDTO, ProductoUpdateDTO, ProductoGetDTO, ProductoSimpleDTO, ProductoHateoasDTO, ErrorDTO
- **Swagger UI:** [http://localhost:8003/swagger-ui.html](http://localhost:8003/swagger-ui.html)
- **HATEOAS:** Ensamblador `ProductoDTOModelAssembler`.
- **Integración:** Consultado por pedidos y sucursales.

### 3. **msvc-sucursal**

- **Propósito:** Gestión de sucursales y su inventario.
- **Endpoints principales:**
  - `GET /api/v1/sucursales` — Listar sucursales
  - `GET /api/v1/sucursales/{id}` — Obtener sucursal por ID
  - `POST /api/v1/sucursales` — Crear sucursal
  - `PUT /api/v1/sucursales/{id}` — Actualizar sucursal
  - `DELETE /api/v1/sucursales/{id}` — Eliminar sucursal
- **DTOs:** SucursalDTO, InventarioDTO, ProductoDTO, ErrorDTO
- **Swagger UI:** [http://localhost:8002/swagger-ui.html](http://localhost:8002/swagger-ui.html)
- **HATEOAS:** Ensamblador `SucursalDTOModelAssembler`.
- **Integración:** Gestiona inventarios y consulta productos.

### 4. **msvc-pedido**

- **Propósito:** Gestión de pedidos, integración con usuarios, productos, sucursales, envíos y pagos.
- **Endpoints principales:**
  - `GET /api/v1/pedidos` — Listar pedidos
  - `GET /api/v1/pedidos/{id}` — Obtener pedido por ID
  - `POST /api/v1/pedidos` — Crear pedido
  - `PUT /api/v1/pedidos/{id}` — Actualizar pedido
  - `DELETE /api/v1/pedidos/{id}` — Eliminar pedido
- **DTOs:** PedidoCreateDTO, PedidoUpdateDTO, PedidoGetDTO, PedidoHateoasDTO, DetallePedidoDTO, ErrorDTO, POJOs de integración
- **Swagger UI:** [http://localhost:8004/swagger-ui.html](http://localhost:8004/swagger-ui.html)
- **HATEOAS:** Ensamblador `PedidoDTOModelAssembler`.
- **Integración:** Orquesta operaciones con usuario, producto, sucursal, envío y pago.

### 5. **msvc-envio**

- **Propósito:** Gestión de envíos asociados a pedidos.
- **Endpoints principales:**
  - `GET /api/v1/envios` — Listar envíos
  - `GET /api/v1/envios/{id}` — Obtener envío por ID
  - `POST /api/v1/envios` — Crear envío
  - `PUT /api/v1/envios/{id}` — Actualizar envío
  - `DELETE /api/v1/envios/{id}` — Eliminar envío
- **DTOs:** EnvioCreateDTO, EnvioUpdateDTO, EnvioGetDTO, EnvioHateoasDTO, ErrorDTO, POJOs de integración
- **Swagger UI:** [http://localhost:8005/swagger-ui.html](http://localhost:8005/swagger-ui.html)
- **HATEOAS:** Ensamblador `EnvioDTOModelAssembler`.
- **Integración:** Actualiza estado de pedidos y calcula costos de envío.

### 6. **msvc-pago**

- **Propósito:** Gestión de pagos asociados a pedidos y usuarios.
- **Endpoints principales:**
  - `GET /api/v1/pagos` — Listar pagos
  - `GET /api/v1/pagos/{id}` — Obtener pago por ID
  - `POST /api/v1/pagos` — Crear pago
  - `PUT /api/v1/pagos/{id}` — Actualizar pago
  - `DELETE /api/v1/pagos/{id}` — Eliminar pago
- **DTOs:** PagoCreateDTO, PagoUpdateDTO, PagoGetDTO, PagoHateoasDTO, PagoDTO, ErrorDTO, POJOs de integración
- **Swagger UI:** [http://localhost:8006/swagger-ui.html](http://localhost:8006/swagger-ui.html)
- **HATEOAS:** Ensamblador `PagoDTOModelAssembler`.
- **Integración:** Relaciona pagos con pedidos y usuarios.

---

## Estructura de Carpetas

```
perfulandia/
  ├── msvc-usuario/
  ├── msvc-producto/
  ├── msvc-sucursal/
  ├── msvc-pedido/
  ├── msvc-envio/
  ├── msvc-pago/
  └── README.md
```

Cada microservicio contiene:

- `controllers/` — Controladores REST
- `dtos/` — Objetos de transferencia de datos
- `models/entities/` — Entidades JPA
- `repositories/` — Repositorios Spring Data
- `services/` — Lógica de negocio
- `exceptions/` — Manejo de errores
- `config/` — Configuración Swagger y otros beans
- `resources/` — Propiedades y datos iniciales

---

## DTOs y Patrones de Diseño

- **DTOs:** Todos los datos expuestos por la API se encapsulan en DTOs, documentados con `@Schema` y ejemplos realistas.
- **POJOs de integración:** Permiten la comunicación entre microservicios (por ejemplo, detalles de pedido, usuario, producto, etc.).
- **HATEOAS:** Uso de ensambladores para enriquecer las respuestas con enlaces navegables.
- **Validación:** Anotaciones de validación (`@NotNull`, `@NotBlank`, etc.) en los DTOs.

---

## Manejo de Errores

- **Excepciones personalizadas:** Cada microservicio define sus propias excepciones.
- **Controladores globales de errores:** Capturan y devuelven respuestas uniformes.
- **DTO de error:** Estructura estándar con código, fecha y mapa de errores.
- **Ejemplo de respuesta de error:**

```json
{
  "status": 400,
  "date": "2024-03-20T15:30:00.000Z",
  "errors": {
    "campo": "mensaje de error"
  }
}
```

---

## Inicialización de Datos

- **Carga automática:** Cada microservicio puede cargar datos de ejemplo en modo desarrollo.
- **Archivos:** `src/main/resources/application-dev.properties` y clases `LoadDatabase.java`.

---

## Seguridad y Buenas Prácticas

- **Separación de responsabilidades:** Cada microservicio es autónomo y desacoplado.
- **Validación de datos:** Validaciones exhaustivas en DTOs y servicios.
- **Documentación:** Swagger/OpenAPI detallado y actualizado.
- **Pruebas:** Tests unitarios y de integración con JUnit y Mockito.
- **Manejo de errores:** Respuestas uniformes y documentadas.
- **Estructura profesional:** Código limpio, modular y fácil de mantener.

---

## Ejecución y Pruebas

1. **Clona el repositorio y navega a la raíz del proyecto.**
2. **Arranca cada microservicio:**
   ```bash
   cd msvc-usuario && ./mvnw spring-boot:run
   cd ../msvc-producto && ./mvnw spring-boot:run
   cd ../msvc-sucursal && ./mvnw spring-boot:run
   cd ../msvc-pedido && ./mvnw spring-boot:run
   cd ../msvc-envio && ./mvnw spring-boot:run
   cd ../msvc-pago && ./mvnw spring-boot:run
   ```
3. **Accede a la documentación Swagger UI** en los puertos indicados arriba.
4. **Ejecuta los tests** con:
   ```bash
   ./mvnw test
   ```
