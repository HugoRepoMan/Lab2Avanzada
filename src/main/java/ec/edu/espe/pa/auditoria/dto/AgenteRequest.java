package ec.edu.espe.pa.auditoria.dto;

import ec.edu.espe.pa.auditoria.entity.EstadoAgente;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AgenteRequest(
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 20, message = "El código admite máximo 20 caracteres")
        String codigo,

        @NotBlank(message = "El nombre del host es obligatorio")
        @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
        String nombre,

        @NotNull(message = "El consumo de memoria es obligatorio")
        @DecimalMin(value = "0.01", message = "El consumo de memoria debe ser mayor que cero")
        @Digits(integer = 8, fraction = 2)
        BigDecimal consumoMemoriaMb,

        @NotNull(message = "El estado es obligatorio")
        EstadoAgente estado,

        @NotNull(message = "La fecha de registro es obligatoria")
        @PastOrPresent(message = "La fecha de registro no puede ser futura")
        LocalDate fechaRegistro,

        Boolean activo
) {
}
