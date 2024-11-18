package org.tfg.api.modelo.dto.respuesta;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor

public class ResultadosRespuesta {
    private List<?> results;
}
