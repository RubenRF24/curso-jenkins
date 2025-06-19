package com.rubenrf.cafeteria_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rubenrf.cafeteria_app.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}
