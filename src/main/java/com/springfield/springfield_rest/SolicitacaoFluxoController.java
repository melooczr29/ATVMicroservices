package com.springfield.springfield_rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/solicitacao-fluxo")
public class SolicitacaoFluxoController {

    private final GerenciadorSolicitacaoService gerenciadorService;

    public SolicitacaoFluxoController(GerenciadorSolicitacaoService gerenciadorService) {
        this.gerenciadorService = gerenciadorService;
    }

    @PostMapping
    @Operation(summary = "Registra uma nova solicitação de serviço")
    public ResponseEntity<?> registrarNovaSolicitacao(@RequestBody Map<String, String> payload) {
        try {
            Integer cidadaoId = Integer.parseInt(payload.getOrDefault("cidadaoId", "0"));
            String descricao = payload.get("descricao");
            if (descricao == null || descricao.isBlank()) {
                return ResponseEntity.badRequest().body("Descricao é obrigatória.");
            }
            Long demandaId = gerenciadorService.novaSolicitacao(cidadaoId, descricao);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("demandaIdGerada", demandaId));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("ID do cidadão inválido.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{demandaId}/acao")
    @Operation(summary = "Aplica uma ação (evento) a uma solicitação existente")
    public ResponseEntity<?> aplicarAcao(
            @PathVariable Long demandaId,
            @RequestBody Map<String, String> payload) {
        try {
            String acaoNome = payload.get("acao");
            if (acaoNome == null)
                return ResponseEntity.badRequest().body("Campo 'acao' é obrigatório.");

            EventoSolicitacao evento = EventoSolicitacao.valueOf(acaoNome.toUpperCase());
            EstadoSolicitacao novoEstado = gerenciadorService.processarAcao(demandaId, evento);
            return ResponseEntity.ok(Map.of("novoEstado", novoEstado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Acao invalida. Use 'ANALISAR' ou 'CONCLUIR'.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/cidadao/{cidadaoId}")
    @Operation(summary = "Consulta o histórico de todas as solicitações de um cidadão")
    public ResponseEntity<?> consultarHistoricoCidadao(
            @Parameter(description = "ID do Cidadão") @PathVariable Integer cidadaoId) {
        try {
            List<RegistroFluxoSolicitacao> historico = gerenciadorService.verHistorico(cidadaoId);
            return ResponseEntity.ok(historico);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}