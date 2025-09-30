package X.Etapa1.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name="categoria")
public class CategoriaModel implements Serializable{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@OneToMany(mappedBy = "categoria")
	private List<CursoModel> cursos;

	@NotBlank
	private String nome;

	public String getNome() {
		return nome;
	}

	public List<CursoModel> getCursos() {
		return cursos;
	}

	public void setCursos(List<CursoModel> cursos) {
		this.cursos = cursos;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	
	
	
}
