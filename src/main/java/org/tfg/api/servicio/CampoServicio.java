package org.tfg.api.servicio;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tfg.api.excepcion.RecursoNoEncontradoException;
import org.tfg.api.excepcion.SolicitudInvalidaException;
import org.tfg.api.modelo.dto.solicitud.RegistrarCampoSolicitud;
import org.tfg.api.modelo.entidad.Campo;
import org.tfg.api.repositorio.CampoRepositorio;
import org.tfg.api.utilidad.Calculos;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CampoServicio {
    private final CampoRepositorio campoRepositorio;

    public List<Campo> obtenerCampos(String adminId) {
        return campoRepositorio.findByAdminId(adminId);
    }

    public Campo obtenerCampoPorId(String id, String adminId) {
        return Optional.ofNullable(campoRepositorio.findByIdAndAdminId(id, adminId))
                .orElseThrow(() -> new RecursoNoEncontradoException("Campo no encontrado con ID: " + id));
    }

    public String registrarCampo(RegistrarCampoSolicitud solicitud, String adminId) {
        if (solicitud.getCoordenadas() == null || solicitud.getCoordenadas().size() < 3) {
            throw new SolicitudInvalidaException("Se requieren al menos 3 coordenadas para registrar un campo.");
        }

        Calculos.Superficie superficie = Calculos.calcularSuperficie(solicitud.getCoordenadas());

        Campo campo = Campo.builder()
                .adminId(adminId)
                .nombre(solicitud.getNombre())
                .coordenadas(solicitud.getCoordenadas())
                .estado(solicitud.getEstado())
                .superficie(superficie.metrosCuadrados())
                .superficieHa(superficie.hectareas())
                .build();

        return campoRepositorio.save(campo).getId();
    }

    public String actualizarCampo(String id, RegistrarCampoSolicitud solicitud, String adminId) {
        Campo campoExistente = Optional.ofNullable(campoRepositorio.findByIdAndAdminId(id, adminId))
                .orElseThrow(() -> new RecursoNoEncontradoException("Campo no encontrado con ID: " + id));

        List<List<Double>> nuevasCoordenadas = Optional.ofNullable(solicitud.getCoordenadas())
                .orElse(campoExistente.getCoordenadas());

        if (nuevasCoordenadas.size() < 3) {
            throw new SolicitudInvalidaException("Se requieren al menos 3 coordenadas válidas.");
        }

        Calculos.Superficie superficie = Calculos.calcularSuperficie(nuevasCoordenadas);

        Campo campoActualizado = campoExistente.toBuilder()
                .nombre(Optional.ofNullable(solicitud.getNombre()).orElse(campoExistente.getNombre()))
                .coordenadas(nuevasCoordenadas)
                .estado(Optional.ofNullable(solicitud.getEstado()).orElse(campoExistente.getEstado()))
                .superficie(superficie.metrosCuadrados())
                .superficieHa(superficie.hectareas())
                .build();

        return campoRepositorio.save(campoActualizado).getId();
    }

    public boolean eliminarCampo(String id, String adminId) {
        Campo campo = Optional.ofNullable(campoRepositorio.findByIdAndAdminId(id, adminId))
                .orElseThrow(() -> new RecursoNoEncontradoException("No se puede eliminar: campo no encontrado con ID: " + id));

        campoRepositorio.delete(campo);
        return true;
    }
}
