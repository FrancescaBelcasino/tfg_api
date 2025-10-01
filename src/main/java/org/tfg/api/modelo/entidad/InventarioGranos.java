package org.tfg.api.modelo.entidad;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Builder(toBuilder = true)
@Document("inventarioGranos")
public class InventarioGranos {
    @Id
    private String id;
    private String usuarioId;
    private String adminId;
    private String nombre;
    private String variedad;
    private Double cantidad;
    private LocalDate fechaCosecha;
    private String ubicacionAlmacenamiento;
    private String calidad;
}

