package com.rubenrf.cafeteria_app.dto.cliente;

import jakarta.validation.constraints.Email;

public record DatosActualizarCliente(String nombre, @Email String correo, String telefono) {
    // Constructor, getters, and other methods can be added here if needed

}
