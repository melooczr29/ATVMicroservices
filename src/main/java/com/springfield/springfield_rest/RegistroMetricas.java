package com.springfield.springfield_rest;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class RegistroMetricas {

    private final MeterRegistry registry;
    private final UsuarioRepository usuarioRepo;

    public RegistroMetricas(MeterRegistry registry, UsuarioRepository usuarioRepo) {
        this.registry = registry;
        this.usuarioRepo = usuarioRepo;
    }

    @PostConstruct
    public void registrarContadorUsuarios() {
        registry.gauge("springfield_usuarios_total",
                Tags.empty(),
                usuarioRepo,
                repo -> repo.count());
    }
}