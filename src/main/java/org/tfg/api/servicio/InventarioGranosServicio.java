package org.tfg.api.servicio;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tfg.api.modelo.dto.solicitud.RegistrarGranosSolicitud;
import org.tfg.api.modelo.entidad.InventarioGranos;
import org.tfg.api.repositorio.InventarioGranosRepositorio;

import java.util.List;
import java.util.Optional;

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

    public InventarioGranos mostrarGrano(String id) {
        return inventarioGranosRepositorio.findById(id).orElse(null);
    }

    public String actualizarGrano(String id, RegistrarGranosSolicitud solicitud) {
        return inventarioGranosRepositorio.findById(id)
                .map(g -> g.toBuilder()
                        .grano(solicitud.getGrano())
                        .variedad(solicitud.getVariedad())
                        .cantidadAlmacenada(solicitud.getCantidadAlmacenada())
                        .fechaCosecha(solicitud.getFechaCosecha())
                        .ubicacionAlmacenamiento(solicitud.getUbicacionAlmacenamiento())
                        .build())
                .map(g -> inventarioGranosRepositorio.save(g))
                .map(InventarioGranos::getId)
                .orElse(null);
    }

    public boolean eliminarGrano(String id) {
        Optional<InventarioGranos> optionalGrano = inventarioGranosRepositorio.findById(id);

        if (!optionalGrano.isPresent()) {
            return false;
        }

        inventarioGranosRepositorio.deleteById(id);

        return true;
    }

}
