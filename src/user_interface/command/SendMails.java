package user_interface.command;

import bin.GMailAPI;
import bin.TextFileOperation;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.stage.Stage;
import user_interface.Browser;
import user_interface.table_datatype.RecieverList;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SendMails implements Command {
    private final ObservableList<RecieverList> mailList;
    private final String subject, body, attachmentPath;

    public SendMails(ObservableList<RecieverList> mailList, String subject, String body, String attachmentPath) {
        this.subject = subject;
        this.body = body;
        this.mailList = mailList;
        this.attachmentPath = attachmentPath;
    }

    @Override
    public void execute() {
//        if (mailList.size() > 0 && !subject.equals("")) {
//            try {
//                List<String> data = new TextFileOperation().read("credential.txt");
//                Map<String, String> credential = new LinkedHashMap<>();
//                for (String line : data) {
//                    String[] keyAndValue = line.split("=");
//                    credential.put(keyAndValue[0], keyAndValue[1]);
//                }
//                GMailAPI gmailAPI = new GMailAPI(credential, subject, body);
//                gmailAPI.obtainToken();
//                gmailAPI.sendEmail(mailList, attachmentPath);
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (URISyntaxException e) {
//                System.out.print("URL EXCEPTION");
//            }
//        }
        List<String> data;
        try {
            data = new TextFileOperation().read("credential.txt");
        } catch (IOException e){
            e.printStackTrace();
            return;
        }
        Map<String, String> credential = new LinkedHashMap<>();
        for (String line : data) {
            String[] keyAndValue = line.split("=");
            credential.put(keyAndValue[0], keyAndValue[1]);
        }
        GMailAPI gmailAPI = new GMailAPI(credential, subject, body);
        Stage browserStage = new Stage();
        new Browser(browserStage, gmailAPI.getRequestURI()).openWindow();
        Runnable listener = new Runnable() {
            @Override
            public void run() {
                String response;
                try {
                    response = gmailAPI.listenForResponse();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        browserStage.close();
                    }
                });
            }
        };
        Thread listenerThread = new Thread(listener);
        listenerThread.start();
    }
}