package org.tfg.api.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class SolicitudInvalidaException extends RuntimeException {
  public SolicitudInvalidaException(String mensaje) {
    super(mensaje);
  }
}
