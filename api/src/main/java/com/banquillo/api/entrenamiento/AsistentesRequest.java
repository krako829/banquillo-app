package com.banquillo.api.entrenamiento;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record AsistentesRequest(@NotNull List<Integer> dorsales) {
}
