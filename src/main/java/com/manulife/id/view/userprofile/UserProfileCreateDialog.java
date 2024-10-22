package com.manulife.id.view.userprofile;

import com.manulife.id.constant.ResponseCode;
import com.manulife.id.dto.UserProfileDto;
import com.manulife.id.exception.BadRequestException;
import com.manulife.id.service.UserProfileService;
import com.manulife.id.util.VadiinValidationUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;

import static javax.swing.JColorChooser.createDialog;

public class UserProfileCreateDialog extends Dialog {

    private final UserProfileService userService;
    private final Runnable onCloseCallback;
    TextField fullnameField;
    TextField emailField ;
    TextField usernameField;
    PasswordField passwordField;
    TextField roleField;
    TextArea addressField;

    Button saveButton;
    Button cancelButton;
    @Autowired
    public UserProfileCreateDialog(UserProfileService userService,  Runnable onCloseCallback) {
        this.userService = userService;
        this.onCloseCallback = onCloseCallback;
        createDialog();
    }


    public void createDialog() {

        setHeaderTitle("Create User Profile");
        setModal(true);
        setWidth("60%");

        fullnameField = new TextField("Full Name");
        fullnameField.setWidth("100%");
        emailField = new TextField("Email");
        emailField.setWidth("100%");
        usernameField = new TextField("Username");
        usernameField.setWidth("100%");
        passwordField = new PasswordField("Password");
        passwordField.setWidth("100%");
        roleField = new TextField("Role");
        roleField.setWidth("100%");
        addressField = new TextArea("Address");
        addressField.setWidth("100%");

        saveButton = new Button("Save", event -> saveUserProfile());
        cancelButton = new Button("Cancel", event -> close());

        FormLayout formLayout = new FormLayout();
        formLayout.add(fullnameField, emailField, usernameField, passwordField, roleField,addressField);
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton,cancelButton);
        add(formLayout,buttonLayout);
    }

    private void saveUserProfile() {
        if ( VadiinValidationUtils.isValidInput(fullnameField, emailField, usernameField, passwordField, roleField)) {
            UserProfileDto newUserProfile = new UserProfileDto();
            newUserProfile.setFullname(fullnameField.getValue());
            newUserProfile.setEmail(emailField.getValue());
            newUserProfile.setUsername(usernameField.getValue());
            newUserProfile.setAddress(addressField.getValue());
            newUserProfile.setRole(roleField.getValue());
            newUserProfile.setPassword(passwordField.getValue());
            userService.create(newUserProfile, null);
            close();
            onCloseCallback.run();
        } else {
            throw new BadRequestException("Please fill in all fields correctly.", ResponseCode.IMPORTANT_DATA_IS_EMPTY);
        }
    }


}
