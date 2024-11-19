package org.tfg.api.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tfg.api.modelo.entidad.InventarioGranos;

public interface InventarioGranosRepositorio extends MongoRepository<InventarioGranos, String> {
}
