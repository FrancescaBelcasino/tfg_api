package org.tfg.api.modelo.dto.solicitud;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class RegistrarParcelaSolicitud {
    private String id;
    private String nombre;
    private List<List<Double>> coordenadas;
    private String estado;
    private List<String> caracteristicas;
    private String semilla;
    private Double cantidadSembrada;
    private Double superficie;
}
