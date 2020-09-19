package bin;

import javafx.collections.ObservableList;
import user_interface.table_datatype.RecieverList;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

public class GMailAPI {
    private Map<String, String> credential, contendType;
    private final String subject, body;

    public GMailAPI(Map<String, String> credential, String subject, String body) {
        this.credential = credential;
        this.subject = subject;
        this.body = body;
        contendType = new LinkedHashMap<>();
    }

    public void obtainToken() throws URISyntaxException, IOException {
        //Step 1: send request to Google's Auth 2.0 server
        String address = "https://accounts.google.com/o/oauth2/v2/auth?"
                + generateParameters("client_id", "redirect_uri", "response_type", "scope");

        //Open above address in a browser
        Desktop.getDesktop().browse(new URI(address));

        //Get the Auth 2.0 server response
        ServerSocket serverSocket = new ServerSocket(4444);
        Socket socket = serverSocket.accept();
        String response = getInputString(socket.getInputStream());
        socket.close();
        serverSocket.close();

        //Handle the Auth 2.0 server response / get authorization code
        if (!response.contains("access_denied")) {
            this.credential.put("code", response.substring(response.indexOf("=") + 1, response.indexOf("&")));
        } else {
            return;
        }

        //Step 2: exchange authorization code for access / refresh token
        address = "https://oauth2.googleapis.com/token";
        String param = generateParameters("code", "client_id", "client_secret", "redirect_uri", "grant_type");

        //Send authorization code to Google API and get Access/Refresh Token
        response = connectToURI(address, param.getBytes());

        //extract Access/Refresh Token
        String[] extractedParameter = response.replace("\\u007D", "")
                .replaceAll("\\u007B", "")
                .replaceAll("\"", "")
                .split(",");
        for (String parameter : extractedParameter) {
            if (parameter.contains("access_token") || parameter.contains("refresh_token")) {
                String[] parameterArr = parameter.split(":");
                credential.put(parameterArr[0].trim(), parameterArr[1].trim());
            }
        }
    }

    public void sendEmail(ObservableList<RecieverList> receiverLists, String attachmentFolder) throws IOException {
        //initiate key and value of contentType
        contendType.put(".csv", "text/csv");
        contendType.put(".doc", "application/msword");
        contendType.put(".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        contendType.put(".epub", "application/epub+zip");
        contendType.put(".gz", "application/gzip");
        contendType.put(".jar", "application/java-archive");
        contendType.put(".jpeg", "image/jpeg");
        contendType.put(".jpg", "image/jpeg");
        contendType.put(".xls", "application/vnd.ms-excel");
        contendType.put(".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        //create message
        String body = new StringBuilder("--foo_bar_baz\n" +
                "Content-Type: text/plain; charset=\"UTF-8\"\n" +
                "MIME-Version: 1.0\n" +
                "Content-Transfer-Encoding: 7bit\n")
                .append("\n")
                .append(this.body)
                .append("\n")
                .toString();
        String address = "https://www.googleapis.com/upload/gmail/v1/users/me/messages/send?" +
                "uploadType=multipart&" +
                generateParameters("access_token");

        //loop for each person in
        for (RecieverList receiver : receiverLists) {
            StringBuilder entireMessage = new StringBuilder();
            entireMessage.append("{\n")
                    .append("\"raw\":\"\n")
                    .append("Content-Type: multipart/mixed; boundary=\"foo_bar_baz\"\n")
                    .append("MIME-Version: 1.0\n")
                    .append("to: ").append(receiver.getEmail()).append("\n")
                    .append("subject: =?UTF-8?B?").append(Base64.getEncoder().encodeToString(subject.getBytes()))
                    .append("?=\n")
                    .append("\n")
                    .append(body)
                    .append("\n");
            if (!receiver.getAttachment().equals("")) {
                entireMessage.append("--foo_bar_baz\n")
                        .append("Content-Type:").append(contendType.get(receiver.getAttachment()
                        .substring(receiver.getAttachment().indexOf(".")))).append("\n")
                        .append("MIME-Version: 1.0\n")
                        .append("Content-Transfer-Encoding: base64\n")
                        .append("Content-Disposition: attachment; filename=").append("\"")
                        .append(receiver.getAttachment()).append("\"\n")
                        .append("\n")
                        .append(encodeFileToBase64(attachmentFolder + receiver.getAttachment())).append("\n")
                        .append("\n")
                        .append("--foo_bar_baz--");
            }
            entireMessage.append("--foo_bar_baz--\n")
                    .append("\"\n")
                    .append("}");

            //TEST
            try {
                HttpURLConnection con = (HttpURLConnection) new URL(address).openConnection();
                con.setRequestMethod("POST");
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setRequestProperty("Content-Type", "message/rfc822");
                con.getOutputStream().write(entireMessage.toString().getBytes());
                getInputString(con.getInputStream());
            } catch (Exception ignore){}
            //END TEST
        }
    }

    private String encodeFileToBase64(String filePath) throws IOException {
        File file = new File(filePath);
        byte[] byteArr = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(byteArr);
        return Base64.getEncoder().encodeToString(byteArr);
    }

    private String connectToURI(String uri, byte[] data) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(uri).openConnection();
        con.setRequestMethod("POST");
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.getOutputStream().write(data);
        return getInputString(con.getInputStream());
    }

    private String generateParameters(String... params) {
        StringBuilder sb = new StringBuilder();
        for (String param : params) {
            if (sb.length() != 0) {
                sb.append("&");
            }
            sb.append(param).append("=").append(this.credential.get(param));
        }
        return sb.toString();
    }

    private String getInputString(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        for (String line = br.readLine(); line != null && !line.equals("Connection: keep-alive");
             line = br.readLine()) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }
}
