package ec.edu.espe.pa.auditoria.mapper;

import ec.edu.espe.pa.auditoria.dto.AgenteRequest;
import ec.edu.espe.pa.auditoria.dto.AgenteResponse;
import ec.edu.espe.pa.auditoria.entity.Agente;
import org.springframework.stereotype.Component;

@Component
public class AgenteMapper {

    public Agente toEntity(AgenteRequest r) {
        Agente e = new Agente();
        update(e, r);
        return e;
    }

    public void update(Agente e, AgenteRequest r) {
        e.setCodigo(r.codigo().trim());
        e.setNombre(r.nombre().trim());
        e.setConsumoMemoriaMb(r.consumoMemoriaMb());
        e.setEstado(r.estado());
        e.setFechaRegistro(r.fechaRegistro());
        e.setActivo(r.activo() != null ? r.activo() : true);
    }

    public AgenteResponse toResponse(Agente e) {
        return new AgenteResponse(
                e.getId(),
                e.getCodigo(),
                e.getNombre(),
                e.getConsumoMemoriaMb(),
                e.getEstado(),
                e.getFechaRegistro(),
                e.getActivo(),
                e.getCreadoEn(),
                e.getActualizadoEn()
        );
    }
}
