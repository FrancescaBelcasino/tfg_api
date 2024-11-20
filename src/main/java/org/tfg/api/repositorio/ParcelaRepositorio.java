package org.tfg.api.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tfg.api.modelo.entidad.Parcela;

public interface ParcelaRepositorio extends MongoRepository<Parcela, String> {
}
