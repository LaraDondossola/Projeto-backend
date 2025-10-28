package com.example.cuidar.repositories;

import com.example.cuidar.models.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {
}