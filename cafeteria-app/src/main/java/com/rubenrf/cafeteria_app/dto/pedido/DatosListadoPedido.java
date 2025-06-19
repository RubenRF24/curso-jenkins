package com.rubenrf.cafeteria_app.dto.pedido;

import java.util.List;

import com.rubenrf.cafeteria_app.dto.detallesPedido.DatosListadoDetallesPedido;

public record DatosListadoPedido(Long id, String nombreCliente, String estado,
        List<DatosListadoDetallesPedido> detallesPedido, Double total) {

}
