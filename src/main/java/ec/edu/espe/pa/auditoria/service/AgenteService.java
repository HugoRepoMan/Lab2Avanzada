package ec.edu.espe.pa.auditoria.service;

import ec.edu.espe.pa.auditoria.dto.AgenteRequest;
import ec.edu.espe.pa.auditoria.dto.AgenteResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AgenteService {

    Page<AgenteResponse> listar(Pageable pageable);

    Page<AgenteResponse> buscarPorNombre(String nombre, Pageable pageable);

    AgenteResponse obtenerPorId(Long id);

    AgenteResponse crear(AgenteRequest req);

    AgenteResponse actualizar(Long id, AgenteRequest req);

    void eliminar(Long id);
}
