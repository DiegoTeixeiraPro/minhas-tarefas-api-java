package br.com.minhastarefas.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskRecordDto(
        @NotBlank String title,
        @NotBlank String description,
        @NotNull Boolean status) {
}