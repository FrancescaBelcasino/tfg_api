package org.tfg.api.modelo.entidad;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@Document(collection = "usuarios")
public class Usuario {
    @Id
    private String id;
    private String email;
    private String contrasena;
    private Rol rol;

    public enum Rol {
        ADMINISTRADOR,
        ESTANDAR
    }
}
