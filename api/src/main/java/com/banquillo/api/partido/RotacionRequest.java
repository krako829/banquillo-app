package com.banquillo.api.partido;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record RotacionRequest(
        @NotEmpty List<Integer> dorsales,
        @Min(1) int duracion,
        @Min(1) int jugadoresEnCampo,
        @Min(1) int minutosPorBloque) {
}
