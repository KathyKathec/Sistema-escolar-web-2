package X.Etapa1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import X.Etapa1.model.CursoModel;

@Repository
public interface CursoRepository extends JpaRepository<CursoModel, Integer>{

	List<CursoModel> findByCategoriaId(int id);

	List<CursoModel> findByNomeContainingIgnoreCase(String nome);


}
