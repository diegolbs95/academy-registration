package com.gym.registration.repository;

import com.gym.registration.entity.Cadastros;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CadastroRepository extends JpaRepository<Cadastros, Long> {

    Optional<Cadastros> findByCodigoEntradaId (Integer codigoEntrada);
}
