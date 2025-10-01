package org.tfg.api.modelo.entidad;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
@Document("parcelas")
public class Parcela {
    @Id
    private String id;
    private String usuarioId;
    private String adminId;
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
