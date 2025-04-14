package com.springfield.springfield_rest;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class GerenciadorSolicitacaoService {

    private final StateMachineFactory<EstadoSolicitacao, EventoSolicitacao> fabricaDeMaquinas;
    private final RegistroFluxoRepository fluxoRepo;
    private final CidadaoRepository cidadaoRepo;
    private final AtomicLong contadorDemanda = new AtomicLong(System.currentTimeMillis());

    public GerenciadorSolicitacaoService(
            @Qualifier("solicitacaoStateMachineFactory") StateMachineFactory<EstadoSolicitacao, EventoSolicitacao> fabricaDeMaquinas,
            RegistroFluxoRepository fluxoRepo,
            CidadaoRepository cidadaoRepo) {
        this.fabricaDeMaquinas = fabricaDeMaquinas;
        this.fluxoRepo = fluxoRepo;
        this.cidadaoRepo = cidadaoRepo;
    }

    @Transactional
    public Long novaSolicitacao(Integer cidadaoId, String descricao) {
        if (!cidadaoRepo.existsById(cidadaoId)) {
            throw new RuntimeException("Cidadao nao encontrado: " + cidadaoId);
        }

        Long novaDemandaId = contadorDemanda.incrementAndGet();

        RegistroFluxoSolicitacao registroInicial = new RegistroFluxoSolicitacao();
        registroInicial.setDemandaId(novaDemandaId);
        registroInicial.setCidadaoId(cidadaoId);
        registroInicial.setDescricaoSolicitacao(descricao);
        registroInicial.setEstadoAtual(EstadoSolicitacao.SOLICITADO);
        fluxoRepo.save(registroInicial);

        return novaDemandaId;
    }

    @Transactional
    @SuppressWarnings("deprecation")
    public EstadoSolicitacao processarAcao(Long demandaId, EventoSolicitacao evento) {
        RegistroFluxoSolicitacao ultimoRegistro = fluxoRepo.findFirstByDemandaIdOrderByDataRegistroDesc(demandaId)
                .orElseThrow(() -> new RuntimeException("Demanda nao encontrada: " + demandaId));

        EstadoSolicitacao estadoCorrente = ultimoRegistro.getEstadoAtual();

        StateMachine<EstadoSolicitacao, EventoSolicitacao> sm = fabricaDeMaquinas
                .getStateMachine(Long.toString(demandaId));
        sm.stop();
        sm.getStateMachineAccessor()
                .doWithAllRegions(access -> {
                    access.resetStateMachine(new DefaultStateMachineContext<EstadoSolicitacao, EventoSolicitacao>(
                            estadoCorrente, null, null, null, null));
                });
        sm.start();

        boolean aceito = sm.sendEvent(MessageBuilder.withPayload(evento).build());

        sm.stop();

        if (!aceito) {
            throw new RuntimeException(
                    "Acao " + evento + " nao permitida para o estado " + estadoCorrente + " da demanda " + demandaId);
        }

        EstadoSolicitacao novoEstado = sm.getState().getId();

        RegistroFluxoSolicitacao novoRegistro = new RegistroFluxoSolicitacao();
        novoRegistro.setDemandaId(demandaId);
        novoRegistro.setCidadaoId(ultimoRegistro.getCidadaoId());
        novoRegistro.setDescricaoSolicitacao(ultimoRegistro.getDescricaoSolicitacao());
        novoRegistro.setEstadoAtual(novoEstado);
        fluxoRepo.save(novoRegistro);

        return novoEstado;
    }

    public List<RegistroFluxoSolicitacao> verHistorico(Integer cidadaoId) {
        if (!cidadaoRepo.existsById(cidadaoId)) {
            throw new RuntimeException("Cidadao nao encontrado: " + cidadaoId);
        }
        return fluxoRepo.findByCidadaoIdOrderByDataRegistroDesc(cidadaoId);
    }
}