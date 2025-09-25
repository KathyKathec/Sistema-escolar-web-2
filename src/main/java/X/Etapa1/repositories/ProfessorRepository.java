package X.Etapa1.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import X.Etapa1.model.ProfessorModel;

@Repository
public interface ProfessorRepository extends JpaRepository<ProfessorModel, Integer> {

		List<ProfessorModel>findProfessorByNomeLike(String nome);
	}


