package org.tfg.api.servicio;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tfg.api.modelo.dto.solicitud.RegistrarGranosSolicitud;
import org.tfg.api.modelo.entidad.InventarioGranos;
import org.tfg.api.repositorio.InventarioGranosRepositorio;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor

public class InventarioGranosServicio {
    private InventarioGranosRepositorio inventarioGranosRepositorio;

    public String registrarGranos(RegistrarGranosSolicitud solicitud) {
        InventarioGranos inventarioGranos = inventarioGranosRepositorio.save(
                InventarioGranos.builder()
                        .grano(solicitud.getGrano())
                        .variedad(solicitud.getVariedad())
                        .cantidadAlmacenada(solicitud.getCantidadAlmacenada())
                        .fechaCosecha(solicitud.getFechaCosecha())
                        .ubicacionAlmacenamiento(solicitud.getUbicacionAlmacenamiento())
                        .build()
        );

        return inventarioGranos.getId();
    }

    public List<InventarioGranos> mostrarGranos() {
        return inventarioGranosRepositorio.findAll();
    }

    // Obtener granos por año de adquisición
    public List<InventarioGranos> obtenerGranosPorAnio(int anio) {
        return inventarioGranosRepositorio.obtenerGranosPorAnio(anio);
    }

    // Obtener granos por rango de fechas
    public List<InventarioGranos> obtenerGranosPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return inventarioGranosRepositorio.obtenerGranosPorRangoFecha(fechaInicio, fechaFin);
    }
}
