package X.Etapa1.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import X.Etapa1.dtos.CategoriaDto;
import X.Etapa1.model.CategoriaModel;
import X.Etapa1.repositories.CategoriaRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/categoria")
public class CategoriaController {

	@Autowired
	CategoriaRepository repositorio;
	
	
	@GetMapping("/")
	public String index() {
		return "index"; //colocar o index geral
	}
	
	@GetMapping("/inserir/")
	public String inserir() {
		return "categorias/inserir";
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
		ModelAndView mv= new ModelAndView("categorias/listar");
		List<CategoriaModel> lista = repositorio.findAll();
		mv.addObject("categorias",lista);
		return mv;
	}
	
	@GetMapping("/listar/")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("categorias/listar");
		List<CategoriaModel> lista = repositorio.findAll();
		mv.addObject("categorias", lista);
		return mv;
	
	}
	
	
	@GetMapping("/editar/{id}")
	public ModelAndView editar(@PathVariable(value="id") int id){
		ModelAndView mv = new ModelAndView("categorias/editar");
		Optional<CategoriaModel> categoria = repositorio.findById(id);
		mv.addObject("id", categoria.get().getId());
		mv.addObject("nome", categoria.get().getNome());
		return mv;
	}	
	
	@PostMapping("/editar/{id}")
	public String editarBD(
			@ModelAttribute @Valid CategoriaDto categoriaDTO, 
			BindingResult result, RedirectAttributes msg,
			@PathVariable(value="id") int id) {
		
		Optional<CategoriaModel>categoria = repositorio.findById(id);

		if(result.hasErrors()) {
			msg.addFlashAttribute("erroEditar", "Erro ao editar a categoria");
			return "redirect:/categoria/listar/";
		}
		var categoriaModel = categoria.get();
		BeanUtils.copyProperties(categoriaDTO, categoriaModel);
		repositorio.save(categoriaModel);
		msg.addFlashAttribute("sucessoEditar", "Categoria editada!");
		return "redirect:../../categoria/listar/";
	}	
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable(value="id") int id){
		Optional<CategoriaModel> categoria = repositorio.findById(id);
		if(categoria.isEmpty()) {
			return "redirect:../../categoria/listar/";
		}
		repositorio.deleteById(id);
		return "redirect:../../categoria/listar/";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
