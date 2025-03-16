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
    private String nombre;
    private String variedad;
    private Double cantidad;
    private LocalDate fechaCosecha;
    private String ubicacionAlmacenamiento;
    private String calidad;
}
