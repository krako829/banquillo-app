package com.banquillo.api.entrenamiento;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntrenamientoRepository extends JpaRepository<Entrenamiento, Long> {

    Optional<Entrenamiento> findByFecha(LocalDate fecha);

    boolean existsByFecha(LocalDate fecha);

    List<Entrenamiento> findAllByOrderByFechaAsc();

    long countByAsistentes_Dorsal(int dorsal);
}
