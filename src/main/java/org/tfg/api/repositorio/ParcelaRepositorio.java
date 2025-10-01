package org.tfg.api.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tfg.api.modelo.entidad.Parcela;

import java.util.List;

public interface ParcelaRepositorio extends MongoRepository<Parcela, String> {

    Parcela findByIdAndAdminId(String id, String adminId);

    List<Parcela> findByAdminId(String adminId);
}
