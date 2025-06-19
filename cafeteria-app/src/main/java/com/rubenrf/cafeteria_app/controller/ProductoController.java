package com.rubenrf.cafeteria_app.controller;

import java.net.URI;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.rubenrf.cafeteria_app.dto.producto.DatosActualizarProducto;
import com.rubenrf.cafeteria_app.dto.producto.DatosCrearProducto;
import com.rubenrf.cafeteria_app.dto.producto.DatosListadoProducto;
import com.rubenrf.cafeteria_app.dto.producto.DatosRespuestaProducto;
import com.rubenrf.cafeteria_app.dto.producto.DatosRespuestasProductosMasVendidos;
import com.rubenrf.cafeteria_app.model.Categoria;
import com.rubenrf.cafeteria_app.model.Producto;
import com.rubenrf.cafeteria_app.service.ProductoService;

import jakarta.validation.Valid;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody @Valid DatosCrearProducto datosCrearProducto,
            UriComponentsBuilder uriComponentsBuilder) {

        Producto producto = productoService.crearProducto(datosCrearProducto);
        URI uri = uriComponentsBuilder.path("/api/productos/{id}").buildAndExpand(producto.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new DatosRespuestaProducto(producto.getId(),
                producto.getNombre(), producto.getPrecio(), Categoria.valueOf(producto.getCategoria()),
                producto.getStock()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerProductoPorID(@PathVariable Long id) {

        Producto producto = productoService.buscarProductoPorId(id);

        return ResponseEntity.ok(new DatosRespuestaProducto(producto.getId(), producto.getNombre(),
        producto.getPrecio(), Categoria.valueOf(producto.getCategoria()), producto.getStock()));
    }

    @GetMapping
    public ResponseEntity<?> obtenerProductos(Pageable pageable) {

        Page<DatosListadoProducto> productos = productoService.listarProductos(pageable);

        return productos.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(productos.getContent());

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id,
            @RequestBody DatosActualizarProducto datosActualizarProducto) {

        Producto producto = productoService.buscarProductoPorId(id);
        producto.actualizarProducto(datosActualizarProducto);

        Producto productoActualizado = productoService.actualizarProducto(producto);

        return ResponseEntity.ok(new DatosRespuestaProducto(productoActualizado.getId(),
                productoActualizado.getNombre(), productoActualizado.getPrecio(),
                Categoria.valueOf(productoActualizado.getCategoria()), productoActualizado.getStock()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {

        productoService.eliminarProducto(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/mas-vendidos")
    public ResponseEntity<?> obtenerProductosMasVendidos() {

        List<DatosListadoProducto> productos = productoService.listarProductos();

        List<DatosRespuestasProductosMasVendidos> productosMasVendidos = productos.stream().map(producto -> new DatosRespuestasProductosMasVendidos(producto.id(), producto.nombre(), producto.unidadesVendidas())).sorted(Comparator.comparingInt(DatosRespuestasProductosMasVendidos::unidadesVendidas).reversed()).toList();

        return ResponseEntity.ok(productosMasVendidos);
    }
    

}
