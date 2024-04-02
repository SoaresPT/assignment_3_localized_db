package org.example.assignment_3_localized_db;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationApp extends Application {

    private ResourceBundle bundle;
    private Label firstNameLabel;
    private Label lastNameLabel;
    private Label emailLabel;
    private Button saveButton;
    private TextField firstNameInput;
    private TextField lastNameInput;
    private TextField emailInput;
    private Label selectLanguageLabel;
    private ComboBox<String> languageSelector;

    @Override
    public void start(Stage primaryStage) {
        setupUI(primaryStage);
        loadResourceBundle(Locale.ENGLISH); // Load default language
        updateUI(primaryStage);
    }

    private void setupUI(Stage primaryStage) {
        languageSelector = new ComboBox<>();
        languageSelector.getItems().addAll("English", "Farsi", "Japanese");
        languageSelector.setValue("English");

        languageSelector.setOnAction(event -> {
            String selectedLanguage = languageSelector.getValue();
            loadResourceBundle(getLocale(selectedLanguage));
            updateUI(primaryStage);
        });

        primaryStage.setTitle("Localization Example");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        selectLanguageLabel = new Label();
        firstNameLabel = new Label();
        lastNameLabel = new Label();
        emailLabel = new Label();
        firstNameInput = new TextField();
        lastNameInput = new TextField();
        emailInput = new TextField();
        saveButton = new Button();

        grid.add(selectLanguageLabel, 0, 0);
        grid.add(languageSelector, 1, 0);
        grid.add(firstNameLabel, 0, 1);
        grid.add(firstNameInput, 1, 1);
        grid.add(lastNameLabel, 0, 2);
        grid.add(lastNameInput, 1, 2);
        grid.add(emailLabel, 0, 3);
        grid.add(emailInput, 1, 3);
        grid.add(saveButton, 1, 4);

        Scene scene = new Scene(grid, 310, 200);
        primaryStage.setScene(scene);
        primaryStage.show();

        saveButton.setOnAction(e -> saveData(firstNameInput.getText(), lastNameInput.getText(), emailInput.getText(), languageSelector.getValue()));
    }

    private void loadResourceBundle(Locale locale) {
        bundle = ResourceBundle.getBundle("messages", locale);
    }

    private void updateUI(Stage primaryStage) {
        primaryStage.setTitle(bundle.getString("app.title"));
        firstNameLabel.setText(bundle.getString("label.firstName"));
        lastNameLabel.setText(bundle.getString("label.lastName"));
        emailLabel.setText(bundle.getString("label.email"));
        saveButton.setText(bundle.getString("button.save"));
        selectLanguageLabel.setText(bundle.getString("label.selectLanguage"));

        firstNameInput.setPromptText(bundle.getString("label.firstName"));
        lastNameInput.setPromptText(bundle.getString("label.lastName"));
        emailInput.setPromptText(bundle.getString("label.email"));
        selectLanguageLabel.setText(bundle.getString("label.selectLanguage"));
    }

    private Locale getLocale(String language) {
        switch (language) {
            case "Farsi":
                return new Locale("fa", "IR");
            case "Japanese":
                return Locale.JAPAN;
            default:
                return Locale.ENGLISH;
        }
    }

    private void saveData(String firstName, String lastName, String email, String selectedLanguage) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String jdbcUrl = "jdbc:mysql://localhost:3306/fxdemo";
            Connection conn = DriverManager.getConnection(jdbcUrl, "root", "Test12");

            String tableName;
            switch (selectedLanguage) {
                case "Farsi":
                    tableName = "employee_fa";
                    break;
                case "Japanese":
                    tableName = "employee_ja";
                    break;
                default:
                    tableName = "employee_en"; // Default to English
            }

            String sql = "INSERT INTO " + tableName + " (first_name, last_name, email) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            statement.setString(3, email);
            statement.executeUpdate();
            System.out.println(bundle.getString("message.saved"));
            conn.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
