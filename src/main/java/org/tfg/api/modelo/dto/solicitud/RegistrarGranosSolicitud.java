package org.tfg.api.modelo.dto.solicitud;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor

public class RegistrarGranosSolicitud {
    private String id;
    private String grano;
    private String variedad;
    private double cantidadAlmacenada;
    private LocalDate fechaCosecha;
    private String ubicacionAlmacenamiento;
}
