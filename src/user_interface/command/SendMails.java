package user_interface.command;

import bin.GMailAPI;
import bin.TextFileOperation;
import javafx.collections.ObservableList;
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
        if (mailList.size() > 0 && !subject.equals("")) {
            try {
                List<String> data = new TextFileOperation().read("credential.txt");
                Map<String, String> credential = new LinkedHashMap<>();
                for (String line : data) {
                    String[] keyAndValue = line.split("=");
                    credential.put(keyAndValue[0], keyAndValue[1]);
                }
                GMailAPI gmailAPI = new GMailAPI(credential, subject, body);
                gmailAPI.obtainToken();
                gmailAPI.sendEmail(mailList, attachmentPath);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                System.out.print("URL EXCEPTION");
            }
        }
    }
}