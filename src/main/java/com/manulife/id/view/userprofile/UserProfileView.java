package com.manulife.id.view.userprofile;

import com.manulife.id.dto.RequestPaging;
import com.manulife.id.dto.ResponsePagingDto;
import com.manulife.id.dto.UserProfileDto;
import com.manulife.id.service.UserProfileService;
import com.manulife.id.view.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
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

        createButton = new Button("Create", event -> {
            UserProfileCreateDialog dialog = new UserProfileCreateDialog(userService , () -> loadUserProfileDtos(currentPage) );
            dialog.open();
        });
        createButton.addClassName("primary");

        refreshButton = new Button("Refresh", event -> loadUserProfileDtos(currentPage));

        Icon downloadIcon = VaadinIcon.DOWNLOAD.create(); // Create a download icon
        downloadIcon.getElement().getStyle().set("margin-right", "5px"); // Optional: add some spacing
        downloadButton = new Button("Generate Report", downloadIcon, event -> downloadReport());

        HorizontalLayout actionButtons = new HorizontalLayout(createButton, refreshButton, downloadButton);
        actionButtons.setJustifyContentMode(JustifyContentMode.END);
        actionButtons.setWidth("100%");
        add(actionButtons);

        this.grid = new Grid<>();

        grid.addColumn(UserProfileDto::getFullname).setHeader("Full Name");
        grid.addColumn(UserProfileDto::getUsername).setHeader("Username");
//        grid.addColumn(UserProfileDto::getEmail).setHeader("Email");
//        grid.addColumn(UserProfileDto::getRole).setHeader("Role");
//        grid.addColumn(UserProfileDto::getAddress).setHeader("Address");
        grid.addComponentColumn(this::createImageComponent).setHeader("Image");
        grid.addComponentColumn(this::createUploadImageButton).setHeader("Upload");
        grid.addComponentColumn(this::createEditButton).setHeader("Edit");
        grid.addComponentColumn(this::createDeleteButton).setHeader("Delete");

        previousButton = new Button("Previous", event -> loadUserProfileDtos(--currentPage));
        nextButton = new Button("Next", event -> loadUserProfileDtos(++currentPage));

        HorizontalLayout pagination = new HorizontalLayout(previousButton, nextButton);

        add(grid, pagination);
        loadUserProfileDtos(currentPage);
    }

    private Image createImageComponent(UserProfileDto userProfile) {
        Image image = new Image();
        if (userProfile.getImage() != null) {
            image.setSrc(new StreamResource("user-image-" + userProfile.getId(),
                    () -> new ByteArrayInputStream(userProfile.getImage())));
            image.setAlt("User Image");
            image.setHeight("50px");
            image.setWidth("50px");
        } else {
            image.setSrc("images/broken-image.png");
            image.setHeight("50px");
            image.setWidth("50px");
        }
        return image;
    }

    private Button createUploadImageButton(UserProfileDto userProfile) {
        Button uploadButton = new Button("Upload", event ->{
            UserProfileUploadImageDialog dialog = new UserProfileUploadImageDialog(userService, userProfile , () -> loadUserProfileDtos(currentPage) );
            dialog.open();
        });
        uploadButton.addClassName("warning");
        return uploadButton;
    }
    private Button createEditButton(UserProfileDto userProfile) {
        Button editButton = new Button("Edit", event ->{
            UserProfileEditDialog dialog = new UserProfileEditDialog(userService, userProfile , () -> loadUserProfileDtos(currentPage) );
            dialog.open();
        });
        editButton.addClassName("warning");
        return editButton;
    }

    private Button createDeleteButton(UserProfileDto userProfile) {
        Button deleteButton = new Button("Delete", event -> {
            UserProfileDeleteDialog dialog = new UserProfileDeleteDialog(userService, userProfile, () -> loadUserProfileDtos(currentPage) );
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