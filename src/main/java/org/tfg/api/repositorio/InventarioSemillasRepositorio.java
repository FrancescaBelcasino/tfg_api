package org.tfg.api.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tfg.api.modelo.entidad.InventarioSemillas;

import java.util.List;

public interface InventarioSemillasRepositorio extends MongoRepository<InventarioSemillas, String> {

    InventarioSemillas findByIdAndAdminId(String id, String adminId);

    List<InventarioSemillas> findByAdminId(String adminId);
}
