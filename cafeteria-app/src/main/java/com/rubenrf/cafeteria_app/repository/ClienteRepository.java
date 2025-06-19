package com.rubenrf.cafeteria_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rubenrf.cafeteria_app.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
