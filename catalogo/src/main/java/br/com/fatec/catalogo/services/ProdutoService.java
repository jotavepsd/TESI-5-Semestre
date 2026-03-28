package br.com.fatec.catalogo.services;

import br.com.fatec.catalogo.models.ProdutoModel;
import br.com.fatec.catalogo.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    public List<ProdutoModel> listarTodos(String nome) {
        if (nome != null && !nome.isBlank()) {
            return repository.findByNomeContainingIgnoreCase(nome);
        }
        return repository.findAll();
    }

    public void salvar(ProdutoModel produto) {
        // Busca produtos com nome similar para validar duplicidade
        List<ProdutoModel> existentes = repository.findByNomeContainingIgnoreCase(produto.getNome());

        // Se encontrar o mesmo nome em um ID diferente, bloqueia
        boolean nomeJaExiste = existentes.stream()
                .anyMatch(p -> p.getNome().equalsIgnoreCase(produto.getNome())
                        && !p.getIdProduto().equals(produto.getIdProduto()));

        if (nomeJaExiste) {
            throw new IllegalArgumentException("Já existe um produto com este nome.");
        }
        repository.save(produto);
    }

    public ProdutoModel buscarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Não encontrado"));
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}