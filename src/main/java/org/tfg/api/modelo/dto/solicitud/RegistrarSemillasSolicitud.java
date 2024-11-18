package org.tfg.api.modelo.dto.solicitud;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor

public class RegistrarSemillasSolicitud {
    private String id;
    private String semilla;
    private String variedad;
    private double cantidadDisponible;
    private LocalDate fechaAdquisicion;
    private LocalDate fechaExpiracion;
}

