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
    private String nombre;
    private String variedad;
    private String proveedor;
    private Double cantidad;
    private LocalDate fechaAdquisicion;
    private LocalDate fechaExpiracion;
}

