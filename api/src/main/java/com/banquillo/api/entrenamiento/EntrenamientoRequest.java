package com.banquillo.api.entrenamiento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record EntrenamientoRequest(@NotNull LocalDate fecha, @NotBlank @Size(max = 200) String objetivo) {
}
