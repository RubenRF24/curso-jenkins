package com.rubenrf.cafeteria_app.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.rubenrf.cafeteria_app.dto.cliente.DatosActualizarCliente;
import com.rubenrf.cafeteria_app.dto.cliente.DatosCrearCliente;
import com.rubenrf.cafeteria_app.dto.cliente.DatosListadoCliente;
import com.rubenrf.cafeteria_app.dto.cliente.DatosListadoTotalCliente;
import com.rubenrf.cafeteria_app.model.Cliente;
import com.rubenrf.cafeteria_app.model.Estado;
import com.rubenrf.cafeteria_app.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<?> crearCliente(@RequestBody @Valid DatosCrearCliente datosCrearCliente,
            UriComponentsBuilder uriComponentsBuilder) {
        Cliente cliente = clienteService.crearCliente(datosCrearCliente);

        URI uri = uriComponentsBuilder.path("/api/clientes/{id}").buildAndExpand(cliente.getId()).toUri();

        return ResponseEntity.created(uri).body(new DatosListadoCliente(cliente.getId(), cliente.getNombre(),
                cliente.getCorreo(), cliente.getTelefono()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerClientePorId(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarClientePorId(id);

        return ResponseEntity.ok(cliente);
    }

    @GetMapping
    public ResponseEntity<?> listarClientes(Pageable pageable) {
        Page<DatosListadoCliente> clientes = clienteService.listarClientes(pageable);

        return clientes.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(clientes.getContent());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarCliente(@PathVariable Long id,
            @RequestBody @Valid DatosActualizarCliente datosActualizarCliente) {

        Cliente cliente = clienteService.buscarClientePorId(id);

        cliente.actualizarCliente(datosActualizarCliente);

        Cliente clienteActualizado = clienteService.actualizarCliente(cliente);
        return ResponseEntity.ok().body(new DatosListadoCliente(clienteActualizado.getId(),
                clienteActualizado.getNombre(), clienteActualizado.getCorreo(), clienteActualizado.getTelefono()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/total-pedidos")
    public ResponseEntity<?> obtenerTotalPedidos(@PathVariable Long id){

        Cliente cliente = clienteService.buscarClientePorId(id);

        Double totalPedidos = cliente.getPedidosList().stream().map(pedido -> {
            if(pedido.getEstado().equals(Estado.CANCELADO.toString())){
                return 0.0;
            }
            return pedido.getDetallesPedidoList().stream().map(detalle -> {
                return detalle.getProducto().getPrecio() * detalle.getCantidad();
            }).reduce(0.0, Double::sum);
        }).reduce(0.0, Double::sum);

        return ResponseEntity.ok(new DatosListadoTotalCliente(cliente.getId(), cliente.getNombre(), cliente.getCorreo(), cliente.getTelefono(), totalPedidos));
    }

}
