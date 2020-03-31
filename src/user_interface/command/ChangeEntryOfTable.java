package user_interface.command;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import user_interface.table_datatype.RecieverList;

public class ChangeEntryOfTable implements Command {
    private TextField name, email, attachment;
    private TableView<RecieverList> table;

    public ChangeEntryOfTable(TextField name, TextField email, TextField attachment, TableView<RecieverList> table) {
        this.name = name;
        this.email = email;
        this.attachment = attachment;
        this.table = table;
    }

    @Override
    public void execute() {
        int index = table.getSelectionModel().getSelectedIndex();
        name.setText(table.getItems().get(index).getName());
        email.setText(table.getItems().get(index).getEmail());
        attachment.setText(table.getItems().get(index).getAttachment());
        new DeleteEntryOfTable(table).execute();
    }
}
