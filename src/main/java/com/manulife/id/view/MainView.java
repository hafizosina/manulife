package com.manulife.id.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value  = "", layout = MainLayout.class)
@PageTitle("Manulife Indonesia")
public class MainView extends VerticalLayout {

    public MainView(){
        createHeader();
    }
    private void createHeader() {
        H1 hello = new H1("Welcome");
        hello.addClassNames("text-l", "m-m");

        HorizontalLayout banner = new HorizontalLayout(hello);
        banner.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        banner.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        banner.setWidth("100%");
        banner.addClassNames("py-m", "px-m");
        add(banner);
    }
}
