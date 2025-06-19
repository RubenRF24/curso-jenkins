package com.rubenrf.cafeteria_app.dto.producto;

import com.rubenrf.cafeteria_app.model.Categoria;

public record DatosRespuestaProducto(Long id, String nombre, Double precio, Categoria categoria, Integer stock) {
    // Constructor, getters, and other methods can be added here if needed

}
