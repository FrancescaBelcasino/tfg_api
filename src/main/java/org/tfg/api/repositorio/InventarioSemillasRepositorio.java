package org.tfg.api.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tfg.api.modelo.entidad.InventarioSemillas;

public interface InventarioSemillasRepositorio extends MongoRepository<InventarioSemillas, String> {
}
