package org.tfg.api.servicio;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class EmailServicio {

    private static final String SENDGRID_API_KEY = System.getenv("MAIL_PASSWORD");
    private static final String FROM = System.getenv("FROM"); // Debe estar verificado en SendGrid

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

        sendEmail(email, "Recuperá tu contraseña", html);
    }

    public void enviarCorreoIntentosFallidos(String para, String urlRecuperacion) {
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
                <h2 style="color: #43a047;">¡Detectamos intentos fallidos de inicio de sesión!</h2>
                <p>Recibimos <b>5 intentos fallidos</b> de inicio de sesión en tu cuenta.</p>
                <p>Por tu seguridad, tu cuenta ha sido <b>bloqueada temporalmente</b>.</p>
                <p>Para recuperar el acceso, debés restablecer tu contraseña.</p>
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

        sendEmail(para, "Intentos fallidos de inicio de sesión", html);
    }

    private void sendEmail(String to, String subject, String htmlContent) {
        Email from = new Email(FROM);
        Email toEmail = new Email(to);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(SENDGRID_API_KEY);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
