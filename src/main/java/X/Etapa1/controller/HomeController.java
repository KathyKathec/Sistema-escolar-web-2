package X.Etapa1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import X.Etapa1.repositories.CategoriaRepository;
import X.Etapa1.repositories.CursoRepository;

@Controller
public class HomeController {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("cursos", cursoRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "index";
    }
}

