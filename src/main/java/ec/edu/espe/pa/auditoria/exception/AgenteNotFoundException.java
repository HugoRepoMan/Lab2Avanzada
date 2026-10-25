package ec.edu.espe.pa.auditoria.exception;

public class AgenteNotFoundException extends RuntimeException {

    public AgenteNotFoundException(Long id) {
        super("Agente con id " + id + " no encontrado");
    }
}
