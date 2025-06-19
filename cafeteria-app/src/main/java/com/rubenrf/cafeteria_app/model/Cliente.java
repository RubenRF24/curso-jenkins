package com.rubenrf.cafeteria_app.model;

import java.util.List;

import com.rubenrf.cafeteria_app.dto.cliente.DatosActualizarCliente;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbl_cliente")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "correo")
    private String correo;

    @Column(name = "telefono")
    private String telefono;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Pedido> pedidosList;



    public void actualizarCliente(DatosActualizarCliente datosActualizarCliente) {
        if (datosActualizarCliente.nombre() != null) {
            this.nombre = datosActualizarCliente.nombre();
        }
        if (datosActualizarCliente.correo() != null) {
            this.correo = datosActualizarCliente.correo();
        }
        if (datosActualizarCliente.telefono() != null) {
            this.telefono = datosActualizarCliente.telefono();
        }
    }
}
