package com.example.pontoacessibilidade.service;

import com.example.pontoacessibilidade.entity.PontoAcessibilidade;
import com.example.pontoacessibilidade.repository.PontoAcessibilidadeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class PontoAcessibilidadeService {

    @Autowired
    PontoAcessibilidadeRepository repository;

    public List<PontoAcessibilidade> listarTodos() {
        return repository.findAll();
    }

    public PontoAcessibilidade buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Ponto não encontrado: " + id));
    }

    public PontoAcessibilidade criar(PontoAcessibilidade ponto) {
        if (ponto.getNomeLocal() == null || ponto.getNomeLocal().trim().isEmpty()) {
            throw new IllegalArgumentException("nomeLocal é obrigatório");
        }
        if (ponto.getEndereco() == null || ponto.getEndereco().trim().isEmpty()) {
            throw new IllegalArgumentException("endereco é obrigatório");
        }
        return repository.save(ponto);
    }

    public PontoAcessibilidade atualizar(Long id, PontoAcessibilidade dadosAtualizados) {
        PontoAcessibilidade existente = buscarPorId(id);

        existente.setNomeLocal(dadosAtualizados.getNomeLocal());
        existente.setEndereco(dadosAtualizados.getEndereco());
        existente.setTipoLocal(dadosAtualizados.getTipoLocal());
        existente.setTemRampa(dadosAtualizados.isTemRampa());
        existente.setTemBanheiroAdaptado(dadosAtualizados.isTemBanheiroAdaptado());
        existente.setTemVagaPCD(dadosAtualizados.isTemVagaPCD());
        existente.setPossuiObstaculos(dadosAtualizados.isPossuiObstaculos());
        existente.setAcessibilidadeGeral(dadosAtualizados.getAcessibilidadeGeral());
        existente.setDescricao(dadosAtualizados.getDescricao());

        return repository.save(existente);
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Ponto não encontrado: " + id);
        }
        repository.deleteById(id);
    }

    public int calcularIndiceAcessibilidade(Long id) {
        PontoAcessibilidade ponto = buscarPorId(id);
        return calcularIndiceAcessibilidade(ponto);
    }

    public int calcularIndiceAcessibilidade(PontoAcessibilidade ponto) {
        int indice = 0;
        if (ponto.isTemRampa()) indice += 25;
        if (ponto.isTemBanheiroAdaptado()) indice += 25;
        if (ponto.isTemVagaPCD()) indice += 25;
        if (!ponto.isPossuiObstaculos()) indice += 25;
        return indice;
    }
}
