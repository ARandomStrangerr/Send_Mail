package user_interface.command;

import javafx.scene.control.TableView;
import user_interface.table_datatype.RecieverList;

public class DeleteEntryOfTable implements Command{
    private TableView<RecieverList> table;

    public DeleteEntryOfTable(TableView<RecieverList> table) {
        this.table = table;
    }

    @Override
    public void execute() {
        table.getItems().remove(table.getSelectionModel().getSelectedItem());
    }
}
