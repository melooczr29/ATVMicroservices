package com.springfield.springfield_rest;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CidadaoService {
    private final CidadaoRepository repository;

    public CidadaoService(CidadaoRepository repository) {
        this.repository = repository;
    }

    public List<Cidadao> listarTodos() {
        return repository.findAll();
    }

    public Optional<Cidadao> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public Cidadao salvar(Cidadao cidadao) {
        return repository.save(cidadao);
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
    }

    public Cidadao atualizar(Integer id, Cidadao cidadaoAtualizado) {
        return repository.findById(id)
                .map(cidadaoExistente -> {
                    cidadaoExistente.setNome(cidadaoAtualizado.getNome());
                    cidadaoExistente.setEndereco(cidadaoAtualizado.getEndereco());
                    cidadaoExistente.setBairro(cidadaoAtualizado.getBairro());
                    return repository.save(cidadaoExistente);
                })
                .orElseGet(() -> {
                    cidadaoAtualizado.setId(id);
                    return repository.save(cidadaoAtualizado);
                });
    }
}