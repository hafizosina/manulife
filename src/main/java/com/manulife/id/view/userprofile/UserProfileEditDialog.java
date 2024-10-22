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
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.beans.factory.annotation.Autowired;

public class UserProfileEditDialog extends Dialog {

    private final UserProfileService userService;
    private final UserProfileDto userProfile;
    TextField fullnameField;
    TextField emailField ;
    TextField usernameField;
    TextField roleField;
    TextArea addressField;

    Button saveButton;
    Button cancelButton;
    @Autowired
    public UserProfileEditDialog(UserProfileService userService, UserProfileDto userProfile) {
        this.userService = userService;
        this.userProfile = userProfile;
        createDialog();
    }

    public void createDialog() {

        setHeaderTitle("Edit User Profile");
        setModal(true);
        setWidth("60%");

        fullnameField = new TextField("Full Name");
        fullnameField.setWidth("100%");
        fullnameField.setValue(userProfile.getFullname());
        emailField = new TextField("Email");
        emailField.setWidth("100%");
        emailField.setValue(userProfile.getEmail());
        usernameField = new TextField("Username");
        usernameField.setWidth("100%");
        usernameField.setValue(userProfile.getUsername());
        roleField = new TextField("Role");
        roleField.setWidth("100%");
        roleField.setValue(userProfile.getRole());
        addressField = new TextArea("Address");
        addressField.setWidth("100%");
        addressField.setValue(userProfile.getAddress());

        saveButton = new Button("Save", event -> saveUserProfile());
        cancelButton = new Button("Cancel", event -> close());

        FormLayout formLayout = new FormLayout();
        formLayout.add(fullnameField, emailField, usernameField, roleField,addressField);
        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton,cancelButton);
        add(formLayout,buttonLayout);
    }

    private void saveUserProfile() {
        if ( VadiinValidationUtils.isValidInput(fullnameField, emailField, usernameField, roleField)) {
            UserProfileDto newUserProfile = new UserProfileDto();
            newUserProfile.setFullname(fullnameField.getValue());
            newUserProfile.setEmail(emailField.getValue());
            newUserProfile.setUsername(usernameField.getValue());
            newUserProfile.setAddress(addressField.getValue());
            newUserProfile.setRole(roleField.getValue());
            userService.update(newUserProfile, null);
            close();
        } else {
            throw new BadRequestException("Please fill in all fields correctly.", ResponseCode.IMPORTANT_DATA_IS_EMPTY);
        }
    }
}
