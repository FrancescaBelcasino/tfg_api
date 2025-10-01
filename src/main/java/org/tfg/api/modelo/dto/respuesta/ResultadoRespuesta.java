package org.tfg.api.modelo.dto.respuesta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ResultadoRespuesta {
    private String resultado;
}
