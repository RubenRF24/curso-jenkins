package com.rubenrf.cafeteria_app.dto.pedido;

import java.util.List;

import com.rubenrf.cafeteria_app.dto.detallesPedido.DatosCrearDetallesPedido;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.Valid;

public record DatosCrearPedido(@NotNull Long idCliente,
        @Valid List<DatosCrearDetallesPedido> datosCrearDetallesPedido) {

}
