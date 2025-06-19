package com.rubenrf.cafeteria_app;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;

import com.rubenrf.cafeteria_app.dto.cliente.DatosListadoCliente;
import com.rubenrf.cafeteria_app.dto.producto.DatosListadoProducto;
import com.rubenrf.cafeteria_app.service.ClienteService;
import com.rubenrf.cafeteria_app.service.ProductoService;

import io.restassured.RestAssured;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class CafeteriaAppApplicationTests {

	@LocalServerPort
	private Integer port;

	@Autowired
	private ProductoService productoService;

	@Autowired
	private ClienteService clienteService;

	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;

		productoService.eliminarTodosLosProductos();
		clienteService.eliminarTodosLosClientes();
	}

	@Test
	void obtenerProductosVacio() {
				
		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/productos")
				.then()
				.log().all()
				.statusCode(204);

	}

	@Test
	void crearProducto() {
		String crearProductoJson = """
				{
					"nombre": "Café",
					"precio": 500,
					"categoria": "BEBIDA",
					"stock": 100
				}
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(crearProductoJson)
				.when()
				.post("/api/productos")
				.then()
				.log().all()
				.statusCode(201);

	}

	@Test
	void obtenerClientesVacio() {

		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/clientes")
				.then()
				.log().all()
				.statusCode(204);

	}

	@Test
	void crearCliente() {
		String crearClienteJson = """
				{
					"nombre": "Ruben Robles",
					"correo": "ruben@mail.com",
					"telefono": "+573020202022"
				}
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(crearClienteJson)
				.when()
				.post("/api/clientes")
				.then()
				.log().all()
				.statusCode(201);

	}

	@Test
	void obtenerCliente() {
		String crearClienteJson = """
				{
					"nombre": "Ruben Robles",
					"correo": "ruben@mail.com",
					"telefono": "+573020202022"
				}
				""";
		DatosListadoCliente cliente = RestAssured.given()
				.contentType("application/json")
				.body(crearClienteJson)
				.when()
				.post("/api/clientes")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoCliente.class);

		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/clientes/" + cliente.id())
				.then()
				.log().all()
				.statusCode(200);

	}

	@Test
	void obtenerProducto() {
		String crearProductoJson = """
				{
					"nombre": "Café",
					"precio": 500,
					"categoria": "BEBIDA",
					"stock": 100
				}
				""";
		DatosListadoProducto producto = RestAssured.given()
				.contentType("application/json")
				.body(crearProductoJson)
				.when()
				.post("/api/productos")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoProducto.class);

		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/productos/" + producto.id())
				.then()
				.log().all()
				.statusCode(200);
	}

	

	@Test
	void obtenerClientes() {
		String crearClienteJson = """
				{
					"nombre": "Ruben Robles",
					"correo": "ruben@mail.com",
					"telefono": "+573020202022"
				}
				""";

		String crearClienteJson2 = """
				{
					"nombre": "Diego Montes",
					"correo": "diego@mail.com",
					"telefono": "+573112001122"
				}
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(crearClienteJson)
				.when()
				.post("/api/clientes")
				.then()
				.log().all()
				.statusCode(201);

		RestAssured.given()
				.contentType("application/json")
				.body(crearClienteJson2)
				.when()
				.post("/api/clientes")
				.then()
				.log().all()
				.statusCode(201);

		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/clientes")
				.then()
				.log().all()
				.statusCode(200);

	}

	

	

	@Test
	void obtenerProductos() {
		String crearProductoJson = """
				{
					"nombre": "Café",
					"precio": 500,
					"categoria": "BEBIDA",
					"stock": 100
				}
				""";

		String crearProductoJson2 = """
				{
					"nombre": "Coca Cola 400ml",
					"precio": 2500,
					"categoria": "BEBIDA",
					"stock": 100
				}
				""";
		RestAssured.given()
				.contentType("application/json")
				.body(crearProductoJson)
				.when()
				.post("/api/productos")
				.then()
				.log().all()
				.statusCode(201);

		RestAssured.given()
				.contentType("application/json")
				.body(crearProductoJson2)
				.when()
				.post("/api/productos")
				.then()
				.log().all()
				.statusCode(201);

		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/productos")
				.then()
				.log().all()
				.statusCode(200);
	}

	

	@Test
	void actualizarCliente() {
		String crearClienteJson = """
				{
					"nombre": "Ruben Robles",
					"correo": "ruben@mail.com",
					"telefono": "+573020202022"
				}
				""";

		DatosListadoCliente cliente = RestAssured.given()
				.contentType("application/json")
				.body(crearClienteJson)
				.when()
				.post("/api/clientes")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoCliente.class);

		String actualizarClienteJson = """
				{
					"nombre": "Ruben Fontalvo",
					"correo": "ruben2@mail.com",
					"telefono": "+573124400022"
				}
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(actualizarClienteJson)
				.when()
				.put("/api/clientes/" + cliente.id())
				.then()
				.log().all()
				.statusCode(200);

	}

	@Test
	void actualizarProducto() {
		String crearProductoJson = """
				{
					"nombre": "Café",
					"precio": 500,
					"categoria": "BEBIDA",
					"stock": 100
				}
				""";

		DatosListadoProducto producto = RestAssured.given()
				.contentType("application/json")
				.body(crearProductoJson)
				.when()
				.post("/api/productos")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoProducto.class);

		String actualizarProductoJson = """
				{
					"nombre": "Café Sello Rojo",
					"precio": 1500,
					"categoria": "BEBIDA",
					"stock": 1000
				}
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(actualizarProductoJson)
				.when()
				.put("/api/productos/" + producto.id())
				.then()
				.log().all()
				.statusCode(200);

	}

	@Test
	void eliminarCliente() {
		String crearClienteJson = """
				{
					"nombre": "Ruben Robles",
					"correo": "ruben@mail.com",
					"telefono": "+573020202022"
				}
				""";

		DatosListadoCliente cliente = RestAssured.given()
				.contentType("application/json")
				.body(crearClienteJson)
				.when()
				.post("/api/clientes")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoCliente.class);

		RestAssured.given()
				.contentType("application/json")
				.when()
				.delete("/api/clientes/" + cliente.id())
				.then()
				.log().all()
				.statusCode(204);

	}

	@Test
	void eliminarProducto() {
		String crearProductoJson = """
				{
					"nombre": "Café",
					"precio": 500,
					"categoria": "BEBIDA",
					"stock": 100
				}
				""";

		DatosListadoProducto producto = RestAssured.given()
				.contentType("application/json")
				.body(crearProductoJson)
				.when()
				.post("/api/productos")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoProducto.class);

		RestAssured.given()
				.contentType("application/json")
				.when()
				.delete("/api/productos/" + producto.id())
				.then()
				.log().all()
				.statusCode(204);

	}

	@Test
	void crearPedido() {
		// Primero creamos un cliente
		String crearClienteJson = """
				{
					"nombre": "Ruben Robles",
					"correo": "ruben@mail.com",
					"telefono": "+573020202022"
				}
				""";
		DatosListadoCliente cliente = RestAssured.given()
				.contentType("application/json")
				.body(crearClienteJson)
				.when()
				.post("/api/clientes")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoCliente.class);

		// Luego creamos un producto
		String crearProductoJson = """
				{
					"nombre": "Café",
					"precio": 500,
					"categoria": "BEBIDA",
					"stock": 100
				}
				""";
		DatosListadoProducto producto = RestAssured.given()
				.contentType("application/json")
				.body(crearProductoJson)
				.when()
				.post("/api/productos")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoProducto.class);

		// Finalmente creamos el pedido
		String crearPedidoJson = """
				{
					"idCliente": %d,
					"datosCrearDetallesPedido": [
						{
							"idProducto": %d,
							"cantidad": 2
						}
					]
				}
				""".formatted(cliente.id(), producto.id());

		RestAssured.given()
				.contentType("application/json")
				.body(crearPedidoJson)
				.when()
				.post("/api/pedidos")
				.then()
				.log().all()
				.statusCode(201);
	}

	@Test
	void entregarPedido() {
		// Primero creamos un cliente
		String crearClienteJson = """
				{
					"nombre": "Ruben Robles",
					"correo": "ruben@mail.com",
					"telefono": "+573020202022"
				}
				""";
		DatosListadoCliente cliente = RestAssured.given()
				.contentType("application/json")
				.body(crearClienteJson)
				.when()
				.post("/api/clientes")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoCliente.class);

		// Luego creamos un producto
		String crearProductoJson = """
				{
					"nombre": "Café",
					"precio": 500,
					"categoria": "BEBIDA",
					"stock": 100
				}
				""";
		DatosListadoProducto producto = RestAssured.given()
				.contentType("application/json")
				.body(crearProductoJson)
				.when()
				.post("/api/productos")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoProducto.class);

		// Creamos el pedido
		String crearPedidoJson = """
				{
					"idCliente": %d,
					"datosCrearDetallesPedido": [
						{
							"idProducto": %d,
							"cantidad": 2
						}
					]
				}
				""".formatted(cliente.id(), producto.id());

		Integer pedidoId = RestAssured.given()
				.contentType("application/json")
				.body(crearPedidoJson)
				.when()
				.post("/api/pedidos")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.path("id");

		// Entregamos el pedido
		RestAssured.given()
				.contentType("application/json")
				.when()
				.put("/api/pedidos/" + pedidoId + "/entregar-pedido")
				.then()
				.log().all()
				.statusCode(200);
	}

	@Test
	void cancelarPedido() {
		// Primero creamos un cliente
		String crearClienteJson = """
				{
					"nombre": "Ruben Robles",
					"correo": "ruben@mail.com",
					"telefono": "+573020202022"
				}
				""";
		DatosListadoCliente cliente = RestAssured.given()
				.contentType("application/json")
				.body(crearClienteJson)
				.when()
				.post("/api/clientes")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoCliente.class);

		// Luego creamos un producto
		String crearProductoJson = """
				{
					"nombre": "Café",
					"precio": 500,
					"categoria": "BEBIDA",
					"stock": 100
				}
				""";
		DatosListadoProducto producto = RestAssured.given()
				.contentType("application/json")
				.body(crearProductoJson)
				.when()
				.post("/api/productos")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoProducto.class);

		// Creamos el pedido
		String crearPedidoJson = """
				{
					"idCliente": %d,
					"datosCrearDetallesPedido": [
						{
							"idProducto": %d,
							"cantidad": 2
						}
					]
				}
				""".formatted(cliente.id(), producto.id());

		Integer pedidoId = RestAssured.given()
				.contentType("application/json")
				.body(crearPedidoJson)
				.when()
				.post("/api/pedidos")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.path("id");

		// Cancelamos el pedido
		RestAssured.given()
				.contentType("application/json")
				.when()
				.put("/api/pedidos/" + pedidoId + "/cancelar-pedido")
				.then()
				.log().all()
				.statusCode(200);
	}

	@Test
	void crearPedidoConProductoInexistente() {
		// Primero creamos un cliente
		String crearClienteJson = """
				{
					"nombre": "Ruben Robles",
					"correo": "ruben@mail.com",
					"telefono": "+573020202022"
				}
				""";
		DatosListadoCliente cliente = RestAssured.given()
				.contentType("application/json")
				.body(crearClienteJson)
				.when()
				.post("/api/clientes")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoCliente.class);

		// Intentamos crear un pedido con un producto que no existe
		String crearPedidoJson = """
				{
					"idCliente": %d,
					"datosCrearDetallesPedido": [
						{
							"idProducto": 999,
							"cantidad": 2
						}
					]
				}
				""".formatted(cliente.id());

		RestAssured.given()
				.contentType("application/json")
				.body(crearPedidoJson)
				.when()
				.post("/api/pedidos")
				.then()
				.log().all()
				.statusCode(404);
	}

	@Test
	void crearPedidoConClienteInexistente() {
		// Intentamos crear un pedido con un cliente que no existe
		String crearPedidoJson = """
				{
					"idCliente": 999,
					"datosCrearDetallesPedido": [
						{
							"idProducto": 1,
							"cantidad": 2
						}
					]
				}
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(crearPedidoJson)
				.when()
				.post("/api/pedidos")
				.then()
				.log().all()
				.statusCode(404);
	}

	@Test
	void crearPedidoConStockInsuficiente() {
		// Primero creamos un cliente
		String crearClienteJson = """
				{
					"nombre": "Ruben Robles",
					"correo": "ruben@mail.com",
					"telefono": "+573020202022"
				}
				""";
		DatosListadoCliente cliente = RestAssured.given()
				.contentType("application/json")
				.body(crearClienteJson)
				.when()
				.post("/api/clientes")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoCliente.class);

		// Luego creamos un producto con stock limitado
		String crearProductoJson = """
				{
					"nombre": "Café",
					"precio": 500,
					"categoria": "BEBIDA",
					"stock": 1
				}
				""";
		DatosListadoProducto producto = RestAssured.given()
				.contentType("application/json")
				.body(crearProductoJson)
				.when()
				.post("/api/productos")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoProducto.class);

		// Intentamos crear un pedido con cantidad mayor al stock
		String crearPedidoJson = """
				{
					"idCliente": %d,
					"datosCrearDetallesPedido": [
						{
							"idProducto": %d,
							"cantidad": 2
						}
					]
				}
				""".formatted(cliente.id(), producto.id());

		RestAssured.given()
				.contentType("application/json")
				.body(crearPedidoJson)
				.when()
				.post("/api/pedidos")
				.then()
				.log().all()
				.statusCode(400);
	}

	@Test
	void obtenerPedidoPorId() {
		// Primero creamos un cliente
		String crearClienteJson = """
				{
					"nombre": "Ruben Robles",
					"correo": "ruben@mail.com",
					"telefono": "+573020202022"
				}
				""";
		DatosListadoCliente cliente = RestAssured.given()
				.contentType("application/json")
				.body(crearClienteJson)
				.when()
				.post("/api/clientes")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoCliente.class);

		// Luego creamos un producto
		String crearProductoJson = """
				{
					"nombre": "Café",
					"precio": 500,
					"categoria": "BEBIDA",
					"stock": 100
				}
				""";
		DatosListadoProducto producto = RestAssured.given()
				.contentType("application/json")
				.body(crearProductoJson)
				.when()
				.post("/api/productos")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoProducto.class);

		// Creamos el pedido
		String crearPedidoJson = """
				{
					"idCliente": %d,
					"datosCrearDetallesPedido": [
						{
							"idProducto": %d,
							"cantidad": 2
						}
					]
				}
				""".formatted(cliente.id(), producto.id());

		Integer pedidoId = RestAssured.given()
				.contentType("application/json")
				.body(crearPedidoJson)
				.when()
				.post("/api/pedidos")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.path("id");

		// Obtenemos el pedido por ID
		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/pedidos/" + pedidoId)
				.then()
				.log().all()
				.statusCode(200);
	}

	@Test
	void obtenerPedidoInexistente() {
		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/pedidos/999")
				.then()
				.log().all()
				.statusCode(404);
	}


	@Test
	void cancelarPedidoInexistente() {
		RestAssured.given()
				.contentType("application/json")
				.when()
				.put("/api/pedidos/999/cancelar-pedido")
				.then()
				.log().all()
				.statusCode(404);
	}

	@Test
	void entregarPedidoInexistente() {
		RestAssured.given()
				.contentType("application/json")
				.when()
				.put("/api/pedidos/999/entregar-pedido")
				.then()
				.log().all()
				.statusCode(404);
	}

	@Test
	void actualizarProductoInexistente() {
		String actualizarProductoJson = """
				{
					"nombre": "Café Sello Rojo",
					"precio": 1500,
					"categoria": "BEBIDA",
					"stock": 1000
				}
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(actualizarProductoJson)
				.when()
				.put("/api/productos/999")
				.then()
				.log().all()
				.statusCode(404);
	}

	@Test
	void actualizarClienteInexistente() {
		String actualizarClienteJson = """
				{
					"nombre": "Ruben Fontalvo",
					"correo": "ruben2@mail.com",
					"telefono": "+573124400022"
				}
				""";

		RestAssured.given()
				.contentType("application/json")
				.body(actualizarClienteJson)
				.when()
				.put("/api/clientes/999")
				.then()
				.log().all()
				.statusCode(404);
	}

	@Test
	void eliminarProductoInexistente() {
		RestAssured.given()
				.contentType("application/json")
				.when()
				.delete("/api/productos/999")
				.then()
				.log().all()
				.statusCode(404);
	}

	@Test
	void eliminarClienteInexistente() {
		RestAssured.given()
				.contentType("application/json")
				.when()
				.delete("/api/clientes/999")
				.then()
				.log().all()
				.statusCode(404);
	}

	@Test
	void obtenerProductoInexistente() {
		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/productos/999")
				.then()
				.log().all()
				.statusCode(404);
	}

	@Test
	void obtenerClienteInexistente() {
		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/clientes/999")
				.then()
				.log().all()
				.statusCode(404);
	}

	@Test
	void obtenerTotalPedidos() {
		// Primero creamos un cliente
		String crearClienteJson = """
				{
					"nombre": "Ruben Robles",
					"correo": "ruben@mail.com",
					"telefono": "+573020202022"
				}
				""";
		DatosListadoCliente cliente = RestAssured.given()
				.contentType("application/json")
				.body(crearClienteJson)
				.when()
				.post("/api/clientes")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoCliente.class);

		// Luego creamos un producto
		String crearProductoJson = """
				{
					"nombre": "Café",
					"precio": 500,
					"categoria": "BEBIDA",
					"stock": 100
				}
				""";
		DatosListadoProducto producto = RestAssured.given()
				.contentType("application/json")
				.body(crearProductoJson)
				.when()
				.post("/api/productos")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoProducto.class);

		// Creamos tres pedidos para el cliente
		String crearPedidoJson = """
				{
					"idCliente": %d,
					"datosCrearDetallesPedido": [
						{
							"idProducto": %d,
							"cantidad": 2
						}
					]
				}
				""".formatted(cliente.id(), producto.id());

		// Primer pedido
		RestAssured.given()
				.contentType("application/json")
				.body(crearPedidoJson)
				.when()
				.post("/api/pedidos")
				.then()
				.log().all()
				.statusCode(201);

		// Segundo pedido
		RestAssured.given()
				.contentType("application/json")
				.body(crearPedidoJson)
				.when()
				.post("/api/pedidos")
				.then()
				.log().all()
				.statusCode(201);

		// Tercer pedido
		RestAssured.given()
				.contentType("application/json")
				.body(crearPedidoJson)
				.when()
				.post("/api/pedidos")
				.then()
				.log().all()
				.statusCode(201);

		// Obtenemos el total de pedidos del cliente
		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/clientes/" + cliente.id() + "/total-pedidos")
				.then()
				.log().all()
				.statusCode(200)
				.body("totalPedidos", equalTo(3000.0F));
	}

	@Test
	void obtenerTotalPedidosClienteInexistente() {
		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/clientes/999/total-pedidos")
				.then()
				.log().all()
				.statusCode(404);
	}

	@Test
	void obtenerTotalPedidosClienteSinPedidos() {
		// Primero creamos un cliente
		String crearClienteJson = """
				{
					"nombre": "Ruben Robles",
					"correo": "ruben@mail.com",
					"telefono": "+573020202022"
				}
				""";
		DatosListadoCliente cliente = RestAssured.given()
				.contentType("application/json")
				.body(crearClienteJson)
				.when()
				.post("/api/clientes")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoCliente.class);

		// Obtenemos el total de pedidos del cliente (debería ser 0)
		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/clientes/" + cliente.id() + "/total-pedidos")
				.then()
				.log().all()
				.statusCode(200)
				.body("totalPedidos", equalTo(0.0F));
	}

	@Test
	void obtenerProductosMasVendidos() {
		// Primero creamos un cliente
		String crearClienteJson = """
				{
					"nombre": "Ruben Robles",
					"correo": "ruben@mail.com",
					"telefono": "+573020202022"
				}
				""";
		DatosListadoCliente cliente = RestAssured.given()
				.contentType("application/json")
				.body(crearClienteJson)
				.when()
				.post("/api/clientes")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoCliente.class);

		// Creamos tres productos diferentes
		String crearProducto1Json = """
				{
					"nombre": "Café",
					"precio": 500,
					"categoria": "BEBIDA",
					"stock": 100
				}
				""";
		DatosListadoProducto producto1 = RestAssured.given()
				.contentType("application/json")
				.body(crearProducto1Json)
				.when()
				.post("/api/productos")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoProducto.class);

		String crearProducto2Json = """
				{
					"nombre": "Coca Cola",
					"precio": 2500,
					"categoria": "BEBIDA",
					"stock": 100
				}
				""";
		DatosListadoProducto producto2 = RestAssured.given()
				.contentType("application/json")
				.body(crearProducto2Json)
				.when()
				.post("/api/productos")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoProducto.class);

		String crearProducto3Json = """
				{
					"nombre": "Hamburguesa",
					"precio": 15000,
					"categoria": "COMIDA",
					"stock": 100
				}
				""";
		DatosListadoProducto producto3 = RestAssured.given()
				.contentType("application/json")
				.body(crearProducto3Json)
				.when()
				.post("/api/productos")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().as(DatosListadoProducto.class);

		// Creamos pedidos con diferentes cantidades para cada producto
		// Producto 1: 5 unidades
		String crearPedido1Json = """
				{
					"idCliente": %d,
					"datosCrearDetallesPedido": [
						{
							"idProducto": %d,
							"cantidad": 5
						}
					]
				}
				""".formatted(cliente.id(), producto1.id());

		RestAssured.given()
				.contentType("application/json")
				.body(crearPedido1Json)
				.when()
				.post("/api/pedidos")
				.then()
				.log().all()
				.statusCode(201);

		// Producto 2: 3 unidades
		String crearPedido2Json = """
				{
					"idCliente": %d,
					"datosCrearDetallesPedido": [
						{
							"idProducto": %d,
							"cantidad": 3
						}
					]
				}
				""".formatted(cliente.id(), producto2.id());

		RestAssured.given()
				.contentType("application/json")
				.body(crearPedido2Json)
				.when()
				.post("/api/pedidos")
				.then()
				.log().all()
				.statusCode(201);

		// Producto 3: 1 unidad
		String crearPedido3Json = """
				{
					"idCliente": %d,
					"datosCrearDetallesPedido": [
						{
							"idProducto": %d,
							"cantidad": 1
						}
					]
				}
				""".formatted(cliente.id(), producto3.id());

		RestAssured.given()
				.contentType("application/json")
				.body(crearPedido3Json)
				.when()
				.post("/api/pedidos")
				.then()
				.log().all()
				.statusCode(201);

		// Obtenemos los productos más vendidos
		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/productos/mas-vendidos")
				.then()
				.log().all()
				.statusCode(200)
				.body("[0].id", equalTo(producto1.id().intValue()))
				.body("[0].unidadesVendidas", equalTo(5))
				.body("[1].id", equalTo(producto2.id().intValue()))
				.body("[1].unidadesVendidas", equalTo(3))
				.body("[2].id", equalTo(producto3.id().intValue()))
				.body("[2].unidadesVendidas", equalTo(1));
	}

	@Test
	void obtenerProductosMasVendidosSinVentas() {
		// Creamos un producto sin ventas
		String crearProductoJson = """
				{
					"nombre": "Café",
					"precio": 500,
					"categoria": "BEBIDA",
					"stock": 100
				}
				""";
		RestAssured.given()
				.contentType("application/json")
				.body(crearProductoJson)
				.when()
				.post("/api/productos")
				.then()
				.log().all()
				.statusCode(201);

		// Obtenemos los productos más vendidos
		RestAssured.given()
				.contentType("application/json")
				.when()
				.get("/api/productos/mas-vendidos")
				.then()
				.log().all()
				.statusCode(200);
	}
}
