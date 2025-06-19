package com.rubenrf.cafeteria_app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rubenrf.cafeteria_app.dto.cliente.DatosCrearCliente;
import com.rubenrf.cafeteria_app.dto.cliente.DatosListadoCliente;
import com.rubenrf.cafeteria_app.model.Cliente;

public interface ClienteService {

    Cliente crearCliente(DatosCrearCliente datosCrearCliente);

    Page<DatosListadoCliente> listarClientes(Pageable pageable);

    Cliente buscarClientePorId(Long idCliente);

    Cliente actualizarCliente(Cliente cliente);

    void eliminarCliente(Long idCliente);

    void eliminarTodosLosClientes();

}
