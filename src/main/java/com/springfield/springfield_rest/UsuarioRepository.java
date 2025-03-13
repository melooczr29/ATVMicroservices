package com.springfield.springfield_rest;

import com.springfield.springfield_rest.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByCidadao(Cidadao cidadao);
}
