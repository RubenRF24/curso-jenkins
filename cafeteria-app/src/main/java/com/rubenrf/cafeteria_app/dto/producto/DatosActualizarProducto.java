package com.rubenrf.cafeteria_app.dto.producto;

import com.rubenrf.cafeteria_app.model.Categoria;

public record DatosActualizarProducto(String nombre, Double precio, Categoria categoria,
        Integer stock) {

}
