package org.tfg.api.modelo.dto.respuesta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ResultadosRespuesta {
    private List<?> resultados;
}
