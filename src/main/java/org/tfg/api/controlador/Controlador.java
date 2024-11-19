package org.tfg.api.controlador;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tfg.api.modelo.dto.respuesta.IdRespuesta;
import org.tfg.api.modelo.dto.respuesta.ResultadosRespuesta;
import org.tfg.api.modelo.dto.solicitud.IniciarSesionSolicitud;
import org.tfg.api.modelo.dto.solicitud.RegistrarGranosSolicitud;
import org.tfg.api.modelo.dto.solicitud.RegistrarSemillasSolicitud;
import org.tfg.api.modelo.dto.solicitud.RegistrarUsuarioSolicitud;
import org.tfg.api.modelo.entidad.InventarioGranos;
import org.tfg.api.modelo.entidad.InventarioSemillas;
import org.tfg.api.servicio.InventarioGranosServicio;
import org.tfg.api.servicio.InventarioSemillasServicio;
import org.tfg.api.servicio.UsuarioServicio;

import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", methods = {GET, POST, PATCH})

public class Controlador {
    private UsuarioServicio usuarioServicio;
    private InventarioSemillasServicio inventarioSemillasServicio;
    private InventarioGranosServicio inventarioGranosServicio;

    // Endpoints para registro e inicio de sesión genéricos

    @PostMapping("/usuarios/registrar-usuario")
    public ResponseEntity<IdRespuesta> registrarUsuario(@RequestBody RegistrarUsuarioSolicitud solicitud) {
        String id = usuarioServicio.registrarUsuario(solicitud);

        return ResponseEntity.ok(new IdRespuesta(id));
    }

    @PostMapping("/usuarios/iniciar-sesion")
    public ResponseEntity<IdRespuesta> iniciarSesion(@RequestBody IniciarSesionSolicitud solicitud) {
        return usuarioServicio.iniciarSesion(solicitud)
                .map(id -> ResponseEntity.ok(new IdRespuesta(id)))
                .orElse(ResponseEntity.badRequest().build());
    }

    // Endpoints para Inventario de Semillas

    @PostMapping("/inventarios/semillas")
    public ResponseEntity<IdRespuesta> registrarSemillas(@RequestBody RegistrarSemillasSolicitud solicitud) {
        String id = inventarioSemillasServicio.registrarSemillas(solicitud);

        return ResponseEntity.ok(new IdRespuesta(id));
    }

    @GetMapping("/inventarios/semillas")
    public ResponseEntity<ResultadosRespuesta> mostrarSemillas() {
        List<InventarioSemillas> semillas = inventarioSemillasServicio.mostrarSemillas();

        return ResponseEntity.ok(new ResultadosRespuesta(semillas));
    }

    @GetMapping("/inventarios/semillas/{id}")
    public ResponseEntity<ResultadosRespuesta> mostrarSemilla(@PathVariable String id) {

        InventarioSemillas semilla = inventarioSemillasServicio.mostrarSemilla(id);

        if (semilla == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(new ResultadosRespuesta(singletonList(semilla)));
    }

    @PatchMapping("/inventarios/semillas/{id}")
    public ResponseEntity<IdRespuesta> actualizarSemilla(@PathVariable String id, @RequestBody RegistrarSemillasSolicitud solicitud) {
        return Optional.ofNullable(inventarioSemillasServicio.actualizarSemilla(id, solicitud))
                .map(idSemilla -> ResponseEntity.ok(new IdRespuesta(idSemilla)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/inventarios/semillas/{id}")
    public ResponseEntity<ResultadosRespuesta> eliminarSemilla(@PathVariable String id) {
        boolean semillaEliminada = inventarioSemillasServicio.eliminarSemilla(id);

        if (!semillaEliminada) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();

    }

    // Endpoints para inventario de granos

    @PostMapping("/inventarios/granos")
    public ResponseEntity<IdRespuesta> registrarGranos(@RequestBody RegistrarGranosSolicitud solicitud) {
        String id = inventarioGranosServicio.registrarGranos(solicitud);

        return ResponseEntity.ok(new IdRespuesta(id));
    }

    @GetMapping("/inventarios/granos")
    public ResponseEntity<ResultadosRespuesta> mostrarGranos() {
        List<InventarioGranos> granos = inventarioGranosServicio.mostrarGranos();

        return ResponseEntity.ok(new ResultadosRespuesta(granos));
    }

    @GetMapping("/inventarios/granos/{id}")
    public ResponseEntity<ResultadosRespuesta> mostrarGrano() {

        return ResponseEntity.ok(new ResultadosRespuesta(null));
    }

    @PatchMapping("/inventarios/granos/{id}")
    public ResponseEntity<IdRespuesta> actualizarGrano(@PathVariable String id, @RequestBody RegistrarGranosSolicitud solicitud) {
        return Optional.ofNullable(inventarioGranosServicio.actualizarGrano(id, solicitud))
                .map(idg -> ResponseEntity.ok(new IdRespuesta(idg)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/inventarios/granos/{id}")
    public ResponseEntity<ResultadosRespuesta> eliminarGrano() {

        return ResponseEntity.ok(new ResultadosRespuesta(null));
    }
}
