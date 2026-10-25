package ec.edu.espe.pa.auditoria.repository;

import ec.edu.espe.pa.auditoria.entity.Agente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgenteRepository extends JpaRepository<Agente, Long> {

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdNot(String codigo, Long id);

    Page<Agente> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);
}
