package com.manulife.id.util;

import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

public class VadiinValidationUtils {

    public static boolean isValidInput(TextField fullname, TextField email, TextField username, PasswordField password, TextField rolefield) {
        return !fullname.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty() && !rolefield.isEmpty();
    }
    public static boolean isValidInput(TextField fullname, TextField email, TextField username, TextField rolefield) {
        return !fullname.isEmpty() && !email.isEmpty() && !username.isEmpty()  && !rolefield.isEmpty();
    }
    public static boolean isValidInput(TextField fullname, EmailField email, TextField username, PasswordField password, TextField rolefield) {
        return !fullname.isEmpty() && !email.isEmpty() && !username.isEmpty() && !password.isEmpty() && !rolefield.isEmpty();
    }
}
