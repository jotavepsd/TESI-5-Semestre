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
        if (repository.existsByNome(produto.getNome())) {
            throw new IllegalArgumentException("Já existe um produto com este nome.");
        }
        repository.save(produto);
    }

    public ProdutoModel buscarPorId(long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Não encontrado"));
    }

    public void excluir(long id) {
        repository.deleteById(id);
    }
}