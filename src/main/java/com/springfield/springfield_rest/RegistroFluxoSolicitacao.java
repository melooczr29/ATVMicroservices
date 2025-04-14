package com.springfield.springfield_rest;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class RegistroFluxoSolicitacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long demandaId;
    private Integer cidadaoId;
    private String descricaoSolicitacao;

    @Enumerated(EnumType.STRING)
    private EstadoSolicitacao estadoAtual;

    private LocalDateTime dataRegistro = LocalDateTime.now();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDemandaId() {
        return demandaId;
    }

    public void setDemandaId(Long demandaId) {
        this.demandaId = demandaId;
    }

    public Integer getCidadaoId() {
        return cidadaoId;
    }

    public void setCidadaoId(Integer cidadaoId) {
        this.cidadaoId = cidadaoId;
    }

    public String getDescricaoSolicitacao() {
        return descricaoSolicitacao;
    }

    public void setDescricaoSolicitacao(String descricaoSolicitacao) {
        this.descricaoSolicitacao = descricaoSolicitacao;
    }

    public EstadoSolicitacao getEstadoAtual() {
        return estadoAtual;
    }

    public void setEstadoAtual(EstadoSolicitacao estadoAtual) {
        this.estadoAtual = estadoAtual;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}