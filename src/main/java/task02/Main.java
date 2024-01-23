package task02;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main extends Application {
    private TextField apiKeyInput;
    private TextField cityInput;
    private Label resultLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Current Weather Fetcher");

        VBox layout = new VBox(15);
        layout.setPadding(new Insets(20, 10, 10, 20));

        apiKeyInput = new TextField();
        apiKeyInput.setPromptText("Enter API Key of weatherapi.com");
        apiKeyInput.setStyle("-fx-font-family: 'JetBrains Mono'; -fx-font-size: 17; -fx-font-weight: bold;");

        cityInput = new TextField();
        cityInput.setPromptText("Enter City");
        cityInput.setStyle("-fx-font-family: 'JetBrains Mono'; -fx-font-size: 17; -fx-font-weight: bold;");

        Button fetchButton = new Button("Fetch Weather");
        fetchButton.setStyle("-fx-font-family: 'JetBrains Mono'; -fx-font-size: 17; -fx-font-weight: bold;");
        fetchButton.setOnAction(e -> fetchWeather());

        resultLabel = new Label();
        resultLabel.setStyle("-fx-font-family: 'JetBrains Mono'; -fx-font-size: 19; -fx-font-weight: bold;");

        layout.getChildren().addAll(apiKeyInput, cityInput, fetchButton, resultLabel);

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    private void fetchWeather() {
        try {
            String apiKey = apiKeyInput.getText();
            String city = cityInput.getText();

            String endPoint = "http://api.weatherapi.com/v1/current.json?key="
                    + apiKey + "&q=" + city + "&aqi=yes";

            HttpClient client = HttpClient.newHttpClient();
            URI uri = new URI(endPoint);
            HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JSONObject json = new JSONObject(response.body());
                JSONObject location = json.getJSONObject("location");
                JSONObject current = json.getJSONObject("current");

                String lastUpdated = current.getString("last_updated");
                String place = location.getString("name") + ", "
                        + location.getString("region") + ", "
                        + location.getString("country");
                String timeZone = location.getString("tz_id");
                String localTime = location.getString("localtime");
                float tempCelsius = current.getFloat("temp_c");
                String feelsLike = current.getJSONObject("condition").getString("text");

                String resultText = "Fetched data for " + place + "\n" +
                        "Fetched at: " + lastUpdated + " (TimeZone: " + timeZone + ")\n" +
                        "Local time in " + location.getString("name") + ": " + localTime + " (TimeZone: " + timeZone + ")\n" +
                        "Current temperature: " + tempCelsius + " °C\n" +
                        "Feels like " + feelsLike;

                resultLabel.setText(resultText);

            } else {
                resultLabel.setText("Oops! Error calling API response: " + response.body());
            }
        } catch (Exception e) {
            resultLabel.setText("Error fetching weather data: " + e.getMessage());
        }
    }
}

