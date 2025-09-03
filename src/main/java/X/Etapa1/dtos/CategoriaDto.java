package X.Etapa1.dtos;

import jakarta.validation.constraints.NotBlank;

public record CategoriaDto(@NotBlank String nome) {

}
