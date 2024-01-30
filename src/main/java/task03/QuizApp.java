package task03;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class QuizApp extends Application {
    private String apiKey = "XfiXXYwAxHmQIlgcA00wag==5is4FYIdgwMi6LZF";
    private String url = "https://opentdb.com/api.php?amount=1";

    private TextArea questionTextArea;
    private TextField answerTextField;
    private Button submitButton;
    private Label feedbackLabel;

    private int questionNumber = 1;
    private int score = 0;

    public JSONObject getQuestion() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        URI uri = new URI(url);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JSONObject json = new JSONObject(response.body());
            int responseCode = json.getInt("response_code");

            if (responseCode == 0) {
                JSONArray results = json.getJSONArray("results");
                if (results.length() > 0) {
                    JSONObject firstResult = results.getJSONObject(0);
                    return firstResult;
                } else {
                    System.out.println("Oops! No results. Fetching another question.");
                    return getQuestion();
                }
            } else {
                System.out.println("Oops! Response code: " + responseCode);
                throw new RuntimeException("Error calling API response: " + response.body());
            }
        } else {
            System.out.println("Oops! Error calling API response: " + response.body());
            throw new RuntimeException("Error calling API response: " + response.body());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Quiz App");

        questionTextArea = new TextArea();
        answerTextField = new TextField();
        submitButton = new Button("Submit Answer");
        feedbackLabel = new Label();

        VBox layout = new VBox(20);
        layout.getChildren().addAll(questionTextArea, answerTextField, submitButton, feedbackLabel);

        Scene scene = new Scene(layout, 800, 500);

        String style = "-fx-font-size: 18; -fx-font-family: 'Arial'; -fx-font-weight: bold;" +
                "-fx-background-color: black; -fx-text-fill: white;";
        scene.getRoot().setStyle(style);

        primaryStage.setScene(scene);
        primaryStage.show();

        initializeQuiz();
    }

    private void initializeQuiz() {
        submitButton.setOnAction(e -> processAnswer());
        displayQuestion();
    }

    private void displayQuestion() {
        try {
            QuizApp quiz = new QuizApp();
            JSONObject response = quiz.getQuestion();

            if (response != null) {
                String question = response.getString("question");
                String type = response.getString("type");
                String category = response.getString("category");

                questionTextArea.setText("Question " + questionNumber + ": " + question +
                        "\nQuestion Category: " + category + "; Question Type: " + type);
            } else {
                // Handle the situation when there are no results
                feedbackLabel.setText("Oops! No questions available. Try again later.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processAnswer() {
        try {
            QuizApp quiz = new QuizApp();
            JSONObject response = quiz.getQuestion();

            if (response != null) {
                String correctAnswer = response.getString("correct_answer");
                String reqAnswer = correctAnswer.toLowerCase().replace(" ", "");
                String answer = answerTextField.getText().toLowerCase().replace(" ", "");

                String feedbackMessage;
                if (answer.equals(reqAnswer)) {
                    score++;
                    feedbackMessage = "Correct! The answer is: " + correctAnswer;
                } else {
                    feedbackMessage = "Wrong! The answer is: " + correctAnswer;
                }

                feedbackLabel.setText(feedbackMessage);

                questionNumber++;
                if (questionNumber <= 10) {
                    displayQuestion();
                } else {
                    endQuiz();
                }
            } else {
                feedbackLabel.setText("Oops! No questions available. Try again later.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void endQuiz() {
        feedbackLabel.setText("Your score: " + score + "\nThank you for playing!");
    }
}
