package com.rubenrf.cafeteria_app.dto.cliente;

import com.rubenrf.cafeteria_app.model.Cliente;

public record DatosListadoCliente(Long id, String nombre, String correo, String telefono) {
    // Constructor, getters, and other methods can be added here if needed

    public DatosListadoCliente(Cliente cliente) {
        this(cliente.getId(), cliente.getNombre(), cliente.getCorreo(), cliente.getTelefono());
    }

}
