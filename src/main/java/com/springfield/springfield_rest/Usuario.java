package com.springfield.springfield_rest;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "USUARIO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cidadao_id", unique = true, nullable = false)
    private Cidadao cidadao;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private Integer tentativasFalhas = 0;

    @Column(nullable = false)
    private Boolean bloqueado = false;

    @Column(nullable = false)
    private LocalDateTime ultimaAlteracaoSenha = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime ultimoLogin;

    @Column(nullable = false)
    private boolean senhaExpirada = false;
}