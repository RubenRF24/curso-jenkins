package com.rubenrf.cafeteria_app.dto.detallesPedido;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;

public record DatosCrearDetallesPedido(@NotNull Long idProducto, @NotNull Integer cantidad) {

}
