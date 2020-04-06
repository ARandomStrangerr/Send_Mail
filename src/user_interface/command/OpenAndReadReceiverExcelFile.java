package user_interface.command;

import bin.ExcelOperation;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import user_interface.table_datatype.RecieverList;

import java.io.IOException;

public class OpenAndReadReceiverExcelFile implements Command {
    private TextField textField;
    private ObservableList<RecieverList> list;

    public OpenAndReadReceiverExcelFile(TextField textField, ObservableList<RecieverList> list) {
        this.textField = textField;
        this.list = list;
    }

    @Override
    public void execute() {
        FileChooser fileChooser = new FileChooser();
        textField.setText(fileChooser.showOpenDialog(new Stage()).getAbsolutePath());
        try {
            for (String line : new ExcelOperation().read(textField.getText())) {
                String[] data = line.split(":");
                list.add(new RecieverList(data[0].trim(), data[1].trim(), data[2].trim()));
            }
        } catch (IOException e) {
            System.out.print("File Not Found");
        }
    }
}