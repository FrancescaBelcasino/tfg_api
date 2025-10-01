package org.tfg.api.modelo.entidad;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder(toBuilder = true)
@Document("campos")
public class Campo {
    @Id
    private String id;
    private String usuarioId;
    private String adminId;
    private String nombre;
    private List<List<Double>> coordenadas;
    private String estado;
    private Double superficie;
    private Double superficieHa;
    private ArrayList<Parcela> parcelas;
}
