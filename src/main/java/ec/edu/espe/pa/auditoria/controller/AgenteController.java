package ec.edu.espe.pa.auditoria.controller;

import ec.edu.espe.pa.auditoria.dto.AgenteRequest;
import ec.edu.espe.pa.auditoria.dto.AgenteResponse;
import ec.edu.espe.pa.auditoria.service.AgenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/agentes")
@RequiredArgsConstructor
public class AgenteController {

    private final AgenteService service;

    @GetMapping
    public Page<AgenteResponse> listar(@PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return service.listar(pageable);
    }

    @GetMapping("/buscar")
    public Page<AgenteResponse> buscar(
            @RequestParam String nombre,
            @PageableDefault(size = 10, sort = "nombre") Pageable pageable
    ) {
        return service.buscarPorNombre(nombre, pageable);
    }

    @GetMapping("/{id}")
    public AgenteResponse obtener(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @PostMapping
    public ResponseEntity<AgenteResponse> crear(@Valid @RequestBody AgenteRequest req) {
        AgenteResponse creado = service.crear(req);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.id())
                .toUri();
        return ResponseEntity.created(location).body(creado);
    }

    @PutMapping("/{id}")
    public AgenteResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AgenteRequest req
    ) {
        return service.actualizar(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
