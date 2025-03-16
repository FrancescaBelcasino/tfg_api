package org.tfg.api.servicio;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.tfg.api.modelo.dto.solicitud.IniciarSesionSolicitud;
import org.tfg.api.modelo.dto.solicitud.RegistrarUsuarioSolicitud;
import org.tfg.api.modelo.entidad.Usuario;
import org.tfg.api.repositorio.UsuarioRepositorio;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.tfg.api.modelo.entidad.Usuario.Rol.ESTANDAR;

@Service
@AllArgsConstructor
public class UsuarioServicio {
    private UsuarioRepositorio usuarioRepositorio;
    private Map<String, Integer> intentosFallidos = new HashMap<>();
    private static final int MAX_INTENTOS = 5;

    public String registrarUsuario(RegistrarUsuarioSolicitud solicitud) {
        if (!validarContrasena(solicitud.getContrasena())) {
            throw new IllegalArgumentException("La contraseña no cumple con los requisitos de seguridad");
        }

        String contrasenaCifrada = BCrypt.hashpw(solicitud.getContrasena(), BCrypt.gensalt());

        Usuario usuario = usuarioRepositorio.save(
                Usuario.builder()
                        .email(solicitud.getEmail())
                        .contrasena(contrasenaCifrada)
                        .rol(ESTANDAR)
                        .build()
        );

        return usuario.getId();
    }

    public Optional<String> iniciarSesion(IniciarSesionSolicitud solicitud) {
        return usuarioRepositorio
                .findByEmail(solicitud.getEmail())
                .filter(usuario -> {
                    boolean valido = BCrypt.checkpw(solicitud.getContrasena(), usuario.getContrasena());
                    if (!valido) {
                        manejarIntentoFallido(solicitud.getEmail());
                    } else {
                        intentosFallidos.remove(solicitud.getEmail());
                    }
                    return valido;
                })
                .map(Usuario::getId);
    }

    private void manejarIntentoFallido(String email) {
        intentosFallidos.put(email, intentosFallidos.getOrDefault(email, 0) + 1);
        if (intentosFallidos.get(email) >= MAX_INTENTOS) {
            enviarCorreoRecuperacion(email);
        }
    }

    private void enviarCorreoRecuperacion(String email) {
        System.out.println("Correo de recuperación enviado a: " + email);
    }

    private boolean validarContrasena(String contrasena) {
        return contrasena.length() >= 12 &&
                contrasena.matches(".*[A-Z].*") &&
                contrasena.matches(".*[a-z].*") &&
                contrasena.matches(".*\\d.*") &&
                contrasena.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }
}
