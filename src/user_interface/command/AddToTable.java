package user_interface.command;

import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import user_interface.table_datatype.RecieverList;

import java.util.regex.Pattern;

public class AddToTable implements Command {
    private ObservableList<RecieverList> data;
    private TextField name, email, attachment;

    public AddToTable(ObservableList<RecieverList> data, TextField name, TextField email, TextField attachment) {
        this.data = data;
        this.name = name;
        this.email = email;
        this.attachment = attachment;
    }

    @Override
    public void execute() {
        String nameValue = name.getText().trim(),
                emailValue = email.getText().trim(),
                attachmentValue = attachment.getText().trim();
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        if (pattern.matcher(emailValue).matches()) {
            data.add(new RecieverList(nameValue, emailValue, attachmentValue));
            name.clear();
            email.clear();
            attachment.clear();
        }
    }
}