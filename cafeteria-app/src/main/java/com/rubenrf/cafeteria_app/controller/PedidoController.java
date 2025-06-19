package com.rubenrf.cafeteria_app.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.rubenrf.cafeteria_app.dto.detallesPedido.DatosListadoDetallesPedido;
import com.rubenrf.cafeteria_app.dto.pedido.DatosCrearPedido;
import com.rubenrf.cafeteria_app.dto.pedido.DatosListadoPedido;
import com.rubenrf.cafeteria_app.model.Cliente;
import com.rubenrf.cafeteria_app.model.DetallesPedido;
import com.rubenrf.cafeteria_app.model.Pedido;
import com.rubenrf.cafeteria_app.service.ClienteService;
import com.rubenrf.cafeteria_app.service.DetallesPedidoService;
import com.rubenrf.cafeteria_app.service.PedidoService;
import com.rubenrf.cafeteria_app.service.ProductoService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private DetallesPedidoService detallesPedidoService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<?> crearPedido(@RequestBody @Valid DatosCrearPedido datosCrearPedido,
            UriComponentsBuilder uriComponentsBuilder) {

        Cliente cliente = clienteService.buscarClientePorId(datosCrearPedido.idCliente());
        if (cliente == null) {
            return ResponseEntity.badRequest().body("Cliente no encontrado");
        }

        Pedido pedidoCreado = pedidoService.crearPedido(cliente);

        List<DetallesPedido> detallesPedidoList = datosCrearPedido.datosCrearDetallesPedido().stream()
                .map(detalle -> {
                    DetallesPedido detallesPedido = new DetallesPedido();
                    detallesPedido.setCantidad(detalle.cantidad());
                    detallesPedido.setProducto(productoService.buscarProductoPorId(detalle.idProducto()));
                    detallesPedido.setPedido(pedidoCreado);
                    return detallesPedido;
                }).toList();

        pedidoCreado.setDetallesPedidoList(detallesPedidoList);

        detallesPedidoService.crearDetallesPedido(detallesPedidoList);

        pedidoCreado.getDetallesPedidoList().forEach(detalle -> {
            productoService.actualizarStock(detalle.getProducto(), detalle.getCantidad());
        });

        URI uri = uriComponentsBuilder.path("/api/pedidos/{id}").buildAndExpand(pedidoCreado.getId()).toUri();

        return ResponseEntity.created(uri)
                .body(new DatosListadoPedido(pedidoCreado.getId(), pedidoCreado.getCliente().getNombre(),
                        pedidoCreado.getEstado(),
                        pedidoCreado.getDetallesPedidoList().stream().map(detalle -> {
                            return new DatosListadoDetallesPedido(detalle.getProducto().getNombre(),
                                    detalle.getCantidad());
                        }).toList(), pedidoCreado.getDetallesPedidoList().stream().map(detalle -> {
                            return detalle.getProducto().getPrecio() * detalle.getCantidad();
                        }).reduce(0.0, Double::sum)));

    }

    @PutMapping("/{id}/entregar-pedido")
    public ResponseEntity<?> entregarPedido(@PathVariable Long id) {

        pedidoService.entregarPedido(id);

        return ResponseEntity.ok().body("Pedido #" + id + " entregado con éxito.");
    }

    @PutMapping("/{id}/cancelar-pedido")
    public ResponseEntity<?> cancelarPedido(@PathVariable Long id) {

        Pedido pedidoCancelado = pedidoService.cancelarPedido(id);

        pedidoCancelado.getDetallesPedidoList().forEach(detalle -> {
            productoService.actualizarStock(detalle.getProducto(), detalle.getCantidad() * -1);
        });
        
        return ResponseEntity.ok().body("Pedido #" + id + " cancelado.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPedido(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPedidoPorId(id);
        if (pedido == null) {
            return ResponseEntity.badRequest().body("Pedido no encontrado");
        }

        return ResponseEntity.ok(new DatosListadoPedido(pedido.getId(), pedido.getCliente().getNombre(),
                pedido.getEstado(),
                pedido.getDetallesPedidoList().stream().map(detalle -> {
                    return new DatosListadoDetallesPedido(detalle.getProducto().getNombre(),
                            detalle.getCantidad());
                }).toList(), pedido.getDetallesPedidoList().stream().map(detalle -> {
                    return detalle.getProducto().getPrecio() * detalle.getCantidad();
                }).reduce(0.0, Double::sum)));
    }

}
