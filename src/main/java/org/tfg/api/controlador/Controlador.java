package org.tfg.api.controlador;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.tfg.api.modelo.dto.respuesta.IdRespuesta;
import org.tfg.api.modelo.dto.respuesta.ResultadosRespuesta;
import org.tfg.api.modelo.dto.solicitud.*;
import org.tfg.api.modelo.entidad.Campo;
import org.tfg.api.modelo.entidad.InventarioGranos;
import org.tfg.api.modelo.entidad.InventarioSemillas;
import org.tfg.api.modelo.entidad.Parcela;
import org.tfg.api.seguridad.CustomUserDetails;
import org.tfg.api.servicio.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE})
public class Controlador {

    private final UsuarioServicio usuarioServicio;
    private final CampoServicio campoServicio;
    private final ParcelaServicio parcelaServicio;
    private final InventarioSemillasServicio inventarioSemillasServicio;
    private final InventarioGranosServicio inventarioGranosServicio;
    private final RespaldoServicio respaldoServicio;

    private String resolveAdminId(CustomUserDetails principal) {
        if (principal.getRol().equals("ADMINISTRADOR")) {
            return principal.getUsuarioId();
        } else {
            return principal.getAdminId();
        }
    }

    @PostMapping("/usuarios/registrar-usuario")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistrarUsuarioSolicitud solicitud) {
        String id = usuarioServicio.registrarUsuario(solicitud);
        return ResponseEntity.ok(new IdRespuesta(id));
    }

    @PostMapping("/usuarios/iniciar-sesion")
    public ResponseEntity<?> iniciarSesion(@RequestBody IniciarSesionSolicitud solicitud) {
        return usuarioServicio.iniciarSesion(solicitud)
                .map(jwt -> ResponseEntity.ok(java.util.Map.of("token", jwt)))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/usuarios/recuperar-contrasena")
    public ResponseEntity<?> recuperarContrasena(@RequestBody Map<String, String> req) {
        String email = req.get("email");
        usuarioServicio.iniciarRecuperacionContrasena(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/usuarios/cambiar-contrasena")
    public ResponseEntity<?> cambiarContrasena(@RequestBody CambiarContrasenaSolicitud solicitud) {
        usuarioServicio.cambiarContrasenaConToken(solicitud);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/respaldos/data")
    public void ejecutarBackup() {
        respaldoServicio.backupLocalData();
    }

    @GetMapping("/respaldos/github")
    public void ejecutarBackupGitHub() {
        respaldoServicio.syncWithGitHub();
    }

    @GetMapping("/campos")
    public ResponseEntity<?> obtenerCampos(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);

        return ResponseEntity.ok(
                ResultadosRespuesta.<Campo>builder()
                        .resultados(campoServicio.obtenerCampos(adminId))
                        .build()
        );
    }

    @GetMapping("/campos/{id}")
    public ResponseEntity<?> obtenerCampoPorId(@PathVariable String id, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);

        Campo campo = campoServicio.obtenerCampoPorId(id, adminId);
        return ResponseEntity.ok(campo);
    }

    @PostMapping("/campos")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> registrarCampo(@RequestBody RegistrarCampoSolicitud solicitud, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = principal.getUsuarioId();

        String campoId = campoServicio.registrarCampo(solicitud, adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(IdRespuesta.builder().id(campoId).build());
    }

    @PatchMapping("/campos/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> actualizarCampo(@PathVariable String id, @RequestBody RegistrarCampoSolicitud solicitud, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = principal.getUsuarioId();

        String campoId = campoServicio.actualizarCampo(id, solicitud, adminId);
        return ResponseEntity.ok(IdRespuesta.builder().id(campoId).build());
    }

    @DeleteMapping("/campos/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<?> eliminarCampo(@PathVariable String id, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = principal.getUsuarioId();

        campoServicio.eliminarCampo(id, adminId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/parcelas")
    public ResponseEntity<?> obtenerParcelas(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);

        return ResponseEntity.ok(
                ResultadosRespuesta.<Parcela>builder()
                        .resultados(parcelaServicio.obtenerParcelas(adminId))
                        .build()
        );
    }

    @GetMapping("/parcelas/{id}")
    public ResponseEntity<?> obtenerParcelaPorId(@PathVariable String id, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);

        Parcela parcela = parcelaServicio.obtenerParcelaPorId(id, adminId);
        return ResponseEntity.ok(parcela);
    }

    @PostMapping("/parcelas")
    public ResponseEntity<?> registrarParcela(@RequestBody RegistrarParcelaSolicitud solicitud, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);

        String id = parcelaServicio.registrarParcela(solicitud, adminId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(IdRespuesta.builder().id(id).build());
    }

    @PatchMapping("/parcelas/{id}")
    public ResponseEntity<?> actualizarParcela(@PathVariable String id, @RequestBody RegistrarParcelaSolicitud solicitud, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);

        String idActualizada = parcelaServicio.actualizarParcela(id, solicitud, adminId);
        return ResponseEntity.ok(IdRespuesta.builder().id(idActualizada).build());
    }

    @DeleteMapping("/parcelas/{id}")
    public ResponseEntity<?> eliminarParcela(@PathVariable String id, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);

        parcelaServicio.eliminarParcela(id, adminId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/inventarios/semillas")
    public ResponseEntity<?> obtenerSemillas(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);
        return ResponseEntity.ok(
                ResultadosRespuesta.<InventarioSemillas>builder()
                        .resultados(inventarioSemillasServicio.obtenerSemillas(adminId))
                        .build());
    }

    @GetMapping("/inventarios/semillas/{id}")
    public ResponseEntity<?> obtenerSemillaPorId(@PathVariable String id, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);
        InventarioSemillas semilla = inventarioSemillasServicio.obtenerSemillaPorId(id, adminId);
        return ResponseEntity.ok(semilla);
    }

    @PostMapping("/inventarios/semillas")
    public ResponseEntity<?> registrarSemilla(@RequestBody RegistrarSemillaSolicitud solicitud, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);
        String id = inventarioSemillasServicio.registrarSemilla(solicitud, adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(IdRespuesta.builder().id(id).build());
    }

    @PatchMapping("/inventarios/semillas/{id}")
    public ResponseEntity<?> actualizarSemilla(@PathVariable String id, @RequestBody RegistrarSemillaSolicitud solicitud, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);
        String semillaId = inventarioSemillasServicio.actualizarSemilla(id, solicitud, adminId);
        return ResponseEntity.ok(IdRespuesta.builder().id(semillaId).build());
    }

    @DeleteMapping("/inventarios/semillas/{id}")
    public ResponseEntity<?> eliminarSemilla(@PathVariable String id, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);
        inventarioSemillasServicio.eliminarSemilla(id, adminId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/inventarios/granos")
    public ResponseEntity<?> obtenerGranos(Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);
        return ResponseEntity.ok(
                ResultadosRespuesta.<InventarioGranos>builder()
                        .resultados(inventarioGranosServicio.obtenerGranos(adminId))
                        .build());
    }

    @GetMapping("/inventarios/granos/{id}")
    public ResponseEntity<?> obtenerGranoPorId(@PathVariable String id, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);
        InventarioGranos grano = inventarioGranosServicio.obtenerGranoPorId(id, adminId);
        return ResponseEntity.ok(grano);
    }

    @PostMapping("/inventarios/granos")
    public ResponseEntity<?> registrarGrano(@RequestBody RegistrarGranoSolicitud solicitud, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);
        String id = inventarioGranosServicio.registrarGranos(solicitud, adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(IdRespuesta.builder().id(id).build());
    }

    @PatchMapping("/inventarios/granos/{id}")
    public ResponseEntity<?> actualizarGrano(@PathVariable String id, @RequestBody RegistrarGranoSolicitud solicitud, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);
        String granoId = inventarioGranosServicio.actualizarGrano(id, solicitud, adminId);
        return ResponseEntity.ok(IdRespuesta.builder().id(granoId).build());
    }

    @DeleteMapping("/inventarios/granos/{id}")
    public ResponseEntity<?> eliminarGrano(@PathVariable String id, Authentication authentication) {
        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();
        String adminId = resolveAdminId(principal);
        inventarioGranosServicio.eliminarGrano(id, adminId);
        return ResponseEntity.ok().build();
    }
}