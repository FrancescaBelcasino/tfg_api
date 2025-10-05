package org.tfg.api.modelo.entidad;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@Document(collection = "usuarios")
public class Usuario {
    @Id
    private String id;
    private String adminId;
    private String email;
    private String contrasena;
    private Rol rol;
    private boolean bloqueado;
    private String tokenRecuperacion;
    private LocalDateTime tokenExpiracion;

    public enum Rol {
        ADMINISTRADOR,
        ESTANDAR
    }
}
