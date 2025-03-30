package org.tfg.api.servicio;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tfg.api.modelo.dto.solicitud.RegistrarCampoSolicitud;
import org.tfg.api.modelo.entidad.Campo;
import org.tfg.api.repositorio.CampoRepositorio;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class CampoServicio {
    private CampoRepositorio campoRepositorio;

    public List<Campo> mostrarCampos() {
        return campoRepositorio.findAll();
    }

    public Campo mostrarCampo(String id) {
        return campoRepositorio.findById(id).orElse(null);
    }

    public String registrarCampo(RegistrarCampoSolicitud solicitud) {
        Campo campo = campoRepositorio.save(
                Campo.builder()
                        .nombre(solicitud.getNombre())
                        .coordenadas(solicitud.getCoordenadas())
                        .estado(solicitud.getEstado())
                        .superficie(solicitud.getSuperficie())
                        .build()
        );

        return campo.getId();
    }

    public String actualizarCampo(String id, RegistrarCampoSolicitud solicitud) {
        return campoRepositorio.findById(id)
                .map(campo -> campo.toBuilder()
                        .nombre(solicitud.getNombre() == null ? campo.getNombre() : solicitud.getNombre())
                        .coordenadas(solicitud.getCoordenadas() == null ? campo.getCoordenadas() : solicitud.getCoordenadas())
                        .estado(solicitud.getEstado() == null ? campo.getEstado() : solicitud.getEstado())
                        .superficie(solicitud.getSuperficie() == null ? campo.getSuperficie() : solicitud.getSuperficie())
                        .build())
                .map(campo -> campoRepositorio.save(campo))
                .map(Campo::getId)
                .orElse(null);
    }

    public boolean eliminarCampo(String id) {
        Optional<Campo> optionalCampo = campoRepositorio.findById(id);

        if (!optionalCampo.isPresent()) {
            return false;
        }

        campoRepositorio.deleteById(id);

        return true;
    }
}
