package com.banquillo.api.jugador;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JugadorRepository extends JpaRepository<Jugador, Long> {

    Optional<Jugador> findByDorsal(int dorsal);

    boolean existsByDorsal(int dorsal);

    List<Jugador> findAllByOrderByDorsalAsc();
}
