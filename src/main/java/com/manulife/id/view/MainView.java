package com.manulife.id.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value  = "", layout = MainLayout.class)
@PageTitle("Manulife Indonesia")
public class MainView extends VerticalLayout {

    public MainView() {
        createHeader();
        createWelcomeSection();
        createNavigationButton();
    }

    private void createHeader() {
        H1 welcomeText = new H1("Welcome to Manulife Indonesia");
        welcomeText.getStyle().set("color", "#007bff");

        HorizontalLayout banner = new HorizontalLayout(welcomeText);
        banner.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        banner.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        banner.setWidthFull();
        add(banner);
    }

    private void createWelcomeSection() {
        Span description = new Span("Explore our user management system by navigating to the user section.");
        description.getStyle().set("text-align", "center");

        VerticalLayout welcomeLayout = new VerticalLayout(description);
        welcomeLayout.setAlignItems(Alignment.CENTER);
        welcomeLayout.setPadding(true);
        add(welcomeLayout);
    }

    private void createNavigationButton() {
        Button userPageButton = new Button("Go to User Page", event -> {
            getUI().ifPresent(ui -> ui.navigate("users"));
        });
        userPageButton.addClassName("primary");
        add(userPageButton);
        setAlignItems(Alignment.CENTER);
    }
}
