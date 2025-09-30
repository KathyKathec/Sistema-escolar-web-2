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
import org.springframework.ui.Model;
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

import X.Etapa1.dtos.CursoDto;
import X.Etapa1.model.CategoriaModel;
import X.Etapa1.model.CursoModel;
import X.Etapa1.model.ProfessorModel;
import X.Etapa1.repositories.CategoriaRepository;
import X.Etapa1.repositories.CursoRepository;
import X.Etapa1.repositories.ProfessorRepository;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/curso")
public class CursoControler {

	@Autowired
	CursoRepository repositorio;
	@Autowired
	ProfessorRepository professorRepository;
	@Autowired
	CategoriaRepository categoriaRepository;

	@GetMapping("/")
	public String index(Model model) {
	    model.addAttribute("cursos", repositorio.findAll());
	    model.addAttribute("categorias", categoriaRepository.findAll());
	    return "index";
	}

	@GetMapping("/inserir/")
	public ModelAndView inserir() {
	    ModelAndView mv = new ModelAndView("cursos/inserir");

	    // professores
	    List<ProfessorModel> professores = professorRepository.findAll();
	    mv.addObject("professores", professores);

	    // categorias
	    List<CategoriaModel> categorias = categoriaRepository.findAll();
	    mv.addObject("categorias", categorias);

	    return mv;
	}

	
	@PostMapping("/inserir/")
	public String inserirBD(
	        @ModelAttribute @Valid CursoDto cursoDto,
	        BindingResult result, RedirectAttributes msg,
	        @RequestParam("file") MultipartFile imagem,
	        @RequestParam("professorId") int professorId,
	        @RequestParam("categoriaId") int categoriaId) {

	    if(result.hasErrors()) {

	        System.out.println(result.getAllErrors());
	        msg.addFlashAttribute("erroCadastrar", "Erro ao cadastrar novo curso");
	        return "redirect:/curso/inserir/";
	    }

	    var curso = new CursoModel();
	    BeanUtils.copyProperties(cursoDto, curso);

	    // professor
	    ProfessorModel professor = professorRepository.findById(professorId)
	            .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
	    curso.setProfessor(professor);

	    // categoria
	    CategoriaModel categoria = categoriaRepository.findById(categoriaId)
	            .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));
	    curso.setCategoria(categoria);

	    try {
	        if(!imagem.isEmpty()) {
	            byte[] bytes = imagem.getBytes();
	            Path caminho = Paths.get("./src/main/resources/static/img/"+imagem.getOriginalFilename());
	            Files.write(caminho, bytes);
	            curso.setImagem(imagem.getOriginalFilename());
	        }
	    } catch(IOException e) {
	        System.out.println("erro imagem");
	    }
	    System.out.println(">>> CHEGOU NO SALVAR <<<");

	    repositorio.save(curso);
	    msg.addFlashAttribute("sucessoCadastrar", "Curso cadastrado!");
	    return "redirect:../listar/";

	}

	@GetMapping("/listar/")
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("cursos/listar");
		List<CursoModel> lista = repositorio.findAll();
		mv.addObject("cursos", lista);
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
	@GetMapping("/categoria/{id}")
	public ModelAndView listarPorCategoria(@PathVariable("id") int id) {
	    ModelAndView mv = new ModelAndView("index");
	    List<CursoModel> cursos = repositorio.findByCategoriaId(id);
	    mv.addObject("cursos", cursos);
	    mv.addObject("categorias", categoriaRepository.findAll());
	    return mv;
	}
	@GetMapping("/curso/buscar")
	public String buscar(@RequestParam("q") String query, Model model) {
	    List<CursoModel> cursos = repositorio.findByNomeContainingIgnoreCase(query);
	    model.addAttribute("cursos", cursos);
	    model.addAttribute("categorias", categoriaRepository.findAll()); // mantém no menu
	    return "index"; // reaproveita a mesma tela inicial
	}

	@GetMapping("/detalhes/{id}")
	public ModelAndView detalhes(@PathVariable("id") int id) {
	    ModelAndView mv = new ModelAndView("cursos/detalhes");
	    Optional<CursoModel> curso = repositorio.findById(id);
	    if(curso.isPresent()) {
	        mv.addObject("curso", curso.get());
	    }
	    return mv;
	}

	@GetMapping("/buscar")
	public ModelAndView buscar(@RequestParam("q") String q) {
	    ModelAndView mv = new ModelAndView("index");
	    List<CursoModel> cursos = repositorio.findByNomeContainingIgnoreCase(q);
	    mv.addObject("cursos", cursos);
	    mv.addObject("categorias", categoriaRepository.findAll());
	    return mv;
	}

	@GetMapping("/editar/{id}")
	public ModelAndView editar(@PathVariable(value="id") int id){
	    ModelAndView mv = new ModelAndView("cursos/editar");
	    Optional<CursoModel> curso = repositorio.findById(id);

	    if(curso.isEmpty()) {
	        return new ModelAndView("redirect:/curso/listar/");
	    }

	    mv.addObject("curso", curso.get());

	    // professores
	    mv.addObject("professores", professorRepository.findAll());

	    // categorias
	    mv.addObject("categorias", categoriaRepository.findAll());

	    return mv;
	}

	@PostMapping("/editar/{id}")
	public String editarBD(
	        @ModelAttribute @Valid CursoDto cursoDto,
	        BindingResult result, 
	        @RequestParam("file") MultipartFile imagem,
	        RedirectAttributes msg,
	        @PathVariable(value="id") int id,
	        @RequestParam("professorId") int professorId) {

	    Optional<CursoModel> cursoOpt = repositorio.findById(id);

	    if(cursoOpt.isEmpty()) {
	        msg.addFlashAttribute("erroEditar", "Curso não encontrado!");
	        return "redirect:/curso/listar/";
	    }

	    if(result.hasErrors()) {
	        msg.addFlashAttribute("erroEditar", "Erro ao editar o curso!");
	        return "redirect:/curso/editar/" + id;
	    }

	    var cursoModel = cursoOpt.get();
	    BeanUtils.copyProperties(cursoDto, cursoModel);

	    // busca o professor e seta
	    ProfessorModel professor = professorRepository.findById(professorId)
	            .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
	    cursoModel.setProfessor(professor);

	    try {
	        if(!imagem.isEmpty()) {
	            byte[] bytes = imagem.getBytes();
	            Path caminho = Paths.get("./src/main/resources/static/img/"+imagem.getOriginalFilename());
	            Files.write(caminho, bytes);
	            cursoModel.setImagem(imagem.getOriginalFilename());
	        }
	    } catch(IOException e) {
	        System.out.println("erro imagem");
	    }

	    repositorio.save(cursoModel);
	    msg.addFlashAttribute("sucessoEditar", "Curso editado!");
	    return "redirect:/curso/listar/";
	}

	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable(value="id") int id){
		Optional<CursoModel> curso = repositorio.findById(id);
		if(curso.isEmpty()) {
			return "redirect:../../curso/listar/";
		}
		repositorio.deleteById(id);
		return "redirect:../../curso/listar/";
	}
	
	
	
	
	
	
	
	
}
