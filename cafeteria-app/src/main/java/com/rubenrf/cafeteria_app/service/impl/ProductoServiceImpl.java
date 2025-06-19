package com.rubenrf.cafeteria_app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rubenrf.cafeteria_app.dto.producto.DatosCrearProducto;
import com.rubenrf.cafeteria_app.dto.producto.DatosListadoProducto;
import com.rubenrf.cafeteria_app.model.Producto;
import com.rubenrf.cafeteria_app.repository.ProductoRepository;
import com.rubenrf.cafeteria_app.service.ProductoService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    @Transactional
    public Producto crearProducto(DatosCrearProducto datosCrearProducto) {
        Producto producto = Producto.builder()
                .nombre(datosCrearProducto.nombre())
                .precio(datosCrearProducto.precio())
                .categoria(datosCrearProducto.categoria().toString())
                .stock(datosCrearProducto.stock())
                .build();

        return productoRepository.save(producto);

    }

    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new EntityNotFoundException("Producto #" + id + " no encontrado.");
        }
        productoRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Producto actualizarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DatosListadoProducto> listarProductos(Pageable pageable) {
        return productoRepository.findAll(pageable).map(DatosListadoProducto::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Producto buscarProductoPorId(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto #" + id + " no encontrado."));
    }

    @Override
    @Transactional
    public void actualizarStock(Producto producto, int cantidad) {
        if(producto.getStock() < cantidad){
            throw new IllegalArgumentException("No hay suficiente stock de " + producto.getNombre());
        }
        producto.setStock(producto.getStock() - cantidad);
        actualizarProducto(producto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DatosListadoProducto> listarProductos() {
        return productoRepository.findAll().stream().map(DatosListadoProducto::new).toList();
    }

    @Override
    @Transactional
    public void eliminarTodosLosProductos() {
        productoRepository.deleteAll();
    }

    

    

}
