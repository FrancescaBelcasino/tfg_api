package org.tfg.api.modelo.dto.solicitud;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class RegistrarParcelaSolicitud {
    private String id;
    private String nombre;
    private String latitud;
    private String longitud;
    private String estado;
    private String campoId;
    private Double superficie;
}
