package user_interface.command;

import bin.ExcelOperation;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import user_interface.table_datatype.RecieverList;

import java.io.IOException;

public class OpenAndReadReceiverExcelFile implements Command {
    private ObservableList<RecieverList> list;

    public OpenAndReadReceiverExcelFile(ObservableList<RecieverList> list) {
        this.list = list;
    }

    @Override
    public void execute() {
        FileChooser fileChooser = new FileChooser();
        try {
            for (String line : new ExcelOperation().read(fileChooser.showOpenDialog(new Stage()).getAbsolutePath())) {
                String[] data = line.split(":");
                list.add(new RecieverList(data[0].trim(), data[1].trim(), data[2].trim()));
            }
        } catch (IOException e) {
            System.out.print("File Not Found");
        }
    }
}