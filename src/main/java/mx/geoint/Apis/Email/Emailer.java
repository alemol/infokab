package mx.geoint.Apis.Email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Emailer {

    public Emailer(){}

    public void sendEmail(String zipName, String email){
        final String username = "taantsil@centrogeo.edu.mx";
        final String password = "ya29.a0AXeO80RNu1Erb2pTei0QzLR-t4FyFxZAtZQYG1Dueb8Ns7RqXibsNNtPGiqd0Uzv22pISu02aVX9lgeyqDQz7Rdn9HdWL5N5mshiAIXzTwEX0MYwGDJa4I7PNxJLPGPVtN-sd9ND79AYS4k2AxcgoMdyQx-AY-yzmX-kUAYoaCgYKAZcSARASFQHGX2MiBIJT3LatTN1gThNpeDkN6g0175";

        Properties prop = new Properties();
        prop.put("mail.smtp.ssl.enable", "true"); // required for Gmail
        prop.put("mail.smtp.sasl.enable", "true");
        prop.put("mail.smtp.sasl.mechanisms", "XOAUTH2");
        prop.put("mail.smtp.auth.login.disable", "true");
        prop.put("mail.smtp.auth.plain.disable", "true");

        try {
            Session session = Session.getInstance(prop);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.gmail.com", username, password);

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("taantsil@centrogeo.edu.mx"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(email)
            );
            message.setSubject("Enlace de descarga - taantsil");

            String htmlContent = "<html>\n" +
                    "  <head>\n" +
                    "    <style>\n" +
                    "      #body {\n" +
                    "        width: 50%;\n" +
                    "      }\n" +
                    "      .logo {\n" +
                    "        height: 12rem;\n" +
                    "        padding-bottom: 2rem;\n" +
                    "      }\n" +
                    "      .body-top {\n" +
                    "        text-align: center;\n" +
                    "      }\n" +
                    "      .body-section {\n" +
                    "        padding-bottom: 2rem;\n" +
                    "      }\n" +
                    "      p {\n" +
                    "        margin: 0;\n" +
                    "        padding: 0;\n" +
                    "      }\n" +
                    "      .link-text {\n" +
                    "        font-size: 1.5rem;\n" +
                    "      }\n" +
                    "      a.link-text {\n" +
                    "        padding-bottom: 2rem;\n" +
                    "      }\n" +
                    "      .mid-text {\n" +
                    "        font-size: 1.125rem;\n" +
                    "        line-height: 1.75rem;\n" +
                    "        padding-bottom: 2rem;\n" +
                    "      }\n" +
                    "      .footer {\n" +
                    "        background-color: #303030;\n" +
                    "        text-align: center;\n" +
                    "        padding: 2rem 6rem;\n" +
                    "      }\n" +
                    "      .footer-section {\n" +
                    "        padding-bottom: 2rem;\n" +
                    "        text-align: initial;\n" +
                    "      }\n" +
                    "      .footer-main {\n" +
                    "        color: #ffffff;\n" +
                    "        font-size: 1.125rem;\n" +
                    "        line-height: 1.75rem;\n" +
                    "        padding-bottom: 2rem;\n" +
                    "      }\n" +
                    "      .footer-last {\n" +
                    "        color: #e5e7eb;\n" +
                    "        font-size: 1rem;\n" +
                    "        line-height: 1.5rem;\n" +
                    "      }"+
                    "    </style>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "    <div id=\"body\">\n" +
                    "      <div class=\"body-top\">\n" +
                    "        <img class=\"logo\" src=\"http://taantsil.com.mx/logo_verde.jpg\" />\n" +
                    "        <div class=\"body-section\">\n" +
                    "          <p class=\"link-text\">Tu enlace de descarga es:</p>\n" +
                    "          <a class=\"link-text\" href=\"http://taantsil.com.mx/download/"+zipName+"\"\n" +
                    "            >http://taantsil.com.mx/download/"+zipName+"</a\n" +
                    "          >\n" +
                    "        </div>\n" +
                    "        <div class=\"body-section\">\n" +
                    "          <p class=\"link-text\">Tu código es:</p>\n" +
                    "          <p class=\"link-text\">"+zipName+"</p>\n" +
                    "        </div>\n" +
                    "      </div>\n" +
                    "      <div>\n" +
                    "        <p class=\"mid-text\">\n" +
                    "          Este código y la descarga permanecerán activos durante 30 días. Si\n" +
                    "          tardas más de eso en descargarlo será eliminado y deberás realizar la\n" +
                    "          búsqueda nuevamente.\n" +
                    "        </p>\n" +
                    "        <p class=\"mid-text\">\n" +
                    "          Si no reconoces o no esperabas este correo electrónico, siempre puedes\n" +
                    "          denunciar comportamientos sospechosos a nuestro equipo de asistencia\n" +
                    "          enviándo un correo a\n" +
                    "          <a href=\"mailto:taantsil@centrogeo.edu.mx\"\n" +
                    "            >taantsil@centrogeo.edu.mx</a\n" +
                    "          >\n" +
                    "          indicando el código proporcionado.\n" +
                    "        </p>\n" +
                    "      </div>\n" +
                    "      <div class=\"footer\">\n" +
                    "        <img class=\"logo\" src=\"http://taantsil.com.mx/logo3.png\" />\n" +
                    "        <div class=\"footer-section\">\n" +
                    "          <p class=\"footer-main\">\n" +
                    "            Esta herramienta ha sido desarrollada en el Centro de\n" +
                    "            Investigación en Ciencias de Información Geoespacial y financiada\n" +
                    "            por W.K. Kellogg Foundation mediante el proyecto P-6005156-2021\n" +
                    "            “Desarrollo de tecnologías de la información para el corpus\n" +
                    "            lingüístico del maya yucateco “ (Development of Information\n" +
                    "            Technologies for the Linguistic Corpus of the Maya Language of\n" +
                    "            Yucatan).\n" +
                    "          </p>\n" +
                    "          <p class=\"footer-last\">\n" +
                    "            Parque Científico Tecnológico Yucatán (PCTY). Carretera Sierra\n" +
                    "            Papacal, Chuburná Puerto, Sierra Papacal, CP. 97302, Mérida,\n" +
                    "            Yucatán, México .\n" +
                    "          </p>\n" +
                    "        </div>\n" +
                    "      </div>"+
                    "    </div>\n" +
                    "  </body>\n" +
                    "</html>\n";
            message.setContent(htmlContent, "text/html; charset=utf-8");
            message.saveChanges();

            transport.send(message, message.getAllRecipients());

            System.out.println("Done");

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
