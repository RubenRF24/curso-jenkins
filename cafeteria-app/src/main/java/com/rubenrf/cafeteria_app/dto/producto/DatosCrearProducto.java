package com.rubenrf.cafeteria_app.dto.producto;

import com.rubenrf.cafeteria_app.model.Categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DatosCrearProducto(@NotBlank String nombre, @NotNull Double precio, @NotNull Categoria categoria,
        @NotNull Integer stock) {
    // Constructor, getters, and other methods can be added here if needed

}
