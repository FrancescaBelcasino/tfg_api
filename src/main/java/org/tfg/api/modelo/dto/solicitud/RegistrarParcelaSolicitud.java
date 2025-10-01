package org.tfg.api.modelo.dto.solicitud;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class RegistrarParcelaSolicitud {
    private String id;
    private String campoId;
    private String nombre;
    private List<List<Double>> coordenadas;
    private String estado;
    private List<String> caracteristicas;
    private String semilla;
    private String semillaNombre;
    private Double cantidadSembrada;
    private Double superficie;
    private Double superficieHa;
}
