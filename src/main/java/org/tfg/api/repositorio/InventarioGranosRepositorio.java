package org.tfg.api.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tfg.api.modelo.entidad.InventarioGranos;

import java.util.List;

public interface InventarioGranosRepositorio extends MongoRepository<InventarioGranos, String> {

    InventarioGranos findByIdAndAdminId(String id, String adminId);

    List<InventarioGranos> findByAdminId(String adminId);
}
