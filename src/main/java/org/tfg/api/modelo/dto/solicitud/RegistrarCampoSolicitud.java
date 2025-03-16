package org.tfg.api.modelo.dto.solicitud;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RegistrarCampoSolicitud {
    private String id;
    private String nombre;
    private List<List<Double>> coordenadas;
    private String estado;
    private Double superficie;
}
