package com.springfield.springfield_rest;

import com.springfield.springfield_rest.Cidadao;
import com.springfield.springfield_rest.CidadaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cidadaos")
public class CidadaoController {
    private final CidadaoService service;

    public CidadaoController(CidadaoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cidadao> listarTodos() {
        return service.listarTodos();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cidadao> atualizar(
            @PathVariable Integer id,
            @RequestBody Cidadao cidadaoAtualizado) {
        Optional<Cidadao> cidadaoExistente = service.buscarPorId(id);

        if (cidadaoExistente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        cidadaoAtualizado.setId(id);
        Cidadao cidadaoSalvo = service.salvar(cidadaoAtualizado);
        return ResponseEntity.ok(cidadaoSalvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cidadao> buscarPorId(@PathVariable Integer id) {
        Optional<Cidadao> cidadao = service.buscarPorId(id);
        return cidadao.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Cidadao cadastrar(@RequestBody Cidadao cidadao) {
        return service.salvar(cidadao);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}