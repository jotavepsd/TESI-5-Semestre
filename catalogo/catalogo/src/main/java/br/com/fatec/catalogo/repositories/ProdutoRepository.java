package br.com.fatec.catalogo.repositories;

import br.com.fatec.catalogo.models.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoModel, Long> {

    List<ProdutoModel> findByNomeContainingIgnoreCase(String nome);

    Optional<ProdutoModel> findByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);
}