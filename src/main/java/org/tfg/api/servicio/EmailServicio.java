package org.tfg.api.servicio;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailServicio {
    private static final String FROM = System.getenv("FROM");
    private final JavaMailSender mailSender;

    public void enviarCorreoIntentosFallidos(String para, String urlRecuperacion) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setFrom(FROM);
            helper.setTo(para);
            helper.setSubject("Intentos fallidos de inicio de sesión");
            helper.setText(plantillaIntentosFallidos(urlRecuperacion), true);

            mailSender.send(mensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void enviarCorreoCambioContrasena(String email, String urlRecuperacion) {
        String html = String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                  <meta charset='UTF-8'>
                  <style>
                    .btn-verde {
                      display: inline-block;
                      padding: 12px 24px;
                      background-color: #43a047;
                      color: #fff !important;
                      border-radius: 4px;
                      text-decoration: none;
                      font-weight: 600;
                      font-size: 16px;
                    }
                    .container {
                      max-width: 480px;
                      margin: auto;
                      font-family: Arial, sans-serif;
                      border: 1px solid #e0e0e0;
                      padding: 32px;
                      background: #fbfbfb;
                    }
                    .footer {
                      margin-top: 30px;
                      color: #888;
                      font-size: 12px;
                    }
                  </style>
                </head>
                <body>
                  <div class="container">
                    <h2 style="color: #43a047;">Recuperación de contraseña</h2>
                    <p>Recibimos una solicitud para restablecer tu contraseña.</p>
                    <p style="text-align: center; margin: 32px 0;">
                      <a href='%s' class="btn-verde">Restablecer contraseña</a>
                    </p>
                    <p>Este enlace expirará en 30 minutos.</p>
                    <p>Si no solicitaste este cambio, puedes ignorar este correo.</p>
                    <div class="footer">
                      &copy; %d - Equipo de Soporte
                    </div>
                  </div>
                </body>
                </html>
                """, urlRecuperacion, java.time.Year.now().getValue());
        sendHtmlMail(email, "Recuperá tu contraseña", html);
    }

    public void sendHtmlMail(String to, String subject, String htmlContent) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
            helper.setFrom(FROM);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(mensaje);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String plantillaIntentosFallidos(String urlRecuperacion) {
        return String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                  <meta charset='UTF-8'>
                  <style>
                    .btn-verde {
                      display: inline-block;
                      padding: 12px 24px;
                      background-color: #43a047;
                      color: #fff !important;
                      border-radius: 4px;
                      text-decoration: none;
                      font-weight: 600;
                      font-size: 16px;
                    }
                    .container {
                      max-width: 480px;
                      margin: auto;
                      font-family: Arial, sans-serif;
                      border: 1px solid #e0e0e0;
                      padding: 32px;
                      background: #fbfbfb;
                    }
                    .footer {
                      margin-top: 30px;
                      color: #888;
                      font-size: 12px;
                    }
                  </style>
                </head>
                <body>
                  <div class="container">
                    <h2 style="color: #43a047;">¡Detectamos intentos fallidos de inicio de sesión!</h2>
                    <p>Recibimos <b>5 intentos fallidos</b> de inicio de sesión en tu cuenta.</p>
                    <p>Si no reconoces esta actividad, te recomendamos cambiar la contraseña de inmediato para proteger tu cuenta.</p>
                    <p style="text-align: center; margin: 32px 0;">
                      <a href='%s' class="btn-verde">Cambiar contraseña</a>
                    </p>
                    <div class="footer">
                      &copy; %d - Equipo de Soporte
                    </div>
                  </div>
                </body>
                </html>
                """, urlRecuperacion, java.time.Year.now().getValue());
    }
}