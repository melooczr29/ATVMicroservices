package com.springfield.springfield_rest;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final CidadaoRepository cidadaoRepository;

    @Autowired
    public UsuarioService(UsuarioRepository repository, CidadaoRepository cidadaoRepository) {
        this.repository = repository;
        this.cidadaoRepository = cidadaoRepository;
    }

    public Optional<Usuario> buscarPorUsername(String username) {
        return repository.findByUsername(username);
    }

    public Usuario salvar(Usuario usuario) {
        Optional<Usuario> usuarioExistente = repository.findByCidadao(usuario.getCidadao());
        if (usuarioExistente.isPresent()) {
            throw new RuntimeException("Já existe um usuário vinculado a esse cidadão.");
        }

        Optional<Cidadao> cidadaoExistente = cidadaoRepository.findById(usuario.getCidadao().getId());
        if (!cidadaoExistente.isPresent()) {
            throw new RuntimeException("Cidadão não encontrado.");
        }

        return repository.save(usuario);
    }

    public void realizarLogin(String username, String senha) {
        Optional<Usuario> usuarioOpt = repository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.getBloqueado()) {
            throw new RuntimeException("Usuário está bloqueado. Não é possível realizar login.");
        }

        if (!usuario.getSenha().equals(senha)) {
            usuario.setTentativasFalhas(usuario.getTentativasFalhas() + 1);
            repository.save(usuario);

            if (usuario.getTentativasFalhas() >= 3) {
                usuario.setBloqueado(true);
                repository.save(usuario);
                throw new RuntimeException("Usuário bloqueado após 3 tentativas falhas.");
            }

            throw new RuntimeException("Senha incorreta.");
        }

        usuario.setTentativasFalhas(0);
        usuario.setUltimoLogin(LocalDateTime.now());
        repository.save(usuario);

        if (usuario.getUltimaAlteracaoSenha().isBefore(LocalDateTime.now().minusDays(30))) {
            throw new RuntimeException("A senha precisa ser alterada após 30 dias de inatividade.");
        }
    }

    public void trocarSenha(String username, String novaSenha) {
        Optional<Usuario> usuarioOpt = repository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setSenha(novaSenha);
        usuario.setUltimaAlteracaoSenha(LocalDateTime.now());
        usuario.setSenhaExpirada(false);
        repository.save(usuario);
    }

    public void desbloquearUsuario(String username) {
        Optional<Usuario> usuarioOpt = repository.findByUsername(username);

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setBloqueado(false);
        usuario.setTentativasFalhas(0);
        repository.save(usuario);
    }
}
