package util;

import beans.Usuario;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Correo {

    private String correoEnvia = "coreldrain@gmail.com";
    private String contrasena = "ygirvkkdupzltktl";
    private Usuario usuario;
    private String url;

    public Correo(Usuario usuario , String url) {
        this.usuario = usuario;
        this.url = url;
    }

    public boolean EnviarCorreo() {
        Properties propiedad = new Properties();
        propiedad.setProperty("mail.smtp.host", "smtp.gmail.com");
        propiedad.setProperty("mail.smtp.starttls.enable", "true");
        propiedad.setProperty("mail.smtp.port", "587");
        propiedad.setProperty("mail.smtp.auth", "true");

        Session sesion = Session.getDefaultInstance(propiedad);
        MimeMessage mail = new MimeMessage(sesion);

        try {

            mail.setFrom(new InternetAddress(correoEnvia));
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(usuario.getCorreoPUCP()));
            mail.setSubject("Recuperar contraseña");
            mail.addHeader("Content-Type", "text/html; charset=UTF-8");

            String cuerpo = construirCuerpo();
            cuerpo = cuerpo.replaceAll("@@_NOMBRES_COMPLETOS_@@", usuario.getNombre() + " " + usuario.getApellido());
            cuerpo = cuerpo.replaceAll("@@_URL_@@", url);
            cuerpo = cuerpo.replaceAll("@@_MINUTES_RECUPERAR_@@", Constantes.MINUTOS_RECUPERAR + "");

            mail.setContent(cuerpo, "text/html");

            Transport transporte = sesion.getTransport("smtp");
            transporte.connect(correoEnvia, contrasena);

            transporte.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
            transporte.close();

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public String construirCuerpo() {

        String cuerpo = " <html lang='en'>\n"
                + "<head>\n"
                + "    <meta http-equiv='Content-Type' content='text/html; charset=UTF-8' />\n"
                + "</head>\n"
                + "<body>\n"
                + "    <center>\n"
                + "        <div style='text-align: left; color: rgba(15, 153, 171, 1); width: 670px; box-shadow: 0 4px 8px 0 rgba(0,0,0,0.2);transition: 0.3s'>\n"
                + "            <div style='padding: 2px 16px;'>\n"
                + "                <div style='background:  rgba(15, 153, 171, 1) !important; padding: 10px; '>\n"
                + "                    <table>\n"
                + "                        <tr>\n"
                + "                            <td>\n"
                + "                                <span style='font-size: 24px; \n"
                + "                                font-family:LucidaGrande,tahoma,verdana,arial,sans-serif;\n"
                + "                                color: white;'>Aitel Community </span>\n"
                + "                            </td>\n"
                + "                        </tr>\n"
                + "                    </table>\n"
                + "                </div>\n"
                + "       \n"
                + "                <p>Hola, @@_NOMBRES_COMPLETOS_@@:</p>\n"
                + "                <span style='text-align: justify;'>Recibimos una solicitud para restablecer tu contraseña.<br />"
                + "  Ingresa al url proporcionado para su recuperación:</span><br />\n"
                + "                </span>\n"
                + "              <p style='font-weight: bold;'>Link: <a href='@@_URL_@@' target='_blank'>@@_URL_@@</a></p> \n"
                + "                 <p style='font-style: italic;'>Nota: Recuerda que este url solo es válido por los proximos @@_MINUTES_RECUPERAR_@@ minutos desde el envió a este correo. </p>\n"
                + "                <strong>¿No solicitaste este cambio?</strong>\n"
                + "                <p>Si no solicitaste reestablecer tu contraseña, ignorar este mensaje</p>\n"
                + "            </div>\n"
                + "        </div>\n"
                + "    </center>\n"
                + "</body>\n"
                + "</html>;";

        return cuerpo;
    }
}
