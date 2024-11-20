package org.tfg.api.modelo.entidad;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;

@Getter
@Builder(toBuilder = true)
@Document("campos")
public class Campo {
    @Id
    private String id;
    private String nombre;
    private String latitud;
    private String longitud;
    private String estado;
    private Double superficie;
    private ArrayList<Parcela> parcelas;
}
