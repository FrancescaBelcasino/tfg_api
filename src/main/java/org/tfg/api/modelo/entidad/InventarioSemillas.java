package org.tfg.api.modelo.entidad;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
@Document("inventarioSemillas")
public class InventarioSemillas {
    @Id
    private String id;
    private String semilla;
    private String variedad;
    private double cantidadDisponible;
    private LocalDate fechaAdquisicion;
    private LocalDate fechaExpiracion;
}
