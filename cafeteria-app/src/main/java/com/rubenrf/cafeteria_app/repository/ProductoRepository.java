package com.rubenrf.cafeteria_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rubenrf.cafeteria_app.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
