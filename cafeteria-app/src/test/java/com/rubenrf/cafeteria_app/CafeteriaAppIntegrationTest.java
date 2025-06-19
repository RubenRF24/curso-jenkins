package com.rubenrf.cafeteria_app;

import static org.hamcrest.Matchers.equalTo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Import;

import com.rubenrf.cafeteria_app.dto.cliente.DatosListadoCliente;
import com.rubenrf.cafeteria_app.dto.pedido.DatosListadoPedido;
import com.rubenrf.cafeteria_app.dto.producto.DatosListadoProducto;

import io.restassured.RestAssured;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
public class CafeteriaAppIntegrationTest {

        @LocalServerPort
        private Integer port;

        @BeforeEach
        void setup() {
                RestAssured.baseURI = "http://localhost";
                RestAssured.port = port;
        }

        @Test
        void testFlujoCompletoCafeteria() {
                // 1. Crear un cliente
                String crearClienteJson = """
                                {
                                    "nombre": "Juan Pérez",
                                    "correo": "juan@mail.com",
                                    "telefono": "+573001234567"
                                }
                                """;
                DatosListadoCliente cliente = RestAssured.given()
                                .contentType("application/json")
                                .body(crearClienteJson)
                                .when()
                                .post("/api/clientes")
                                .then()
                                .statusCode(201)
                                .extract()
                                .body().as(DatosListadoCliente.class);

                // 2. Crear productos
                String crearProducto1Json = """
                                {
                                    "nombre": "Café Americano",
                                    "precio": 2500,
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
                                .statusCode(201)
                                .extract()
                                .body().as(DatosListadoProducto.class);

                String crearProducto2Json = """
                                {
                                    "nombre": "Croissant",
                                    "precio": 3500,
                                    "categoria": "POSTRE",
                                    "stock": 50
                                }
                                """;
                DatosListadoProducto producto2 = RestAssured.given()
                                .contentType("application/json")
                                .body(crearProducto2Json)
                                .when()
                                .post("/api/productos")
                                .then()
                                .statusCode(201)
                                .extract()
                                .body().as(DatosListadoProducto.class);

                // 3. Crear un pedido
                String crearPedidoJson = """
                                {
                                    "idCliente": %d,
                                    "datosCrearDetallesPedido": [
                                        {
                                            "idProducto": %d,
                                            "cantidad": 2
                                        },
                                        {
                                            "idProducto": %d,
                                            "cantidad": 1
                                        }
                                    ]
                                }
                                """.formatted(cliente.id(), producto1.id(), producto2.id());

                DatosListadoPedido pedido = RestAssured.given()
                                .contentType("application/json")
                                .body(crearPedidoJson)
                                .when()
                                .post("/api/pedidos")
                                .then()
                                .statusCode(201)
                                .extract()
                                .body().as(DatosListadoPedido.class);

                // 4. Verificar que el pedido se creó correctamente
                RestAssured.given()
                                .contentType("application/json")
                                .when()
                                .get("/api/pedidos/" + pedido.id())
                                .then()
                                .statusCode(200)
                                .body("id", equalTo(pedido.id().intValue()))
                                .body("estado", equalTo("PENDIENTE"));

                // 5. Entregar el pedido
                RestAssured.given()
                                .contentType("application/json")
                                .when()
                                .put("/api/pedidos/" + pedido.id() + "/entregar-pedido")
                                .then()
                                .statusCode(200);

                // 6. Verificar que el pedido fue entregado
                RestAssured.given()
                                .contentType("application/json")
                                .when()
                                .get("/api/pedidos/" + pedido.id())
                                .then()
                                .statusCode(200)
                                .body("id", equalTo(pedido.id().intValue()))
                                .body("estado", equalTo("ENTREGADO"));

                // 7. Verificar que el stock se actualizó
                RestAssured.given()
                                .contentType("application/json")
                                .when()
                                .get("/api/productos/" + producto1.id())
                                .then()
                                .statusCode(200)
                                .body("stock", equalTo(98)); // 100 - 2

                RestAssured.given()
                                .contentType("application/json")
                                .when()
                                .get("/api/productos/" + producto2.id())
                                .then()
                                .statusCode(200)
                                .body("stock", equalTo(49)); // 50 - 1
        }

        @Test
        void testFlujoCancelacionPedido() {
                // 1. Crear un cliente
                String crearClienteJson = """
                                {
                                    "nombre": "María López",
                                    "correo": "maria@mail.com",
                                    "telefono": "+573009876543"
                                }
                                """;
                DatosListadoCliente cliente = RestAssured.given()
                                .contentType("application/json")
                                .body(crearClienteJson)
                                .when()
                                .post("/api/clientes")
                                .then()
                                .statusCode(201)
                                .extract()
                                .body().as(DatosListadoCliente.class);

                // 2. Crear un producto
                String crearProductoJson = """
                                {
                                    "nombre": "Té Verde",
                                    "precio": 2000,
                                    "categoria": "BEBIDA",
                                    "stock": 75
                                }
                                """;
                DatosListadoProducto producto = RestAssured.given()
                                .contentType("application/json")
                                .body(crearProductoJson)
                                .when()
                                .post("/api/productos")
                                .then()
                                .statusCode(201)
                                .extract()
                                .body().as(DatosListadoProducto.class);

                // 3. Crear un pedido
                String crearPedidoJson = """
                                {
                                    "idCliente": %d,
                                    "datosCrearDetallesPedido": [
                                        {
                                            "idProducto": %d,
                                            "cantidad": 3
                                        }
                                    ]
                                }
                                """.formatted(cliente.id(), producto.id());

                DatosListadoPedido pedido = RestAssured.given()
                                .contentType("application/json")
                                .body(crearPedidoJson)
                                .when()
                                .post("/api/pedidos")
                                .then()
                                .statusCode(201)
                                .extract()
                                .body().as(DatosListadoPedido.class);

                // 4. Cancelar el pedido
                RestAssured.given()
                                .contentType("application/json")
                                .when()
                                .put("/api/pedidos/" + pedido.id() + "/cancelar-pedido")
                                .then()
                                .statusCode(200);

                // 5. Verificar que el pedido fue cancelado
                RestAssured.given()
                                .contentType("application/json")
                                .when()
                                .get("/api/pedidos/" + pedido.id())
                                .then()
                                .statusCode(200)
                                .body("id", equalTo(pedido.id().intValue()))
                                .body("estado", equalTo("CANCELADO"));

                // 6. Verificar que el stock no se modificó
                RestAssured.given()
                                .contentType("application/json")
                                .when()
                                .get("/api/productos/" + producto.id())
                                .then()
                                .statusCode(200)
                                .body("stock", equalTo(75));
        }
}