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

    @GetMapping
    public String listarProdutos(@RequestParam(name = "nome", required = false) String nome, Model model) {
        model.addAttribute("produtos", service.listarTodos(nome));
        model.addAttribute("nomeFiltro", nome);
        return "lista-produtos";
    }

    @GetMapping("/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("produto", new ProdutoModel());
        return "cadastro-produto";
    }

    @PostMapping("/salvar")
    public String salvarProduto(@Valid @ModelAttribute("produto") ProdutoModel produto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "cadastro-produto";
        }

        try {
            service.salvar(produto);
        } catch (IllegalArgumentException e) {
            result.rejectValue("nome", "error.produto", e.getMessage());
            return "cadastro-produto";
        }

        return "redirect:/produtos";
    }

    @GetMapping("/editar/{id}")
    public String exibirEdicao(@PathVariable long id, Model model) {
        model.addAttribute("produto", service.buscarPorId(id));
        return "editar-produto";
    }

    @GetMapping("/excluir/{id}")
    public String excluirProduto(@PathVariable long id) {
        service.excluir(id);
        return "redirect:/produtos";
    }
}