package com.rubenrf.cafeteria_app.dto.producto;

import com.rubenrf.cafeteria_app.model.Categoria;
import com.rubenrf.cafeteria_app.model.Estado;
import com.rubenrf.cafeteria_app.model.Producto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public record DatosListadoProducto(Long id, String nombre, Double precio, Categoria categoria, Integer stock, int unidadesVendidas) {
    // Constructor, getters, and other methods can be added here if needed

    public DatosListadoProducto(Producto producto) {
        this(producto.getId(), producto.getNombre(), producto.getPrecio(), Categoria.valueOf(producto.getCategoria()),
                producto.getStock(), producto.getDetallesPedidoList().stream().map(detalle -> {
                    if(detalle.getPedido().getEstado().equals(Estado.CANCELADO.toString())){
                        return 0;
                    }
                    return detalle.getCantidad();
                }).reduce(0, Integer::sum));
    }
}
