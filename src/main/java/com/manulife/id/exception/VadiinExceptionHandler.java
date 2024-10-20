package com.manulife.id.exception;

import com.manulife.id.constant.ResponseCode;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;

public class VadiinExceptionHandler implements ErrorHandler {
    @Override
    public void error(ErrorEvent errorEvent) {
        if (UI.getCurrent() != null) {
            Throwable throwable = errorEvent.getThrowable();
            if (throwable instanceof BadRequestException badRequestException) {
                showDialog(badRequestException.getCode(), badRequestException.getMessage());
            } else if (throwable instanceof ProcessException processException) {
                showDialog(processException.getCode(), processException.getMessage());
            } else {
                showDialog(ResponseCode.GENERAL_ERROR, "An unexpected error occurred.");
            }
        }
    }

    private void showDialog(String code, String message) {
        UI.getCurrent().access(() -> {
            Dialog dialog = new Dialog();
            dialog.setCloseOnEsc(true);
            dialog.setCloseOnOutsideClick(true);

            VerticalLayout dialogLayout = new VerticalLayout();
            NativeLabel titleLabel = new NativeLabel("Error : "+code);
            titleLabel.addClassNames("font-weight", "bold");
            NativeLabel messageLabel = new NativeLabel(message);
            Button okButton = new Button("OK", event -> dialog.close());
            dialogLayout.add(titleLabel, messageLabel, okButton);
            dialog.add(dialogLayout);
            dialog.open();
        });
    }
}
