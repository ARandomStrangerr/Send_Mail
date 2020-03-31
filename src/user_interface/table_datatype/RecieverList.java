package user_interface.table_datatype;

import javafx.beans.property.SimpleStringProperty;

public class RecieverList {
    private final SimpleStringProperty name, email, attachment;

    public RecieverList(String name, String email, String attachment){
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.attachment = new SimpleStringProperty(attachment);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public String getAttachment() {
        return attachment.get();
    }

    public SimpleStringProperty attachmentProperty() {
        return attachment;
    }
}
