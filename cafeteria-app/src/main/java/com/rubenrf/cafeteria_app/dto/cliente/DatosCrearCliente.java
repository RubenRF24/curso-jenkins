package com.rubenrf.cafeteria_app.dto.cliente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DatosCrearCliente(@NotBlank String nombre, @Email @NotBlank String correo, @NotBlank String telefono) {
    // Constructor, getters, and other methods can be added here if needed

}
