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

import java.time.LocalDate;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*", methods = {GET, POST, PATCH})

public class Controlador {
    private UsuarioServicio usuarioServicio;
    private InventarioSemillasServicio inventarioSemillasServicio;
    private InventarioGranosServicio inventarioGranosServicio;

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

    @PostMapping("/inventarios/semillas/registrar-semillas")
    public ResponseEntity<IdRespuesta> registrarSemillas(@RequestBody RegistrarSemillasSolicitud solicitud) {
        String id = inventarioSemillasServicio.registrarSemillas(solicitud);

        return ResponseEntity.ok(new IdRespuesta(id));
    }

    @PostMapping("/inventarios/granos/registrar-granos")
    public ResponseEntity<IdRespuesta> registrarGranos(@RequestBody RegistrarGranosSolicitud solicitud) {
        String id = inventarioGranosServicio.registrarGranos(solicitud);

        return ResponseEntity.ok(new IdRespuesta(id));
    }

    @GetMapping("/inventarios/semillas")
    public ResponseEntity<ResultadosRespuesta> mostrarSemillas() {
        List<InventarioSemillas> semillas = inventarioSemillasServicio.mostrarSemillas();

        return ResponseEntity.ok(new ResultadosRespuesta(semillas));
    }

    @GetMapping("/inventarios/granos")
    public ResponseEntity<ResultadosRespuesta> mostrarGranos() {
        List<InventarioGranos> granos = inventarioGranosServicio.mostrarGranos();

        return ResponseEntity.ok(new ResultadosRespuesta(granos));
    }

    // Endpoint para obtener las semillas por año de adquisición
    @GetMapping("/reportes/semillas-por-anio/{anio}")
    public List<InventarioSemillas> obtenerSemillasPorAnio(@PathVariable int anio) {
        return inventarioSemillasServicio.obtenerSemillasPorAnio(anio);
    }

    // Endpoint para obtener las semillas por rango de fechas
    @GetMapping("/reportes/semillas-por-fechas")
    public List<InventarioSemillas> obtenerSemillasPorRangoFechas(@RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return inventarioSemillasServicio.obtenerSemillasPorRangoFechas(start, end);
    }

    // Endpoint para obtener las semillas por año de adquisición
    @GetMapping("/reportes/granos-por-anio/{anio}")
    public List<InventarioGranos> obtenerGranosPorAnio(@PathVariable int anio) {
        return inventarioGranosServicio.obtenerGranosPorAnio(anio);
    }

    // Endpoint para obtener las semillas por rango de fechas
    @GetMapping("/reportes/granos-por-fechas")
    public List<InventarioGranos> obtenerGranosPorRangoFechas(@RequestParam String startDate, @RequestParam String endDate) {
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        return inventarioGranosServicio.obtenerGranosPorRangoFechas(start, end);
    }


}
