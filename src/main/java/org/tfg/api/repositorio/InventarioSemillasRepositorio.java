package org.tfg.api.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tfg.api.modelo.entidad.InventarioSemillas;

import java.time.LocalDate;
import java.util.List;

public interface InventarioSemillasRepositorio extends MongoRepository<InventarioSemillas, String> {

    // Método para obtener las semillas por año de adquisición
    List<InventarioSemillas> obtenerSemillasPorAnio(int anio);

    // Método para obtener semillas dentro de un rango de fechas
    List<InventarioSemillas> findByFechaAdquisicionGreaterThanAndFechaExpiracionLessThan(LocalDate fechaInicio, LocalDate fechaFin);

}
