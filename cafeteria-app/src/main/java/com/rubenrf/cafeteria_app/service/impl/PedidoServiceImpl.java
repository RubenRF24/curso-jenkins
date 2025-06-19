package com.rubenrf.cafeteria_app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rubenrf.cafeteria_app.model.Cliente;
import com.rubenrf.cafeteria_app.model.Estado;
import com.rubenrf.cafeteria_app.model.Pedido;
import com.rubenrf.cafeteria_app.repository.PedidoRepository;
import com.rubenrf.cafeteria_app.service.PedidoService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Override
    @Transactional
    public Pedido crearPedido(Cliente cliente) {

        Pedido pedido = Pedido.builder()
                .cliente(cliente)
                .estado(Estado.PENDIENTE.toString())
                .build();

        return pedidoRepository.save(pedido);

    }

    @Override
    @Transactional
    public Pedido cancelarPedido(Long idPedido) {
        Pedido pedido = buscarPedidoPorId(idPedido);
        pedido.setEstado(Estado.CANCELADO.toString());
        return pedidoRepository.save(pedido);
    }

    @Override
    @Transactional
    public void entregarPedido(Long idPedido) {
        Pedido pedido = buscarPedidoPorId(idPedido);
        pedido.setEstado(Estado.ENTREGADO.toString());
        pedidoRepository.save(pedido);

    }

    @Override
    @Transactional(readOnly = true)
    public void listarPedidosPorCliente(Long idCliente) {
    }

    @Override
    @Transactional(readOnly = true)
    public Pedido buscarPedidoPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pedido #" + id + " no encontrado."));
    }

}
