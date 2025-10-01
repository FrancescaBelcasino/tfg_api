package org.tfg.api.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tfg.api.modelo.entidad.Campo;

import java.util.List;

public interface CampoRepositorio extends MongoRepository<Campo, String> {

    Campo findByIdAndAdminId(String id, String adminId);

    List<Campo> findByAdminId(String adminId);
}
