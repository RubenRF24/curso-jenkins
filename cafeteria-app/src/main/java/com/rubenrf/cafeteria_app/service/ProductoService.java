package com.rubenrf.cafeteria_app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.rubenrf.cafeteria_app.dto.producto.DatosCrearProducto;
import com.rubenrf.cafeteria_app.dto.producto.DatosListadoProducto;
import com.rubenrf.cafeteria_app.model.Producto;

public interface ProductoService {

    Producto crearProducto(DatosCrearProducto datosCrearProducto);

    void eliminarProducto(Long id);

    Producto actualizarProducto(Producto producto);

    Page<DatosListadoProducto> listarProductos(Pageable pageable);

    List<DatosListadoProducto> listarProductos();

    Producto buscarProductoPorId(Long id);

    void actualizarStock(Producto producto, int cantidad);

    void eliminarTodosLosProductos();

}
