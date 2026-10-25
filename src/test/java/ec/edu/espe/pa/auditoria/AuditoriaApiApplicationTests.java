package ec.edu.espe.pa.auditoria;

import ec.edu.espe.pa.auditoria.dto.AgenteRequest;
import ec.edu.espe.pa.auditoria.dto.AgenteResponse;
import ec.edu.espe.pa.auditoria.entity.Agente;
import ec.edu.espe.pa.auditoria.entity.EstadoAgente;
import ec.edu.espe.pa.auditoria.exception.ConflictoException;
import ec.edu.espe.pa.auditoria.exception.ReglaNegocioException;
import ec.edu.espe.pa.auditoria.mapper.AgenteMapper;
import ec.edu.espe.pa.auditoria.repository.AgenteRepository;
import ec.edu.espe.pa.auditoria.service.AgenteServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuditoriaApiApplicationTests {

    @Mock
    private AgenteRepository repo;

    @Spy
    private AgenteMapper mapper = new AgenteMapper();

    @InjectMocks
    private AgenteServiceImpl service;

    @Test
    void crearAgenteValidoRetornaResponse() {
        AgenteRequest req = new AgenteRequest(
                " AGT-001 ",
                " SRV-PROD-01 ",
                new BigDecimal("512.50"),
                EstadoAgente.EN_MANTENIMIENTO,
                LocalDate.now(),
                true
        );

        when(repo.existsByCodigo("AGT-001")).thenReturn(false);
        when(repo.save(any(Agente.class))).thenAnswer(inv -> {
            Agente a = inv.getArgument(0);
            a.setId(1L);
            return a;
        });

        AgenteResponse res = service.crear(req);

        assertNotNull(res);
        assertEquals(1L, res.id());
        assertEquals("AGT-001", res.codigo());
        assertEquals("SRV-PROD-01", res.nombre());
    }

    @Test
    void crearAgenteDuplicadoLanzaConflictoException() {
        AgenteRequest req = new AgenteRequest(
                "AGT-001",
                "SRV-PROD-01",
                new BigDecimal("512.50"),
                EstadoAgente.EN_MANTENIMIENTO,
                LocalDate.now(),
                true
        );

        when(repo.existsByCodigo("AGT-001")).thenReturn(true);

        assertThrows(ConflictoException.class, () -> service.crear(req));
    }

    @Test
    void crearAgenteInactivoConEstadoActivoLanzaReglaNegocioException() {
        AgenteRequest req = new AgenteRequest(
                "AGT-002",
                "SRV-PROD-02",
                new BigDecimal("256.00"),
                EstadoAgente.ACTIVO,
                LocalDate.now(),
                false
        );

        assertThrows(ReglaNegocioException.class, () -> service.crear(req));
    }

    @Test
    void eliminarAgenteEnEstadoActivoLanzaReglaNegocioException() {
        Agente activo = new Agente();
        activo.setId(1L);
        activo.setCodigo("AGT-001");
        activo.setEstado(EstadoAgente.ACTIVO);

        when(repo.findById(1L)).thenReturn(Optional.of(activo));

        assertThrows(ReglaNegocioException.class, () -> service.eliminar(1L));
    }
}
