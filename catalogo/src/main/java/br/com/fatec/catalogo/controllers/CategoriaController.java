package br.com.fatec.catalogo.controllers;

import br.com.fatec.catalogo.models.CategoriaModel;
import br.com.fatec.catalogo.services.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    private final CategoriaService service;

    public CategoriaController(CategoriaService service) {
        this.service = service;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("categorias", service.listarTodas());
        return "lista-categorias";
    }

    @GetMapping("/nova")
    public String exibirFormulario(Model model) {
        model.addAttribute("categoria", new CategoriaModel());
        return "cadastro-categoria";
    }

    @GetMapping("/editar/{id}")
    public String exibirFormularioEdicao(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", service.buscarPorId(id));
        return "cadastro-categoria";
    }

    @PostMapping("/salvar")
    public String salvar(@Valid @ModelAttribute("categoria") CategoriaModel categoria, BindingResult result) {
        if (result.hasErrors()) {
            return "cadastro-categoria";
        }

        try {
            service.salvar(categoria);
        } catch (IllegalArgumentException e) {
            result.rejectValue("nome", "error.categoria", e.getMessage());
            return "cadastro-categoria";
        }

        return "redirect:/categorias";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable Long id, Model model) {
        try {
            service.excluir(id);
        } catch (RuntimeException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("categorias", service.listarTodas());
            return "lista-categorias";
        }
        return "redirect:/categorias";
    }
}