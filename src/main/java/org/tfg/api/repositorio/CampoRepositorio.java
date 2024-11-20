package org.tfg.api.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tfg.api.modelo.entidad.Campo;

public interface CampoRepositorio extends MongoRepository<Campo, String> {
}
