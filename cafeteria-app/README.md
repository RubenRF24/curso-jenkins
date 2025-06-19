# API de Cafetería

API REST para la gestión de una cafetería, desarrollada con Spring Boot.

## Descripción

Esta API permite gestionar productos, clientes y pedidos de una cafetería. Incluye funcionalidades para:

- Gestión de productos (CRUD)
- Gestión de clientes (CRUD)
- Gestión de pedidos
- Estadísticas de ventas
- Control de inventario

## Tecnologías Utilizadas

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Maven
- Swagger/OpenAPI
- JUnit 5
- RestAssured

## Endpoints

### Productos (`/api/productos`)

#### Crear Producto

- **POST** `/api/productos`
- **Body**:

```json
{
    "nombre": "string",
    "precio": number,
    "categoria": "BEBIDA|COMIDA|POSTRE",
    "stock": number
}
```

- **Respuesta**: 201 Created
- **Errores**: 400 Bad Request (validación), 500 Internal Server Error

#### Obtener Producto

- **GET** `/api/productos/{id}`
- **Respuesta**: 200 OK
- **Errores**: 404 Not Found

#### Listar Productos

- **GET** `/api/productos`
- **Parámetros**: page, size, sort
- **Respuesta**: 200 OK o 204 No Content
- **Errores**: 400 Bad Request

#### Actualizar Producto

- **PUT** `/api/productos/{id}`
- **Body**: Mismo formato que crear
- **Respuesta**: 200 OK
- **Errores**: 404 Not Found, 400 Bad Request

#### Eliminar Producto

- **DELETE** `/api/productos/{id}`
- **Respuesta**: 204 No Content
- **Errores**: 404 Not Found

#### Productos Más Vendidos

- **GET** `/api/productos/mas-vendidos`
- **Respuesta**: 200 OK
- **Formato**:

```json
[
    {
        "id": number,
        "nombre": "string",
        "unidadesVendidas": number
    }
]
```

### Clientes (`/api/clientes`)

#### Crear Cliente

- **POST** `/api/clientes`
- **Body**:

```json
{
	"nombre": "string",
	"correo": "string (email)",
	"telefono": "string"
}
```

- **Respuesta**: 201 Created
- **Errores**: 400 Bad Request (validación)

#### Obtener Cliente

- **GET** `/api/clientes/{id}`
- **Respuesta**: 200 OK
- **Errores**: 404 Not Found

#### Listar Clientes

- **GET** `/api/clientes`
- **Parámetros**: page, size, sort
- **Respuesta**: 200 OK o 204 No Content

#### Actualizar Cliente

- **PUT** `/api/clientes/{id}`
- **Body**:

```json
{
	"nombre": "string",
	"correo": "string (email)",
	"telefono": "string"
}
```

- **Respuesta**: 200 OK
- **Errores**: 404 Not Found, 400 Bad Request

#### Eliminar Cliente

- **DELETE** `/api/clientes/{id}`
- **Respuesta**: 204 No Content
- **Errores**: 404 Not Found

#### Total de Pedidos por Cliente

- **GET** `/api/clientes/{id}/total-pedidos`
- **Respuesta**: 200 OK
- **Formato**:

```json
{
    "id": number,
    "nombre": "string",
    "correo": "string",
    "telefono": "string",
    "totalPedidos": number
}
```

### Pedidos (`/api/pedidos`)

#### Crear Pedido

- **POST** `/api/pedidos`
- **Body**:

```json
{
    "idCliente": number,
    "datosCrearDetallesPedido": [
        {
            "idProducto": number,
            "cantidad": number
        }
    ]
}
```

- **Respuesta**: 201 Created
- **Errores**: 400 Bad Request, 404 Not Found

#### Entregar Pedido

- **PUT** `/api/pedidos/{id}/entregar-pedido`
- **Respuesta**: 200 OK
- **Errores**: 404 Not Found

#### Cancelar Pedido

- **PUT** `/api/pedidos/{id}/cancelar-pedido`
- **Respuesta**: 200 OK
- **Errores**: 404 Not Found

#### Obtener Pedido

- **GET** `/api/pedidos/{id}`
- **Respuesta**: 200 OK
- **Errores**: 404 Not Found

## Estados de Pedidos

- **PENDIENTE**: Pedido creado pero no procesado
- **ENTREGADO**: Pedido completado y entregado
- **CANCELADO**: Pedido cancelado

## Manejo de Errores

La API utiliza un manejador global de excepciones que devuelve respuestas consistentes:

```json
{
	"mensaje": "string"
}
```

Códigos de error comunes:

- 400 Bad Request: Datos inválidos
- 404 Not Found: Recurso no encontrado
- 500 Internal Server Error: Error del servidor

## Validaciones

- Productos:

  - Nombre: No puede estar vacío
  - Precio: Debe ser mayor a 0
  - Stock: Debe ser mayor o igual a 0
  - Categoría: Debe ser válida (BEBIDA, COMIDA, POSTRE)

- Clientes:
  - Nombre: No puede estar vacío
  - Correo: Debe ser un email válido
  - Teléfono: No puede estar vacío

## Documentación API

La documentación de la API está disponible a través de Swagger UI en:

```
http://localhost:8080/swagger-ui.html
```

## Pruebas

El proyecto incluye pruebas unitarias y de integración utilizando:

- JUnit 5
- RestAssured
- TestContainers

Para ejecutar las pruebas:

```bash
mvn test
```

## Configuración

El proyecto utiliza las siguientes configuraciones por defecto:

- Puerto: 8080
- Base de datos: MySQL
- CORS: Habilitado para todos los orígenes
