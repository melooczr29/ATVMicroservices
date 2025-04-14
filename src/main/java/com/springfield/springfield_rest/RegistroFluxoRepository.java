package com.springfield.springfield_rest;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RegistroFluxoRepository extends JpaRepository<RegistroFluxoSolicitacao, Long> {
    List<RegistroFluxoSolicitacao> findByCidadaoIdOrderByDataRegistroDesc(Integer cidadaoId);

    Optional<RegistroFluxoSolicitacao> findFirstByDemandaIdOrderByDataRegistroDesc(Long demandaId);

    List<RegistroFluxoSolicitacao> findByDemandaIdOrderByDataRegistroAsc(Long demandaId);
}