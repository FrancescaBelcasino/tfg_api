package org.tfg.api.servicio;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Service
public class RespaldoServicio {
    private static final Logger logger = Logger.getLogger(RespaldoServicio.class.getName());
    private static final String SOURCE_DIR = "C:/Sistema de Gestión Agropecuaria";
    private static final String BACKUP_DIR = "C:/respaldos/";

    @Scheduled(cron = "0 0 23 * * ?")
    public void backupLocalData() {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));
            Path sourcePath = Paths.get(SOURCE_DIR);
            Path backupPath = Paths.get(BACKUP_DIR, "backup_" + timestamp);

            Files.createDirectories(backupPath);

            Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    Path targetDir = backupPath.resolve(sourcePath.relativize(dir));
                    Files.createDirectories(targetDir);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Path targetFile = backupPath.resolve(sourcePath.relativize(file));
                    Files.copy(file, targetFile, StandardCopyOption.REPLACE_EXISTING);
                    return FileVisitResult.CONTINUE;
                }
            });

            logger.info("✅ Copia de seguridad creada en: " + backupPath);
        } catch (IOException e) {
            logger.severe("❌ Error en el respaldo: " + e.getMessage());
        }
    }

    @Scheduled(cron = "0 0 19 ? * FRI")
    public void syncWithGitHub() {
        try {
            ProcessBuilder pb = new ProcessBuilder(
                    "cmd.exe", "/c",
                    "cd C:/proyecto && git add . && git commit -m \"Backup semanal\" && git push origin main"
            );
            pb.redirectErrorStream(true);
            Process process = pb.start();
            process.waitFor();

            logger.info("✅ Código sincronizado con GitHub.");
        } catch (IOException | InterruptedException e) {
            logger.severe("❌ Error en sincronización con GitHub: " + e.getMessage());
        }
    }
}
