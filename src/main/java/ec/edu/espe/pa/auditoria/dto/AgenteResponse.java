package ec.edu.espe.pa.auditoria.dto;

import ec.edu.espe.pa.auditoria.entity.EstadoAgente;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AgenteResponse(
        Long id,
        String codigo,
        String nombre,
        BigDecimal consumoMemoriaMb,
        EstadoAgente estado,
        LocalDate fechaRegistro,
        Boolean activo,
        LocalDateTime creadoEn,
        LocalDateTime actualizadoEn
) {
}
