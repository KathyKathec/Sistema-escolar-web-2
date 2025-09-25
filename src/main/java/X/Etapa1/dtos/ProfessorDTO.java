package X.Etapa1.dtos;

import jakarta.validation.constraints.NotBlank;

public record ProfessorDTO( @NotBlank String nome, @NotBlank String email) {

}
