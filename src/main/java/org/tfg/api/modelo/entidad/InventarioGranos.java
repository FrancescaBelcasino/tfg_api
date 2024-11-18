package org.tfg.api.modelo.entidad;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Getter
@Builder
@Document("inventarioGranos")
public class InventarioGranos {
    @Id
    private String id;
    private String grano;
    private String variedad;
    private double cantidadAlmacenada;
    private LocalDate fechaCosecha;
    private String ubicacionAlmacenamiento;
}

