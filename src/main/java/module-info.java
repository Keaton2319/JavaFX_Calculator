module section6.app.javafx_calculator_app {
    requires javafx.controls;
    requires javafx.fxml;


    opens section6.app.javafx_calculator_app to javafx.fxml;
    exports section6.app.javafx_calculator_app;
}