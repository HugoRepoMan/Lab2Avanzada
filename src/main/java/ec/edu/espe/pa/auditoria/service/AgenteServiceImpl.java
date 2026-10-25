package ec.edu.espe.pa.auditoria.service;

import ec.edu.espe.pa.auditoria.dto.AgenteRequest;
import ec.edu.espe.pa.auditoria.dto.AgenteResponse;
import ec.edu.espe.pa.auditoria.entity.Agente;
import ec.edu.espe.pa.auditoria.entity.EstadoAgente;
import ec.edu.espe.pa.auditoria.exception.AgenteNotFoundException;
import ec.edu.espe.pa.auditoria.exception.ConflictoException;
import ec.edu.espe.pa.auditoria.exception.ReglaNegocioException;
import ec.edu.espe.pa.auditoria.mapper.AgenteMapper;
import ec.edu.espe.pa.auditoria.repository.AgenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AgenteServiceImpl implements AgenteService {

    private final AgenteRepository repo;
    private final AgenteMapper mapper;

    @Override
    public Page<AgenteResponse> listar(Pageable pageable) {
        return repo.findAll(pageable).map(mapper::toResponse);
    }

    @Override
    public Page<AgenteResponse> buscarPorNombre(String nombre, Pageable pageable) {
        String filtro = nombre == null ? "" : nombre.trim();
        return repo.findByNombreContainingIgnoreCase(filtro, pageable).map(mapper::toResponse);
    }

    @Override
    public AgenteResponse obtenerPorId(Long id) {
        return repo.findById(id)
                .map(mapper::toResponse)
                .orElseThrow(() -> new AgenteNotFoundException(id));
    }

    @Override
    @Transactional
    public AgenteResponse crear(AgenteRequest req) {
        validarReglas(req);
        if (repo.existsByCodigo(req.codigo().trim())) {
            throw new ConflictoException("Ya existe un agente con el código " + req.codigo());
        }
        Agente guardado = repo.save(mapper.toEntity(req));
        return mapper.toResponse(guardado);
    }

    @Override
    @Transactional
    public AgenteResponse actualizar(Long id, AgenteRequest req) {
        Agente e = repo.findById(id)
                .orElseThrow(() -> new AgenteNotFoundException(id));
        validarReglas(req);
        if (repo.existsByCodigoAndIdNot(req.codigo().trim(), id)) {
            throw new ConflictoException("Ya existe otro agente con el código " + req.codigo());
        }
        mapper.update(e, req);
        return mapper.toResponse(repo.saveAndFlush(e));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Agente e = repo.findById(id)
                .orElseThrow(() -> new AgenteNotFoundException(id));
        if (e.getEstado() == EstadoAgente.ACTIVO) {
            throw new ReglaNegocioException(
                    "No se puede eliminar un agente que se encuentra en estado ACTIVO; debe suspenderse o desconectarse primero"
            );
        }
        repo.delete(e);
    }

    private void validarReglas(AgenteRequest req) {
        if (Boolean.FALSE.equals(req.activo()) && req.estado() == EstadoAgente.ACTIVO) {
            throw new ReglaNegocioException(
                    "Un agente inactivo (activo = false) no puede tener el estado ACTIVO"
            );
        }
    }
}
