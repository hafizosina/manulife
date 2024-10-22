package com.manulife.id.view.userprofile;

import com.manulife.id.constant.ResponseCode;
import com.manulife.id.dto.ImageRequestDto;
import com.manulife.id.dto.UserProfileDto;
import com.manulife.id.exception.BadRequestException;
import com.manulife.id.exception.ProcessException;
import com.manulife.id.service.UserProfileService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class UserProfileUploadImageDialog extends Dialog {

    private final UserProfileService userService;
    private final UserProfileDto userProfile;
    private final Runnable onCloseCallback;

    Button saveButton;
    Button cancelButton;

    String base64Image;

    private MemoryBuffer memoryBuffer;
    private Upload upload;
    UserProfileUploadImageDialog(UserProfileService userService, UserProfileDto userProfile, Runnable onCloseCallback) {
        this.userService = userService;
        this.userProfile = userProfile;
        this.onCloseCallback = onCloseCallback;
        createDialog();
    }

    private void createDialog() {
        setHeaderTitle("Image");
        setModal(true);

        H1 question = new H1("Upload Image for User: " + userProfile.getFullname());
        question.addClassNames("text-l", "m-m");

        memoryBuffer = new MemoryBuffer();
        upload = new Upload(memoryBuffer);
        upload.setAcceptedFileTypes("image/png", "image/jpeg");
        upload.setMaxFiles(1);

        upload.addSucceededListener(event -> {
            try{
                byte[] bytes = memoryBuffer.getInputStream().readAllBytes();
                base64Image = java.util.Base64.getEncoder().encodeToString(bytes);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new ProcessException("Unknown error", ResponseCode.GENERAL_ERROR);
            }
        });

        saveButton = new Button("Save", event -> uploadImage());
        cancelButton = new Button("Cancel", event -> close());
        HorizontalLayout hlayout = new HorizontalLayout(saveButton,cancelButton);
        add(question,upload ,hlayout);
    }

    private void uploadImage() {
        if (base64Image != null && !base64Image.isEmpty()) {
            ImageRequestDto dto = new ImageRequestDto();
            dto.setUsername(userProfile.getUsername());
            dto.setBase64(base64Image);
            userService.uploadImage(dto, null);
            close();
            onCloseCallback.run();
        } else {
            throw new BadRequestException("Please upload an image before saving.", ResponseCode.NULL_EXCEPTION);
        }
    }

}
