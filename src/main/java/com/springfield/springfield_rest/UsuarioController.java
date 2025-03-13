package com.springfield.springfield_rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioSalvo = service.salvar(usuario);
            return ResponseEntity.ok(usuarioSalvo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<Usuario> buscarPorUsername(@PathVariable String username) {
        return service.buscarPorUsername(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String senha) {
        try {
            service.realizarLogin(username, senha);
            return ResponseEntity.ok("Login bem-sucedido.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/trocar-senha")
    public ResponseEntity<String> trocarSenha(@RequestParam String username, @RequestParam String novaSenha) {
        try {
            service.trocarSenha(username, novaSenha);
            return ResponseEntity.ok("Senha alterada com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/desbloquear")
    public ResponseEntity<String> desbloquearUsuario(@RequestParam String username) {
        try {
            service.desbloquearUsuario(username);
            return ResponseEntity.ok("Usuário desbloqueado com sucesso.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
