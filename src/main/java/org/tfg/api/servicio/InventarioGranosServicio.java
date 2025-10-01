package org.tfg.api.servicio;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tfg.api.excepcion.RecursoNoEncontradoException;
import org.tfg.api.excepcion.SolicitudInvalidaException;
import org.tfg.api.modelo.dto.solicitud.RegistrarGranoSolicitud;
import org.tfg.api.modelo.entidad.InventarioGranos;
import org.tfg.api.repositorio.InventarioGranosRepositorio;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InventarioGranosServicio {
    private final InventarioGranosRepositorio inventarioGranosRepositorio;

    public List<InventarioGranos> obtenerGranos(String adminId) {
        return inventarioGranosRepositorio.findByAdminId(adminId);
    }

    public InventarioGranos obtenerGranoPorId(String id, String adminId) {
        return Optional.ofNullable(inventarioGranosRepositorio.findByIdAndAdminId(id, adminId))
                .orElseThrow(() -> new RecursoNoEncontradoException("Grano no encontrado con ID: " + id));
    }

    public String registrarGranos(RegistrarGranoSolicitud solicitud, String adminId) {
        if (solicitud.getNombre() == null || solicitud.getCantidad() == null) {
            throw new SolicitudInvalidaException("Nombre y cantidad son obligatorios para registrar un grano.");
        }

        InventarioGranos inventarioGranos = inventarioGranosRepositorio.save(
                InventarioGranos.builder()
                        .adminId(adminId)
                        .nombre(solicitud.getNombre())
                        .variedad(solicitud.getVariedad())
                        .cantidad(solicitud.getCantidad())
                        .fechaCosecha(solicitud.getFechaCosecha())
                        .ubicacionAlmacenamiento(solicitud.getUbicacionAlmacenamiento())
                        .calidad(solicitud.getCalidad())
                        .build()
        );
        return inventarioGranos.getId();
    }

    public String actualizarGrano(String id, RegistrarGranoSolicitud solicitud, String adminId) {
        InventarioGranos granoExistente = Optional.ofNullable(inventarioGranosRepositorio.findByIdAndAdminId(id, adminId))
                .orElseThrow(() -> new RecursoNoEncontradoException("Grano no encontrado con ID: " + id));

        InventarioGranos actualizado = inventarioGranosRepositorio.save(
                granoExistente.toBuilder()
                        .nombre(Optional.ofNullable(solicitud.getNombre()).orElse(granoExistente.getNombre()))
                        .variedad(Optional.ofNullable(solicitud.getVariedad()).orElse(granoExistente.getVariedad()))
                        .cantidad(Optional.ofNullable(solicitud.getCantidad()).orElse(granoExistente.getCantidad()))
                        .fechaCosecha(Optional.ofNullable(solicitud.getFechaCosecha()).orElse(granoExistente.getFechaCosecha()))
                        .ubicacionAlmacenamiento(Optional.ofNullable(solicitud.getUbicacionAlmacenamiento()).orElse(granoExistente.getUbicacionAlmacenamiento()))
                        .calidad(Optional.ofNullable(solicitud.getCalidad()).orElse(granoExistente.getCalidad()))
                        .build()
        );
        return actualizado.getId();
    }

    public boolean eliminarGrano(String id, String adminId) {
        InventarioGranos grano = Optional.ofNullable(inventarioGranosRepositorio.findByIdAndAdminId(id, adminId))
                .orElseThrow(() -> new RecursoNoEncontradoException("No se puede eliminar: grano no encontrado con ID: " + id));

        inventarioGranosRepositorio.delete(grano);
        return true;
    }
}
