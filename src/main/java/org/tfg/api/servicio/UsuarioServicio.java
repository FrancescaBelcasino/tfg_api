package org.tfg.api.servicio;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.tfg.api.modelo.dto.solicitud.CambiarContrasenaSolicitud;
import org.tfg.api.modelo.dto.solicitud.IniciarSesionSolicitud;
import org.tfg.api.modelo.dto.solicitud.RegistrarUsuarioSolicitud;
import org.tfg.api.modelo.entidad.Usuario;
import org.tfg.api.repositorio.UsuarioRepositorio;
import org.tfg.api.seguridad.JwtUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.tfg.api.modelo.entidad.Usuario.Rol.ESTANDAR;

@Service
@RequiredArgsConstructor
public class UsuarioServicio {
    private static final int MAX_INTENTOS = 5;
    private static final String RECOVERY_FRONTEND = System.getenv("RECOVERY_FRONTEND");
    private final UsuarioRepositorio usuarioRepositorio;
    private final EmailServicio emailServicio;
    private final JwtUtil jwtUtil;
    private final Map<String, Integer> intentosFallidos = new HashMap<>();

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
                        .bloqueado(false)
                        .adminId(null)
                        .build()
        );

        return usuario.getId();
    }

    public Optional<String> iniciarSesion(IniciarSesionSolicitud solicitud) {
        return usuarioRepositorio
                .findByEmail(solicitud.getEmail())
                .filter(usuario -> {
                    if (usuario.isBloqueado()) {
                        throw new IllegalStateException("La cuenta está bloqueada. Revisá tu email para restablecer la contraseña.");
                    }

                    boolean valido = BCrypt.checkpw(solicitud.getContrasena(), usuario.getContrasena());
                    if (!valido) {
                        manejarIntentoFallido(solicitud.getEmail());
                    } else {
                        intentosFallidos.remove(solicitud.getEmail());
                    }
                    return valido;
                })
                .map(usuario ->
                        jwtUtil.generateToken(
                                usuario.getId(),
                                usuario.getEmail(),
                                usuario.getRol().name()
                        )
                );
    }

    private void manejarIntentoFallido(String email) {
        intentosFallidos.put(email, intentosFallidos.getOrDefault(email, 0) + 1);
        if (intentosFallidos.get(email) == MAX_INTENTOS) {
            usuarioRepositorio.findByEmail(email).ifPresent(usuario -> {
                usuario.setBloqueado(true);
                String token = UUID.randomUUID().toString();
                usuario.setTokenRecuperacion(token);
                usuario.setTokenExpiracion(LocalDateTime.now().plusMinutes(30));
                usuarioRepositorio.save(usuario);
                String url = RECOVERY_FRONTEND + token;
                emailServicio.enviarCorreoIntentosFallidos(email, url);
            });
        }
    }

    public void iniciarRecuperacionContrasena(String email) {
        usuarioRepositorio.findByEmail(email).ifPresent(usuario -> {
            String token = UUID.randomUUID().toString();
            usuario.setTokenRecuperacion(token);
            usuario.setTokenExpiracion(LocalDateTime.now().plusMinutes(30));
            usuarioRepositorio.save(usuario);
            String url = RECOVERY_FRONTEND + token;
            emailServicio.enviarCorreoCambioContrasena(usuario.getEmail(), url);
        });
    }

    public void cambiarContrasenaConToken(CambiarContrasenaSolicitud solicitud) {
        Usuario usuario = usuarioRepositorio.findByTokenRecuperacion(solicitud.getToken())
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));
        if (usuario.getTokenExpiracion().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token expirado");
        }
        usuario.setContrasena(BCrypt.hashpw(solicitud.getNuevaContrasena(), BCrypt.gensalt()));
        usuario.setTokenRecuperacion(null);
        usuario.setTokenExpiracion(null);
        usuario.setBloqueado(false);
        intentosFallidos.remove(usuario.getEmail());
        usuarioRepositorio.save(usuario);
    }

    private boolean validarContrasena(String contrasena) {
        return contrasena.length() >= 12 &&
                contrasena.matches(".*[A-Z].*") &&
                contrasena.matches(".*[a-z].*") &&
                contrasena.matches(".*\\d.*") &&
                contrasena.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }
}
