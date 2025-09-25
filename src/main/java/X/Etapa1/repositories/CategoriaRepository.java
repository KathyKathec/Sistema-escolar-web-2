package X.Etapa1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import X.Etapa1.model.CategoriaModel;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaModel, Integer>{

	List<CategoriaModel>findCategoriaByNomeLike(String nome);
	
	
	
}
