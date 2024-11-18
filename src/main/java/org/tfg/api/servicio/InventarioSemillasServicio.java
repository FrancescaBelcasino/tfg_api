package org.tfg.api.servicio;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tfg.api.modelo.dto.solicitud.RegistrarSemillasSolicitud;
import org.tfg.api.modelo.entidad.InventarioSemillas;
import org.tfg.api.repositorio.InventarioSemillasRepositorio;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class InventarioSemillasServicio {
    private InventarioSemillasRepositorio inventarioSemillasRepositorio;

    public String registrarSemillas(RegistrarSemillasSolicitud solicitud) {
        InventarioSemillas inventarioSemillas = inventarioSemillasRepositorio.save(
                InventarioSemillas.builder()
                        .semilla(solicitud.getSemilla())
                        .variedad(solicitud.getVariedad())
                        .cantidadDisponible(solicitud.getCantidadDisponible())
                        .fechaAdquisicion(solicitud.getFechaAdquisicion())
                        .fechaExpiracion(solicitud.getFechaExpiracion())
                        .build()
        );

        return inventarioSemillas.getId();
    }

    public List<InventarioSemillas> mostrarSemillas() {
        return inventarioSemillasRepositorio.findAll();
    }

    public List<InventarioSemillas> obtenerSemillasPorAnio(int anio) {
        return inventarioSemillasRepositorio.obtenerSemillasPorAnio(anio);
    }

    public List<InventarioSemillas> obtenerSemillasPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return inventarioSemillasRepositorio.obtenerSemillasPorRangoFecha(fechaInicio, fechaFin);
    }

}
