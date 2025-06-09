package mx.geoint.Apis.Email;

import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Base64;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.*;

public class GmailSender {

    private static final String APPLICATION_NAME = "Gmail API Java Send Mail";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private Gmail service;

    public GmailSender() throws Exception {
        this.service = getGmailService();
    }

    public static Gmail getGmailService() throws Exception {
        Credential credential = authorize();
        return new Gmail.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static Credential authorize() throws Exception {
        System.out.println("Tokens path: " + new File(TOKENS_DIRECTORY_PATH).getAbsolutePath());
        InputStream in = GmailSender.class.getResourceAsStream(CREDENTIALS_FILE_PATH);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                new GoogleClientSecrets().load(JSON_FACTORY, new InputStreamReader(in)),
                Collections.singleton(GmailScopes.GMAIL_SEND)
        ).setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public MimeMessage createEmail(String to, String zipName) throws MessagingException {
        Properties props = System.getProperties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("taantsil@centrogeo.edu.mx"));
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(to));
        email.setSubject("Enlace de descarga - taantsil");

        String htmlContent = "<html>\n" +
                "  <head>\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"+
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
                "        padding: 2rem 10%;\n" +
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
                "   <div id=\"body\" style=\"max-width: 600px; margin: 0 auto; width: 100%;\">\n"+
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
                "        <img src=\"http://taantsil.com.mx/logo3.png\" style=\"display: block; margin: 0 auto; max-width: 100%; height: auto;\" />\n"+
                "        <div class=\"footer-section\">\n" +
                "          <p class=\"footer-main\">\n" +
                "            Esta herramienta ha sido desarrollada en el Centro de\n" +
                "            Investigación en Ciencias de Información Geoespacial y financiada\n" +
                "            por W.K. Kellogg Foundation mediante el proyecto P-6005156-2021\n" +
                "            \"Desarrollo de tecnologías de la información para el corpus\n" +
                "            lingüístico del maya yucateco\" (Development of Information\n" +
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

        MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(htmlContent, "text/html");

        MimeMultipart multipart = new MimeMultipart();
        multipart.addBodyPart(textPart);

        email.setContent(multipart);

        return email;
    }

    public void sendMessage(String userId, MimeMessage email) throws IOException, MessagingException {
        Message message = createMessageWithEmail(email);
        Message sentMessage = service.users().messages().send(userId, message).execute();
        System.out.println("Message sent with ID: " + sentMessage.getId());
    }

    private Message createMessageWithEmail(MimeMessage email) throws MessagingException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        email.writeTo(baos);
        byte[] bytes = baos.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(bytes);

        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
}