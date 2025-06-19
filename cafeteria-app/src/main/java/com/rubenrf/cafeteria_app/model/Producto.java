package com.rubenrf.cafeteria_app.model;

import java.util.List;

import com.rubenrf.cafeteria_app.dto.producto.DatosActualizarProducto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_producto")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "precio")
    private Double precio;

    @Column(name = "categoria")
    private String categoria;

    @Column(name = "stock")
    private Integer stock;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<DetallesPedido> detallesPedidoList;

    public void actualizarProducto(DatosActualizarProducto datosActualizarProducto) {
        if (datosActualizarProducto.nombre() != null) {
            this.nombre = datosActualizarProducto.nombre();
        }
        if (datosActualizarProducto.precio() != null) {
            this.precio = datosActualizarProducto.precio();
        }
        if (datosActualizarProducto.categoria() != null) {
            this.categoria = datosActualizarProducto.categoria().toString();
        }
        if (datosActualizarProducto.stock() != null) {
            this.stock = datosActualizarProducto.stock();
        }
    }

}
