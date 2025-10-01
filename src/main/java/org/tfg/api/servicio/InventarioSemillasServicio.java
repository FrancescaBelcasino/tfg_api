package org.tfg.api.servicio;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tfg.api.excepcion.RecursoNoEncontradoException;
import org.tfg.api.excepcion.SolicitudInvalidaException;
import org.tfg.api.modelo.dto.solicitud.RegistrarSemillaSolicitud;
import org.tfg.api.modelo.entidad.InventarioSemillas;
import org.tfg.api.repositorio.InventarioSemillasRepositorio;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InventarioSemillasServicio {
    private final InventarioSemillasRepositorio inventarioSemillasRepositorio;

    public List<InventarioSemillas> obtenerSemillas(String adminId) {
        return inventarioSemillasRepositorio.findByAdminId(adminId);
    }

    public InventarioSemillas obtenerSemillaPorId(String id, String adminId) {
        return Optional.ofNullable(inventarioSemillasRepositorio.findByIdAndAdminId(id, adminId))
                .orElseThrow(() -> new RecursoNoEncontradoException("Semilla no encontrada con ID: " + id));
    }

    public String registrarSemilla(RegistrarSemillaSolicitud solicitud, String adminId) {
        if (solicitud.getNombre() == null || solicitud.getCantidad() == null) {
            throw new SolicitudInvalidaException("Nombre y cantidad son obligatorios para registrar una semilla.");
        }

        InventarioSemillas inventarioSemillas = inventarioSemillasRepositorio.save(
                InventarioSemillas.builder()
                        .adminId(adminId)
                        .nombre(solicitud.getNombre())
                        .variedad(solicitud.getVariedad())
                        .proveedor(solicitud.getProveedor())
                        .cantidad(solicitud.getCantidad())
                        .fechaAdquisicion(solicitud.getFechaAdquisicion())
                        .fechaExpiracion(solicitud.getFechaExpiracion())
                        .build()
        );
        return inventarioSemillas.getId();
    }

    public String actualizarSemilla(String id, RegistrarSemillaSolicitud solicitud, String adminId) {
        InventarioSemillas semillaExistente = Optional.ofNullable(inventarioSemillasRepositorio.findByIdAndAdminId(id, adminId))
                .orElseThrow(() -> new RecursoNoEncontradoException("Semilla no encontrada con ID: " + id));

        InventarioSemillas actualizada = inventarioSemillasRepositorio.save(
                semillaExistente.toBuilder()
                        .nombre(Optional.ofNullable(solicitud.getNombre()).orElse(semillaExistente.getNombre()))
                        .variedad(Optional.ofNullable(solicitud.getVariedad()).orElse(semillaExistente.getVariedad()))
                        .proveedor(Optional.ofNullable(solicitud.getProveedor()).orElse(semillaExistente.getProveedor()))
                        .cantidad(Optional.ofNullable(solicitud.getCantidad()).orElse(semillaExistente.getCantidad()))
                        .fechaAdquisicion(Optional.ofNullable(solicitud.getFechaAdquisicion()).orElse(semillaExistente.getFechaAdquisicion()))
                        .fechaExpiracion(Optional.ofNullable(solicitud.getFechaExpiracion()).orElse(semillaExistente.getFechaExpiracion()))
                        .build()
        );
        return actualizada.getId();
    }

    public boolean eliminarSemilla(String id, String adminId) {
        InventarioSemillas semilla = Optional.ofNullable(inventarioSemillasRepositorio.findByIdAndAdminId(id, adminId))
                .orElseThrow(() -> new RecursoNoEncontradoException("No se puede eliminar: semilla no encontrada con ID: " + id));

        inventarioSemillasRepositorio.delete(semilla);
        return true;
    }
}
