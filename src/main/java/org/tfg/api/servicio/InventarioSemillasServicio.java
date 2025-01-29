package org.tfg.api.servicio;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tfg.api.modelo.dto.solicitud.RegistrarSemillasSolicitud;
import org.tfg.api.modelo.entidad.InventarioSemillas;
import org.tfg.api.repositorio.InventarioSemillasRepositorio;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InventarioSemillasServicio {
    private InventarioSemillasRepositorio inventarioSemillasRepositorio;

    public String registrarSemillas(RegistrarSemillasSolicitud solicitud) {
        InventarioSemillas inventarioSemillas = inventarioSemillasRepositorio.save(
                InventarioSemillas.builder()
                        .variedad(solicitud.getVariedad())
                        .proveedor(solicitud.getProveedor())
                        .cantidad(solicitud.getCantidad())
                        .fechaAdquisicion(solicitud.getFechaAdquisicion())
                        .fechaExpiracion(solicitud.getFechaExpiracion())
                        .build()
        );

        return inventarioSemillas.getId();
    }

    public List<InventarioSemillas> mostrarSemillas() {
        return inventarioSemillasRepositorio.findAll();
    }

    public InventarioSemillas mostrarSemilla(String id) {
        return inventarioSemillasRepositorio.findById(id).orElse(null);
    }

    public String actualizarSemilla(String id, RegistrarSemillasSolicitud solicitud) {
        return inventarioSemillasRepositorio.findById(id)
                .map(semilla -> semilla.toBuilder()
                        .variedad(solicitud.getVariedad())
                        .proveedor(solicitud.getProveedor())
                        .cantidad(solicitud.getCantidad())
                        .fechaAdquisicion(solicitud.getFechaAdquisicion())
                        .fechaExpiracion(solicitud.getFechaExpiracion())
                        .build())
                .map(semilla -> inventarioSemillasRepositorio.save(semilla))
                .map(InventarioSemillas::getId)
                .orElse(null);
    }

    public boolean eliminarSemilla(String id) {
        Optional<InventarioSemillas> optionalSemilla = inventarioSemillasRepositorio.findById(id);

        if (!optionalSemilla.isPresent()) {
            return false;
        }

        inventarioSemillasRepositorio.deleteById(id);

        return true;
    }

}
