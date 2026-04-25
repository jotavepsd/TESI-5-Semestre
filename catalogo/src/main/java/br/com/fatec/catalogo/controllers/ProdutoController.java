package br.com.fatec.catalogo.controllers;

import br.com.fatec.catalogo.models.ProdutoModel;
import br.com.fatec.catalogo.services.CategoriaService;
import br.com.fatec.catalogo.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;
    private final CategoriaService categoriaService;

    public ProdutoController(ProdutoService service, CategoriaService categoriaService) {
        this.service = service;
        this.categoriaService = categoriaService;
    }

    @GetMapping("/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("produto", new ProdutoModel());
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "cadastro-produto";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        ProdutoModel produto = service.buscarPorId(id);
        model.addAttribute("produto", produto);
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "cadastro-produto";
    }

    @PostMapping("/salvar")
    public String salvarProduto(@Valid @ModelAttribute("produto") ProdutoModel produto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categorias", categoriaService.listarTodas());
            return "cadastro-produto";
        }

        try {
            service.salvar(produto);
        } catch (RuntimeException e) {
            result.rejectValue("nome", "error.produto", e.getMessage());
            model.addAttribute("categorias", categoriaService.listarTodas());
            return "cadastro-produto";
        }

        return "redirect:/produtos";
    }

    @GetMapping("/excluir/{id}")
    public String excluirProduto(@PathVariable Long id) {
        service.excluir(id);
        return "redirect:/produtos";
    }

    @GetMapping
    public String listar(@RequestParam(required = false) String nome, Model model) {
        model.addAttribute("produtos", service.listarTodos(nome));
        model.addAttribute("nomeFiltro", nome);
        return "lista-produtos";
    }
}