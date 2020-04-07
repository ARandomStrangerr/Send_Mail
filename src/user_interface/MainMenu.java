package user_interface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
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
        ObservableList<RecieverList> list = FXCollections.observableArrayList(new RecieverList("T","bill","gmail.pdf"));

        TableView<RecieverList> table = new TableView<>();
        table.setEditable(true);
        table.setItems(list);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        super.setupControl(super.TABLE_STYLE, "normal", table);

        TableColumn<RecieverList, String> nameColumn = new TableColumn<>("Tên"),
                emailColumn = new TableColumn<>("e-Mail"),
                attachmentColumn = new TableColumn<>("Đính kèm");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(event -> table.getSelectionModel().getSelectedItem().setName(event.getNewValue()));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        emailColumn.setOnEditCommit(event -> table.getSelectionModel().getSelectedItem().setEmail(event.getNewValue()));
        attachmentColumn.setCellValueFactory(new PropertyValueFactory<>("attachment"));
        attachmentColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        attachmentColumn.setOnEditCommit(event -> table.getSelectionModel().getSelectedItem()
                .setAttachment(event.getNewValue()));

        table.getColumns().addAll(nameColumn, emailColumn, attachmentColumn);

        Label attachmentLabel = new Label("Thư mục chứa tệp tin đính kèm"),
                messageLabel = new Label("Tin nhắn gởi kèm"),
                subjectLabel = new Label("Tiều đề"),
                tableLabel = new Label("Danh sách người nhận");
        super.setupControl(super.LABEL_STYLE, "normal", attachmentLabel, messageLabel, subjectLabel,
                tableLabel);

        TextField nameTextField = new TextField(),
                emailTextField = new TextField(),
                attachmentTextField = new TextField(),
                attachmentFolderTextField = new TextField(),
                subjectTextField = new TextField(),
                excelFileTextField = new TextField();
        nameTextField.setPromptText("Tên");
        emailTextField.setPromptText("e-Mail");
        attachmentTextField.setPromptText("Tên tệp tin đính kèm");
        super.setupControl(super.TEXT_FIELD_STYLE, "normal", nameTextField, emailTextField,
                attachmentTextField, attachmentFolderTextField, subjectTextField, excelFileTextField);
        HBox.setHgrow(subjectTextField, Priority.ALWAYS);
        HBox.setHgrow(attachmentFolderTextField, Priority.ALWAYS);
        HBox.setHgrow(excelFileTextField, Priority.ALWAYS);

        TextArea bodyMessageTextArea = new TextArea();
        bodyMessageTextArea.setPromptText("Nội dung");

        Button sendButton = new Button("Gửi"),
                addButton = new Button("Thêm"),
                deleteButton = new Button("Xóa"),
                changeButton = new Button("Thay đổi"),
                choseAttachmentFolder = new Button("Chọn"),
                choseExcelFile = new Button("Chọn");
        sendButton.setOnAction(action -> super.executeCommand(new SendMails(list, subjectTextField.getText().trim(),
                bodyMessageTextArea.getText().trim(), attachmentFolderTextField.getText())));
        addButton.setOnAction(action -> super.executeCommand(new AddToTable(list, nameTextField, emailTextField,
                attachmentTextField)));
        deleteButton.setOnAction(action -> super.executeCommand(new DeleteEntryOfTable(table)));
        changeButton.setOnAction(action -> super.executeCommand(new ChangeEntryOfTable(nameTextField, emailTextField,
                attachmentTextField, table)));
        choseAttachmentFolder.setOnAction(action -> super.executeCommand(new OpenAttachmentFolderChooser(
                attachmentFolderTextField)));
        choseExcelFile.setOnAction(action -> super.executeCommand(new OpenAndReadReceiverExcelFile(excelFileTextField,
                list)));
        super.setupControl(super.BUTTON_STYLE, "normal", sendButton, addButton, deleteButton, changeButton,
                choseAttachmentFolder, choseExcelFile);

        Region betweenTableLabelAndTableOperationButtons = new Region();
        HBox.setHgrow(betweenTableLabelAndTableOperationButtons, Priority.ALWAYS);

        HBox textFieldWrapper = new HBox(nameTextField, emailTextField, attachmentTextField),
                tableOperationButtonsWrapper = new HBox(addButton, deleteButton, changeButton),
                tableLabelAndOperationWrapper = new HBox(tableLabel, betweenTableLabelAndTableOperationButtons,
                        tableOperationButtonsWrapper),
                sendButtonWrapper = new HBox(sendButton),
                buttonsWrapper = new HBox(sendButtonWrapper),
                folderChooserWrapper = new HBox(attachmentFolderTextField, choseAttachmentFolder),
                subjectWrapper = new HBox(subjectLabel, subjectTextField),
                excelChooserWrapper = new HBox(excelFileTextField, choseExcelFile);
        super.setupPane(super.BOX_STYLE, "leftMarginWrapper", textFieldWrapper, sendButtonWrapper,
                tableLabelAndOperationWrapper, buttonsWrapper, folderChooserWrapper, subjectWrapper,
                excelChooserWrapper);

        VBox mainPane = new VBox(excelChooserWrapper, tableLabelAndOperationWrapper, table, textFieldWrapper,
                buttonsWrapper, attachmentLabel, folderChooserWrapper, messageLabel, subjectWrapper,
                bodyMessageTextArea);
        super.setupPane(super.BOX_STYLE, "background", mainPane);

        return mainPane;
    }
}
