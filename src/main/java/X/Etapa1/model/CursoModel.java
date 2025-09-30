package X.Etapa1.model;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="curso")
public class CursoModel implements Serializable{

	

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotBlank
	private String nome;
	@NotBlank
	private String descricao;
	@NotNull
	private LocalDate dataInicio;

	private LocalDate dataFinal;
	
	private String imagem;


    // Relacionamento N para 1
    @ManyToOne
    @JoinColumn(name = "professor_id") // chave estrangeira
    private ProfessorModel professor;
	
	@ManyToOne
	@JoinColumn(name = "categoria_id")
	private CategoriaModel categoria;
	
	public CategoriaModel getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaModel categoria) {
		this.categoria = categoria;
	}

	public ProfessorModel getProfessor() {
		return professor;
	}

	public void setProfessor(ProfessorModel professor) {
		this.professor = professor;
	}

	// getters & setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}


	

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(LocalDate dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getImagem() {
		return imagem;
	}

	public void setImagem(String imagem) {
		this.imagem = imagem;
	}
}
