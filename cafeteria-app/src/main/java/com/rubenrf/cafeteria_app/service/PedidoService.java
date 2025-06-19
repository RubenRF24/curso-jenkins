package com.rubenrf.cafeteria_app.service;

import com.rubenrf.cafeteria_app.model.Cliente;
import com.rubenrf.cafeteria_app.model.Pedido;

public interface PedidoService {

    Pedido crearPedido(Cliente cliente);

    Pedido cancelarPedido(Long idPedido);

    void entregarPedido(Long idPedido);

    void listarPedidosPorCliente(Long idCliente);

    Pedido buscarPedidoPorId(Long id);

}
