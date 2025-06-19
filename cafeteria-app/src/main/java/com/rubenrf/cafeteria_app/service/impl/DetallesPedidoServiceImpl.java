package com.rubenrf.cafeteria_app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rubenrf.cafeteria_app.model.DetallesPedido;
import com.rubenrf.cafeteria_app.repository.DetallesPedidoRepository;
import com.rubenrf.cafeteria_app.service.DetallesPedidoService;

@Service
public class DetallesPedidoServiceImpl implements DetallesPedidoService {

    @Autowired
    private DetallesPedidoRepository detallesPedidoRepository;

    @Override
    @Transactional
    public void crearDetallesPedido(List<DetallesPedido> detallesPedidoList) {

        detallesPedidoRepository.saveAll(detallesPedidoList);
    }

}
