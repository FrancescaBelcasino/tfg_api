package org.tfg.api.servicio;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.tfg.api.modelo.dto.solicitud.IniciarSesionSolicitud;
import org.tfg.api.modelo.dto.solicitud.RegistrarUsuarioSolicitud;
import org.tfg.api.modelo.entidad.Usuario;
import org.tfg.api.repositorio.UsuarioRepositorio;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioServicio {
    private UsuarioRepositorio usuarioRepositorio;

    public String registrarUsuario(RegistrarUsuarioSolicitud solicitud) {
        Usuario usuario = usuarioRepositorio.save(
                Usuario.builder()
                        .email(solicitud.getEmail())
                        .contrasena(solicitud.getContrasena())
                        .build()
        );

        return usuario.getId();
    }


    public Optional<String> iniciarSesion(IniciarSesionSolicitud solicitud) {
        return usuarioRepositorio
                .findByEmail(solicitud.getEmail())
                .filter(usuario -> usuario.getContrasena().equals(solicitud.getContrasena()))
                .map(Usuario::getId);
    }
}
