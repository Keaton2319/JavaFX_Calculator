package section6.app.javafx_calculator_app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class JavaFXCalculator extends Application {
    private TextField tfDisplay;    // display textfield
    private TextField memoryText;   // display memory textfield
    private Button[] btns;          // 28 buttons
    private String[] btnLabels = {  // Labels of 28 buttons
            "Off", "Dark", "Light", "+",
            "7", "8", "9", "-",
            "4", "5", "6", "*",
            "1", "2", "3", "÷",
            ".", "0", "=", "√",
            "C", "CE", "←", "^",
            "M+", "M-", "MR", "MC"
    };
    // For computation
    private double result = 0;      // Result of computation
    private String inStr = "0";  // Input number as String
    // Previous operator: ' '(nothing), '+', '-', '*', '/', '^', '√', '='
    private char lastOperator = ' ';
    private double memoryValue = 0;

    // Event handler for all the 28 Buttons
    EventHandler handler = evt -> {
        String currentBtnLabel = ((Button)evt.getSource()).getText();
        switch (currentBtnLabel) {
            // Number buttons
            case "0": case "1": case "2": case "3": case "4":
            case "5": case "6": case "7": case "8": case "9":
            case ".":
                if (inStr.equals("0")) {
                    inStr = currentBtnLabel;  // no leading zero
                } else {
                    inStr += currentBtnLabel; // append input digit
                }
                tfDisplay.setText(inStr);
                // Clear buffer if last operator is '='
                if (lastOperator == '=') {
                    result = 0;
                    lastOperator = ' ';
                }
                break;


            // Operator buttons: '+', '-', '*', '÷', '^', '√' and '='
            case "+":
                compute();
                lastOperator = '+';
                break;
            case "-":
                compute();
                lastOperator = '-';
                break;
            case "*":
                compute();
                lastOperator = '*';
                break;
            case "÷":
                compute();
                lastOperator = '/';
                break;
            case "=":
                compute();
                lastOperator = '=';
                break;
            case "^":
                compute();
                lastOperator = '^';
                break;
            case "√":
                compute();
                lastOperator = '√';
                break;

            // Clear button
            case "C":
                result = 0;
                inStr = "0";
                lastOperator = ' ';
                tfDisplay.setText("0");
                break;

            // Clear Entry button
            case "CE":
                inStr = "0";
                tfDisplay.setText("0");
                break;

            // Memory Clear button
            case "MC":
                memoryValue = 0.0;
                memoryText.setText("Memory = " + memoryValue);
                break;

            // Memory Recall button
            case "MR":
                inStr = String.valueOf(memoryValue);
                tfDisplay.setText(memoryValue + "");
                break;

            // Memory Plus button
            case "M+":
                if (lastOperator != '=') {
                    memoryValue += Double.parseDouble(inStr);
                }
                else {
                    memoryValue += result;
                }
                memoryText.setText("Memory = " + memoryValue);
                break;

            // Memory Minus button
            case "M-":
                if (lastOperator != '=') {
                    memoryValue -= Double.parseDouble(inStr);
                }
                else {
                    memoryValue -= result;
                }
                memoryText.setText("Memory = " + memoryValue);
                break;

            case "←":
                if (inStr.length() == 1) {
                    inStr = "0";
                } else {
                    tfDisplay.setText(inStr.substring(0, inStr.length() - 1));
                }
        }
    };

    // User pushes '+', '-', '*', '÷', '^', '√', or '=' button.
    // Perform computation on the previous result and the current input number,
    // based on the previous operator.
    private void compute() {
        double inNum = Integer.parseInt(inStr);
        inStr = "0";
        if (lastOperator == ' ') {
            result = inNum;
        } else if (lastOperator == '+') {
            result += inNum;
        } else if (lastOperator == '-') {
            result -= inNum;
        } else if (lastOperator == '*') {
            result *= inNum;
        } else if (lastOperator == '/') {
            result /= inNum;
        } else if (lastOperator == '^') {
            result = Math.pow(result, inNum);
        } else if (lastOperator == '√') {
            result = Math.sqrt(result);
        } else if (lastOperator == '=') {
            // Keep the result for the next operation
        }
        tfDisplay.setText(result + "");
    }

    // Setup the UI
    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        // Setup the Display TextField
        tfDisplay = new TextField("0");
        tfDisplay.setEditable(false);
        tfDisplay.setAlignment(Pos.CENTER_RIGHT);

        // Setup a GridPane for 4x7 Buttons
        int numCols = 4;
        int numRows = 7;
        GridPane paneButton = new GridPane();
        paneButton.setPadding(new Insets(15, 0, 15, 0));  // top, right, bottom, left
        paneButton.setVgap(5);  // Vertical gap between nodes
        paneButton.setHgap(5);  // Horizontal gap between nodes
        // Setup 4 columns of equal width, fill parent
        ColumnConstraints[] columns = new ColumnConstraints[numCols];
        for (int i = 0; i < numCols; ++i) {
            columns[i] = new ColumnConstraints();
            columns[i].setHgrow(Priority.ALWAYS) ;  // Allow column to grow
            columns[i].setFillWidth(true);  // Ask nodes to fill space for column
            paneButton.getColumnConstraints().add(columns[i]);
        }

        // Setup 28 Buttons and add to GridPane; and event handler
        btns = new Button[28];
        for (int i = 0; i < btns.length; ++i) {
            btns[i] = new Button(btnLabels[i]);
            btns[i].setOnAction(handler);  // Register event handler
            btns[i].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // full-width

            // Off button
            switch(btnLabels[i]) {
                case "C": case "CE": case "←": // set the color of more than one button at a time
                    btns[i].setStyle("-fx-color: #fbe0ad");
                    break;

                case "Off": // sets color of Off button and functions as an exit from the program
                    btns[i].setStyle("-fx-color: #8a2b2b");
                    btns[i].setOnAction(actionEvent -> Platform.exit());
                    break;

                case "Dark": // sets color of Dark button and switches to dark mode
                    btns[i].setStyle("-fx-text-fill: black; -fx-background-color: white;");
                    btns[i].setOnAction(ActionEvent -> {
                        root.setStyle("-fx-text-fill: white; -fx-background-color: black;");
                        memoryText.setStyle("-fx-fill: white;");
                    });
                    break;

                case "Light": // sets color of the Light button and switches to light mode
                    btns[i].setStyle("-fx-text-fill: white; -fx-background-color: black;");
                    btns[i].setOnAction(ActionEvent -> {
                        root.setStyle("-fx-text-fill: black; -fx-background-color: white;");
                        memoryText.setStyle("-fx-fill: black;");
                    });
                    break;

                case "MR": case "MC": case "M+": case "M-":
                    btns[i].setStyle("-fx-color: #2099c3");
                    break;

                case "+": case "-": case "*": case "÷": case "√": case "^":
                    btns[i].setStyle("-fx-color: #73ac82");
                    break;

                case ".": case "=":
                    btns[i].setStyle("-fx-color: #d8766b");
                    break;

            }
            paneButton.add(btns[i], i % numCols, i / numCols);  // control, col, row
        }

        // Setup up the scene graph rooted at a BorderPane (of 5 zones)
        root.setPadding(new Insets(15, 15, 15, 15));  // top, right, bottom, left
        root.setTop(tfDisplay);     // Top zone contains the TextField
        root.setCenter(paneButton); // Center zone contains the GridPane of Buttons

        // Set up scene and stage
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.setTitle("JavaFX Calculator");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}