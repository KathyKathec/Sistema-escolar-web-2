package X.Etapa1.dtos;

import java.time.LocalDate;
import java.util.Date;

import X.Etapa1.model.ProfessorModel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CursoDto(@NotBlank String nome, @NotBlank String descricao,
		@NotNull LocalDate dataInicio,  LocalDate dataFinal, @NotNull int professorId,
		@NotNull int categoriaId) {

}