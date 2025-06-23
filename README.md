# Perfulandia - Arquitectura de Microservicios

Este proyecto implementa una arquitectura de microservicios para la gestión de una tienda de perfumes, utilizando Java Spring Boot, Spring Data JPA, Spring HATEOAS y documentación OpenAPI (Swagger). Cada microservicio es autónomo, desacoplado y expone su propia API RESTful documentada y navegable.

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
- **Tecnologías:** Spring Boot, Spring Data JPA, H2, Spring HATEOAS, Swagger/OpenAPI
- **Swagger UI:** [http://localhost:8001/swagger-ui.html](http://localhost:8001/swagger-ui.html)
- **HATEOAS:** Respuestas enriquecidas con links usando ensamblador `UsuarioDTOModelAssembler`.

### 2. **msvc-producto**

- **Propósito:** Gestión de productos (CRUD, consulta de stock, etc.).
- **Endpoints principales:**
  - `GET /api/v1/productos` — Listar productos
  - `GET /api/v1/productos/{id}` — Obtener producto por ID
  - `POST /api/v1/productos` — Crear producto
  - `PUT /api/v1/productos/{id}` — Actualizar producto
  - `DELETE /api/v1/productos/{id}` — Eliminar producto
- **Swagger UI:** [http://localhost:8003/swagger-ui.html](http://localhost:8003/swagger-ui.html)
- **HATEOAS:** Ensamblador `ProductoDTOModelAssembler`.

### 3. **msvc-sucursal**

- **Propósito:** Gestión de sucursales y su inventario.
- **Endpoints principales:**
  - `GET /api/v1/sucursales` — Listar sucursales
  - `GET /api/v1/sucursales/{id}` — Obtener sucursal por ID
  - `POST /api/v1/sucursales` — Crear sucursal
  - `PUT /api/v1/sucursales/{id}` — Actualizar sucursal
  - `DELETE /api/v1/sucursales/{id}` — Eliminar sucursal
- **Swagger UI:** [http://localhost:8002/swagger-ui.html](http://localhost:8002/swagger-ui.html)
- **HATEOAS:** Ensamblador `SucursalDTOModelAssembler`.

### 4. **msvc-pedido**

- **Propósito:** Gestión de pedidos, integración con usuarios, productos, sucursales y pagos.
- **Endpoints principales:**
  - `GET /api/v1/pedidos` — Listar pedidos
  - `GET /api/v1/pedidos/{id}` — Obtener pedido por ID
  - `POST /api/v1/pedidos` — Crear pedido
  - `PUT /api/v1/pedidos/{id}` — Actualizar pedido
  - `DELETE /api/v1/pedidos/{id}` — Eliminar pedido
- **Swagger UI:** [http://localhost:8004/swagger-ui.html](http://localhost:8004/swagger-ui.html)
- **HATEOAS:** Ensamblador `PedidoDTOModelAssembler`.

### 5. **msvc-envio**

- **Propósito:** Gestión de envíos asociados a pedidos.
- **Endpoints principales:**
  - `GET /api/v1/envios` — Listar envíos
  - `GET /api/v1/envios/{id}` — Obtener envío por ID
  - `POST /api/v1/envios` — Crear envío
  - `PUT /api/v1/envios/{id}` — Actualizar envío
  - `DELETE /api/v1/envios/{id}` — Eliminar envío
- **Swagger UI:** [http://localhost:8005/swagger-ui.html](http://localhost:8005/swagger-ui.html)
- **HATEOAS:** Ensamblador `EnvioDTOModelAssembler`.

### 6. **msvc-pago**

- **Propósito:** Gestión de pagos asociados a pedidos y usuarios.
- **Endpoints principales:**
  - `GET /api/v1/pagos` — Listar pagos
  - `GET /api/v1/pagos/{id}` — Obtener pago por ID
  - `POST /api/v1/pagos` — Crear pago
  - `PUT /api/v1/pagos/{id}` — Actualizar pago
  - `DELETE /api/v1/pagos/{id}` — Eliminar pago
- **Swagger UI:** [http://localhost:8006/swagger-ui.html](http://localhost:8006/swagger-ui.html)
- **HATEOAS:** Ensamblador `PagoDTOModelAssembler`.

---

## Tecnologías y Frameworks

- **Spring Boot** (REST, Data JPA, Validation, HATEOAS)
- **Swagger/OpenAPI** (springdoc-openapi-starter-webmvc-ui)
- **H2 Database** (modo dev/test)
- **Mockito/JUnit 5** (tests unitarios y de integración)
- **Spring HATEOAS** (navegabilidad y enlaces en las respuestas)

---

## Documentación y Navegabilidad

- **OpenAPI/Swagger:** Todos los endpoints están documentados con descripciones, parámetros, respuestas y ejemplos.
- **HATEOAS:** Las respuestas de los endpoints principales incluyen enlaces (`_links`) para facilitar la navegación entre recursos.
- **Configuración personalizada:** Cada microservicio tiene su propia configuración de Swagger con título, descripción, contacto y summary.

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

---

## Arquitectura y buenas prácticas

- **Separación de responsabilidades:** Cada microservicio es autónomo y desacoplado.
- **DTOs y ensambladores:** Uso de DTOs para exponer datos y ensambladores HATEOAS para enriquecer las respuestas.
- **Manejo de errores:** Excepciones personalizadas y controladores globales de errores.
- **Inicialización de datos:** Carga de datos de ejemplo en modo desarrollo.

---

## Contacto

- Equipo Perfulandia — soporte@perfulandia.com
