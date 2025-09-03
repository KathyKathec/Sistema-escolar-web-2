package X.Etapa1.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import X.Etapa1.dtos.CategoriaDto;
import X.Etapa1.model.CategoriaModel;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

	@Autowired
	CategoriaRepository repositorio;
	
	
	@GetMapping("/")
	public String index() {
		return "usuario/index";
	}
	
	@GetMapping("/inserir/")
	public String inserir() {
		return "usuario/inserir";
	}
	
	@PostMapping("/inserir/")
	public String inserirDB(@ModelAttribute @Valid CategoriaDto 
			categoriaDto, BindingResult result, RedirectAttributes msg 
		) {
		if(result.hasErrors()) {
			msg.addFlashAttribute("erroCadastrar", "Erro ao cadastrar nova categoria");
			return "redirect:/categoria/inserir";
		}
		var categoriaModel = new CategoriaModel();
		BeanUtils.copyProperties(categoriaDto, categoriaModel);
		repositorio.save(categoriaModel);
		msg.addFlashAttribute("sucessoCadastrar", "Categoria cadastrada com sucesso!");
		return "redirect:../listar/";
	}
	
	@PostMapping("/listar/")
	public ModelAndView listarComFiltro(@RequestParam("tipo") String busca) {
		ModelAndView mv= new ModelAndView("categoria/listar");
		List<CategoriaModel> lista = repositorio.findAll();
		mv.addObject("categorias",lista);
		return mv;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
