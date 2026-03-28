package br.com.fatec.catalogo.controllers;

import br.com.fatec.catalogo.models.ProdutoModel;
import br.com.fatec.catalogo.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @GetMapping("/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("produto", new ProdutoModel());
        return "cadastro-produto";
    }

    @PostMapping("/salvar")
    public String salvarProduto(@Valid @ModelAttribute("produto") ProdutoModel produto, BindingResult result) {
        if (result.hasErrors()) {
            return (produto.getIdProduto() == null || produto.getIdProduto() == 0) ? "cadastro-produto" : "editar-produto";
        }

        // Se o ID for 0, forçamos null para o banco gerar o próximo ID (Auto-incremento)
        if (produto.getIdProduto() != null && produto.getIdProduto() == 0) {
            produto.setIdProduto(null);
        }

        try {
            service.salvar(produto);
        } catch (IllegalArgumentException e) {
            result.rejectValue("nome", "error.produto", e.getMessage());
            return "cadastro-produto";
        }
        return "redirect:/produtos";
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String nome, Model model) {
        model.addAttribute("produtos", service.listarTodos(nome));
        return "lista-produtos";
    }
}