package org.tfg.api.servicio;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tfg.api.excepcion.RecursoNoEncontradoException;
import org.tfg.api.excepcion.SolicitudInvalidaException;
import org.tfg.api.modelo.dto.solicitud.RegistrarParcelaSolicitud;
import org.tfg.api.modelo.entidad.Parcela;
import org.tfg.api.repositorio.InventarioSemillasRepositorio;
import org.tfg.api.repositorio.ParcelaRepositorio;
import org.tfg.api.utilidad.Calculos;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ParcelaServicio {

    private final ParcelaRepositorio parcelaRepositorio;
    private final InventarioSemillasRepositorio inventarioSemillasRepositorio;

    public List<Parcela> obtenerParcelas(String adminId) {
        List<Parcela> parcelas = parcelaRepositorio.findByAdminId(adminId);

        for (Parcela p : parcelas) {
            if (p.getSemilla() != null) {
                inventarioSemillasRepositorio.findById(p.getSemilla())
                        .ifPresent(s -> p.setSemillaNombre(s.getNombre()));
            }
        }

        return parcelas;
    }

    public Parcela obtenerParcelaPorId(String id, String adminId) {
        Parcela parcela = Optional.ofNullable(parcelaRepositorio.findByIdAndAdminId(id, adminId))
                .orElseThrow(() -> new RecursoNoEncontradoException("Parcela no encontrada con ID: " + id));

        if (parcela.getSemilla() != null) {
            inventarioSemillasRepositorio.findById(parcela.getSemilla())
                    .ifPresent(s -> parcela.setSemillaNombre(s.getNombre()));
        }

        return parcela;
    }

    public String registrarParcela(RegistrarParcelaSolicitud solicitud, String adminId) {
        List<List<Double>> coordenadas = solicitud.getCoordenadas();
        if (coordenadas == null || coordenadas.size() < 3) {
            throw new SolicitudInvalidaException("Se requieren al menos 3 coordenadas para registrar una parcela.");
        }

        Calculos.Superficie superficie = Calculos.calcularSuperficie(coordenadas);

        Parcela parcela = Parcela.builder()
                .adminId(adminId)
                .campoId(solicitud.getCampoId())
                .nombre(solicitud.getNombre())
                .coordenadas(coordenadas)
                .estado(solicitud.getEstado())
                .caracteristicas(solicitud.getCaracteristicas())
                .semilla(solicitud.getSemilla())
                .cantidadSembrada(solicitud.getCantidadSembrada())
                .superficie(superficie.metrosCuadrados())
                .superficieHa(superficie.hectareas())
                .build();

        return parcelaRepositorio.save(parcela).getId();
    }

    public String actualizarParcela(String id, RegistrarParcelaSolicitud solicitud, String adminId) {
        Parcela parcelaExistente = Optional.ofNullable(parcelaRepositorio.findByIdAndAdminId(id, adminId))
                .orElseThrow(() -> new RecursoNoEncontradoException("Parcela no encontrada con ID: " + id));

        List<List<Double>> nuevasCoordenadas = Optional.ofNullable(solicitud.getCoordenadas())
                .orElse(parcelaExistente.getCoordenadas());

        if (nuevasCoordenadas.size() < 3) {
            throw new SolicitudInvalidaException("Se requieren al menos 3 coordenadas válidas.");
        }

        Calculos.Superficie superficie = Calculos.calcularSuperficie(nuevasCoordenadas);

        Parcela parcelaActualizada = parcelaExistente.toBuilder()
                .nombre(Optional.ofNullable(solicitud.getNombre()).orElse(parcelaExistente.getNombre()))
                .coordenadas(nuevasCoordenadas)
                .estado(Optional.ofNullable(solicitud.getEstado()).orElse(parcelaExistente.getEstado()))
                .caracteristicas(Optional.ofNullable(solicitud.getCaracteristicas()).orElse(parcelaExistente.getCaracteristicas()))
                .semilla(Optional.ofNullable(solicitud.getSemilla()).orElse(parcelaExistente.getSemilla()))
                .cantidadSembrada(Optional.ofNullable(solicitud.getCantidadSembrada()).orElse(parcelaExistente.getCantidadSembrada()))
                .superficie(superficie.metrosCuadrados())
                .superficieHa(superficie.hectareas())
                .build();

        return parcelaRepositorio.save(parcelaActualizada).getId();
    }

    public void eliminarParcela(String id, String adminId) {
        Parcela parcela = Optional.ofNullable(parcelaRepositorio.findByIdAndAdminId(id, adminId))
                .orElseThrow(() -> new RecursoNoEncontradoException("No se puede eliminar: parcela no encontrada con ID: " + id));

        parcelaRepositorio.delete(parcela);
    }
}