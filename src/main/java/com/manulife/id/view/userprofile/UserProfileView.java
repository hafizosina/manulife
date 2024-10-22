package com.manulife.id.view.userprofile;

import com.manulife.id.constant.ResponseCode;
import com.manulife.id.dto.RequestPaging;
import com.manulife.id.dto.ResponsePagingDto;
import com.manulife.id.dto.UserProfileDto;
import com.manulife.id.exception.BadRequestException;
import com.manulife.id.service.UserProfileService;
import com.manulife.id.view.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamRegistration;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.util.List;

@Route(value = "users", layout = MainLayout.class)
@PageTitle("User")
public class UserProfileView extends VerticalLayout {

    private final UserProfileService userService;
    private final Grid<UserProfileDto> grid;
    private final Button previousButton;
    private final Button nextButton;
    private final Button createButton;
    private final Button refreshButton;
    private final Button downloadButton;
    private int currentPage = 0;
    private final int pageSize = 10;

    @Autowired
    public UserProfileView(UserProfileService userService) {
        this.userService = userService;

        createButton = new Button("Create", event -> openCreateUserProfileDialog());
        createButton.addClassName("primary");

        refreshButton = new Button("Refresh", event -> loadUserProfileDtos(currentPage));

        Icon downloadIcon = VaadinIcon.DOWNLOAD.create(); // Create a download icon
        downloadIcon.getElement().getStyle().set("margin-right", "5px"); // Optional: add some spacing
        downloadButton = new Button("Generate Report", downloadIcon, event -> downloadReport());

        HorizontalLayout actionButtons = new HorizontalLayout(createButton, refreshButton, downloadButton);
        actionButtons.setJustifyContentMode(JustifyContentMode.END);
        actionButtons.setWidth("100%");
        add(actionButtons);

        this.grid = new Grid<>(UserProfileDto.class);

        // Add edit and delete buttons to the grid
        grid.addComponentColumn(this::createEditButton).setHeader("Edit");
        grid.addComponentColumn(this::createDeleteButton).setHeader("Delete");

        previousButton = new Button("Previous", event -> loadUserProfileDtos(--currentPage));
        nextButton = new Button("Next", event -> loadUserProfileDtos(++currentPage));

        HorizontalLayout pagination = new HorizontalLayout(previousButton, nextButton);

        add(grid, pagination);
        loadUserProfileDtos(currentPage);
    }

    private Button createEditButton(UserProfileDto userProfile) {
        Button editButton = new Button("Edit", event ->{
            UserProfileEditDialog dialog = new UserProfileEditDialog(userService, userProfile);
            dialog.open();
            loadUserProfileDtos(currentPage);  // todo : the refesh shold be happen when the dialog is closed
        });
        editButton.addClassName("warning");
        return editButton;
    }

    private Button createDeleteButton(UserProfileDto userProfile) {
        Button deleteButton = new Button("Delete", event -> {
            UserProfileDeleteDialog dialog = new UserProfileDeleteDialog(userService, userProfile);
            dialog.open();
            loadUserProfileDtos(currentPage);
        });
        deleteButton.addClassName("error");;
        return deleteButton;
    }

    private void loadUserProfileDtos(int page) {
        RequestPaging request = new RequestPaging(page, pageSize);
        ResponsePagingDto<List<UserProfileDto>> response = userService.getPaging(request, null);
        grid.setItems(response.getResult());
        grid.setPageSize(pageSize);

        previousButton.setEnabled(page > 0);
        nextButton.setEnabled(page < response.getTotalPages() - 1);
    }

    private void openCreateUserProfileDialog() {
        Dialog dialog = new Dialog();
        dialog.setModal(true);
        dialog.setWidth("60%");

        TextField fullnameField = new TextField("Full Name");
        fullnameField.setWidth("100%");
        TextField emailField = new TextField("Email");
        emailField.setWidth("100%");
        TextField usernameField = new TextField("Username");
        usernameField.setWidth("100%");
        TextField roleField = new TextField("Role");
        roleField.setWidth("100%");
        TextArea addressField = new TextArea("Address");
        addressField.setWidth("100%");
        PasswordField passwordField = new PasswordField("Password");
        passwordField.setWidth("100%");

        Button saveButton = new Button("Save", event -> {
            if (isValidInput(fullnameField, emailField, usernameField, passwordField, roleField)) {
                UserProfileDto newUserProfile = new UserProfileDto();
                newUserProfile.setFullname(fullnameField.getValue());
                newUserProfile.setEmail(emailField.getValue());
                newUserProfile.setUsername(usernameField.getValue());
                newUserProfile.setPassword(passwordField.getValue());
                newUserProfile.setAddress(addressField.getValue());
                newUserProfile.setRole(roleField.getValue());
                userService.create(newUserProfile, null);
                dialog.close();
                loadUserProfileDtos(currentPage);
            } else {
                throw new BadRequestException("Please fill in all fields correctly.", ResponseCode.IMPORTANT_DATA_IS_EMPTY);
            }
        });

        Button cancelButton = new Button("Cancel", event -> dialog.close());
        VerticalLayout formLayout = new VerticalLayout(fullnameField, emailField, usernameField, passwordField, roleField, addressField, saveButton, cancelButton);
        formLayout.setWidth("100%");
        formLayout.setPadding(true);
        formLayout.setSpacing(true);

        dialog.add(formLayout);
        dialog.open();
    }

    private void downloadReport() {
        StreamResource resource = new StreamResource("List User.pdf", this::generatePdf);
        resource.setContentType("application/pdf");

        // https://vaadin.com/forum/t/download-link-or-button/157314/7
        final StreamRegistration registration = VaadinSession.getCurrent().getResourceRegistry().registerResource(resource);
        UI.getCurrent().getPage().executeJs("window.open($0, $1)", registration.getResourceUri().toString(), "_blank");
    }

    private ByteArrayInputStream generatePdf() {
        try {
            return userService.exportPdf(null);
        } catch (Exception e) {
            Notification.show("Error generating PDF: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            return null;
        }
    }

    private boolean isValidInput(TextField fullname, TextField email, TextField username, PasswordField password, TextField rolefield) {
        return !fullname.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty() && !rolefield.isEmpty();
    }
    private boolean isValidInput(TextField fullname, EmailField email, TextField username, PasswordField password, TextField rolefield) {
        return !fullname.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty() && !rolefield.isEmpty();
    }
}