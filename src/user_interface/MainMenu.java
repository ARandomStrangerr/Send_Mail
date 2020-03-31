package user_interface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import user_interface.command.*;
import user_interface.table_datatype.RecieverList;

public class MainMenu extends SuperWindow {

    public MainMenu(Stage stage) {
        super(stage, "Công ty Điện - Điện tử Tin học EIE");
    }

    @Override
    protected Pane setupPane() {
        TableColumn<RecieverList, String> nameColumn = new TableColumn<>("Tên"),
                emailColumn = new TableColumn<>("e-Mail"),
                attachmentColumn = new TableColumn<>("Đính kèm");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        attachmentColumn.setCellValueFactory(new PropertyValueFactory<>("attachment"));

        ObservableList<RecieverList> list = FXCollections.observableArrayList();

        TableView<RecieverList> table = new TableView<>();
        table.getColumns().addAll(nameColumn, emailColumn, attachmentColumn);
        table.setItems(list);

        Label attachmentLabel = new Label("Thư mục chứa tệp tin đính kèm"),
                messageLabel = new Label("Tin nhắn gởi kèm"),
                subjectLabel = new Label("Tiều đề");

        TextField nameTextField = new TextField(),
                emailTextField = new TextField(),
                attachmentTextField = new TextField(),
                attachmentFolderTextField = new TextField(),
                subjectTextField = new TextField();
        nameTextField.setPromptText("Tên");
        emailTextField.setPromptText("e-Mail");
        attachmentTextField.setPromptText("Tên tệp tin đính kèm");

        TextArea bodyMessageTextArea = new TextArea();
        bodyMessageTextArea.setPromptText("Nội dung");

        Button sendButton = new Button("Gửi"),
                addButton = new Button("Thêm"),
                deleteButton = new Button("Xóa"),
                changeButton = new Button("Thay đổi"),
                choseAttachmentFolder = new Button("chọn");
        sendButton.setOnAction(action -> super.executeCommand(new SendMails(list, subjectTextField.getText().trim(),
                bodyMessageTextArea.getText().trim(), attachmentFolderTextField.getText())));
        addButton.setOnAction(action -> super.executeCommand(new AddToTable(list, nameTextField, emailTextField,
                attachmentTextField)));
        deleteButton.setOnAction(action -> super.executeCommand(new DeleteEntryOfTable(table)));
        changeButton.setOnAction(action -> super.executeCommand(new ChangeEntryOfTable(nameTextField, emailTextField,
                attachmentTextField, table)));
        choseAttachmentFolder.setOnAction(action -> super.executeCommand(new OpenAttachmentFolderChooser(
                attachmentFolderTextField)));

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        HBox textFieldWrapper = new HBox(nameTextField, emailTextField, attachmentTextField),
                saveButtonWrapper = new HBox(sendButton),
                tableOperationButtonsWrapper = new HBox(addButton, deleteButton, changeButton),
                buttonsWrapper = new HBox(saveButtonWrapper, region, tableOperationButtonsWrapper),
                folderChooserWrapper = new HBox(attachmentFolderTextField, choseAttachmentFolder),
                subjectWrapper = new HBox(subjectLabel, subjectTextField);
        super.setupPane(super.BOX_STYLE, "leftMarginWrapper", textFieldWrapper, saveButtonWrapper,
                tableOperationButtonsWrapper, buttonsWrapper, folderChooserWrapper, subjectWrapper);

        VBox mainPane = new VBox(table, textFieldWrapper, buttonsWrapper, attachmentLabel, folderChooserWrapper,
                messageLabel, subjectWrapper, bodyMessageTextArea);
        super.setupPane(super.BOX_STYLE, "background", mainPane);

        return mainPane;
    }
}
