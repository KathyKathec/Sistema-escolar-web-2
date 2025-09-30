package X.Etapa1.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import X.Etapa1.dtos.ProfessorDTO;
import X.Etapa1.model.ProfessorModel;
import X.Etapa1.repositories.ProfessorRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/professor")
public class ProfessorController {

	@Autowired
	ProfessorRepository repositorio;
	
	@GetMapping("/")
	public String index() {
		return "index"; //acrecentar um index geral
	}
	
	@GetMapping("/inserir/")
	public String inserir() {
		return "professores/inserir";
	}
	
	@PostMapping("/inserir/")
	public String inserirBD(
			@ModelAttribute @Valid ProfessorDTO professorDTO, 
			BindingResult result, RedirectAttributes msg,
			@RequestParam("file") MultipartFile imagem) {
		if(result.hasErrors()) {
			msg.addFlashAttribute("erroCadastrar", "Erro ao cadastrar novo professor");
			return "redirect:/professor/inserir/";
		}
		var professorModel = new ProfessorModel();
		BeanUtils.copyProperties(professorDTO, professorModel);
		try {
			if(!imagem.isEmpty()) {
				byte[] bytes = imagem.getBytes();
				Path caminho = Paths.get(
						"./src/main/resources/static/img/"+imagem.getOriginalFilename());
				Files.write(caminho, bytes);
				professorModel.setImagem(imagem.getOriginalFilename());
			}
		}catch(IOException e) {
			System.out.println("erro imagem");
		}
		repositorio.save(professorModel);
		msg.addFlashAttribute("sucessoCadastrar", "Professor cadastrado!");
		return "redirect:../listar/";
	
	}
	
	@PostMapping("/listar/")
	public ModelAndView listarComFiltro(@RequestParam("busca") String busca) {
		ModelAndView mv = new ModelAndView("professores/listar");
		List<ProfessorModel> lista = repositorio.findProfessorByNomeLike("%"+busca+"%");
		mv.addObject("professores", lista);
		return mv;
	}
	
	@GetMapping("/listar/")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("professores/listar");
		List<ProfessorModel> lista = repositorio.findAll();
		mv.addObject("professores", lista);
		return mv;
	}
	@GetMapping("/mostrarImagem/{img}")
	@ResponseBody
	public byte[] mostrarImagem(@PathVariable(value="img") String img) throws IOException{
		File imagemArquivo = new File("./src/main/resources/static/img/"+img);
		if(img != null || img.trim().length()>0)
		{
			return Files.readAllBytes(imagemArquivo.toPath());
		}
		return null;
	}
	@GetMapping("/editar/{id}")
	public ModelAndView editar(@PathVariable(value="id") int id){
		ModelAndView mv = new ModelAndView("professores/editar");
		Optional<ProfessorModel> professor = repositorio.findById(id);
		mv.addObject("id", professor.get().getId());
		mv.addObject("email", professor.get().getEmail());
		mv.addObject("nome", professor.get().getNome());
		return mv;
	}	
	
	@PostMapping("/editar/{id}")
	public String editarBD(
			@ModelAttribute @Valid ProfessorDTO professorDTO, 
			BindingResult result, RedirectAttributes msg,
			@PathVariable(value="id") int id) {
		
		Optional<ProfessorModel> professor = repositorio.findById(id);

		if(result.hasErrors()) {
		msg.addFlashAttribute("erroEditar", "Erro ao editar o professor!");
			return "redirect:/professor/listar/";
		}
		var professorModel = professor.get();
		BeanUtils.copyProperties(professorDTO, professorModel);
		repositorio.save(professorModel);
		msg.addFlashAttribute("sucessoEditar", "Professor editado!");
		return "redirect:../../professor/listar/";
	}	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable(value="id") int id){
		Optional<ProfessorModel> professor = repositorio.findById(id);
		if(professor.isEmpty()) {
			return "redirect:../../professor/listar/";
		}
		repositorio.deleteById(id);
		return "redirect:../../professor/listar/";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
	
	
	

