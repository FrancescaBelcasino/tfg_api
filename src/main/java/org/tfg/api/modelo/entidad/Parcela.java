package org.tfg.api.modelo.entidad;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder(toBuilder = true)
@Document("parcelas")
public class Parcela {
    @Id
    private String id;
    private String nombre;
    private String latitud;
    private String longitud;
    private String estado;
    private String campoId;
    private Double superficie;
}
