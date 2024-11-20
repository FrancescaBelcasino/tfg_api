package org.tfg.api.servicio;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tfg.api.modelo.dto.solicitud.RegistrarParcelaSolicitud;
import org.tfg.api.modelo.entidad.Parcela;
import org.tfg.api.repositorio.ParcelaRepositorio;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor

public class ParcelaServicio {
    private ParcelaRepositorio parcelaRepositorio;

    public String registrarParcela(RegistrarParcelaSolicitud solicitud) {
        Parcela parcela = parcelaRepositorio.save(
                Parcela.builder()
                        .nombre(solicitud.getNombre())
                        .latitud(solicitud.getLatitud())
                        .longitud(solicitud.getLongitud())
                        .estado(solicitud.getEstado())
                        .campoId(solicitud.getCampoId())
                        .superficie(solicitud.getSuperficie())
                        .build()
        );

        return parcela.getId();
    }

    public List<Parcela> mostrarParcelas() {
        return parcelaRepositorio.findAll();
    }

    public Parcela mostrarParcela(String id) {
        return parcelaRepositorio.findById(id).orElse(null);
    }

    public String actualizarParcela(String id, RegistrarParcelaSolicitud solicitud) {
        return parcelaRepositorio.findById(id)
                .map(parcela -> parcela.toBuilder()
                        .nombre(solicitud.getNombre())
                        .latitud(solicitud.getLatitud())
                        .longitud(solicitud.getLongitud())
                        .estado(solicitud.getEstado())
                        .campoId(solicitud.getCampoId())
                        .superficie(solicitud.getSuperficie())
                        .build())
                .map(parcela -> parcelaRepositorio.save(parcela))
                .map(Parcela::getId)
                .orElse(null);
    }

    public boolean eliminarParcela(String id) {
        Optional<Parcela> optionalParcela = parcelaRepositorio.findById(id);

        if (!optionalParcela.isPresent()) {
            return false;
        }

        parcelaRepositorio.deleteById(id);

        return true;
    }
}
