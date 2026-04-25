package br.com.fatec.catalogo.services;

import br.com.fatec.catalogo.models.CategoriaModel;
import br.com.fatec.catalogo.models.ProdutoModel;
import br.com.fatec.catalogo.repositories.CategoriaRepository;
import br.com.fatec.catalogo.repositories.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;
    private final CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository repository, CategoriaRepository categoriaRepository) {
        this.repository = repository;
        this.categoriaRepository = categoriaRepository;
    }

    public List<ProdutoModel> listarTodos(String nome) {
        if (nome != null && !nome.isBlank()) {
            return repository.findByNomeContainingIgnoreCase(nome);
        }
        return repository.findAll();
    }

    public ProdutoModel buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    @Transactional
    public void salvar(ProdutoModel produto) {
        if (produto.getCategoria() == null || produto.getCategoria().getIdCategoria() == null) {
            throw new IllegalArgumentException("Categoria é obrigatória.");
        }

        CategoriaModel categoria = categoriaRepository.findById(produto.getCategoria().getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        produto.setCategoria(categoria);

        repository.findByNomeIgnoreCase(produto.getNome()).ifPresent(p -> {
            if (!p.getIdProduto().equals(produto.getIdProduto())) {
                throw new IllegalArgumentException("Já existe um produto com este nome.");
            }
        });

        repository.save(produto);
    }

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado");
        }
        repository.deleteById(id);
    }
}