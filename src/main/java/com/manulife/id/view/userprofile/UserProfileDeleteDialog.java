package com.manulife.id.view.userprofile;

import com.manulife.id.dto.UserProfileDto;
import com.manulife.id.service.UserProfileService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class UserProfileDeleteDialog extends Dialog {

    private final UserProfileService userService;
    private final UserProfileDto userProfile;
    private final Runnable onCloseCallback;

    Button saveButton;
    Button cancelButton;
    
    UserProfileDeleteDialog (UserProfileService userService, UserProfileDto userProfile, Runnable onCloseCallback) {
        this.userService = userService;
        this.userProfile = userProfile;
        this.onCloseCallback = onCloseCallback;
        createDialog();
    }

    private void createDialog() {
        setHeaderTitle("Delete User Profile");
        setModal(true);

        H1 question = new H1("Are You Sure want to Delete User "+ userProfile.getFullname());
        question.addClassNames("text-l", "m-m");

        saveButton = new Button("Save", event -> deleteUserProfile());
        cancelButton = new Button("Cancel", event -> close());
        HorizontalLayout hlayout = new HorizontalLayout(saveButton,cancelButton);
        add(question,hlayout);
    }

    private void deleteUserProfile() {
        userService.delete(userProfile.getUsername(), null);
        close();
        onCloseCallback.run();
    }

}
