package org.tfg.api.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tfg.api.modelo.entidad.InventarioGranos;

import java.time.LocalDate;
import java.util.List;

public interface InventarioGranosRepositorio extends MongoRepository<InventarioGranos, String> {


    // Método para obtener las semillas por año de adquisición
    List<InventarioGranos> obtenerGranosPorAnio(int anio);

    // Método para obtener semillas dentro de un rango de fechas
    List<InventarioGranos> obtenerGranosPorRangoFecha(LocalDate fechaInicio, LocalDate fechaFin);
}
