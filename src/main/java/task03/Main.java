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
import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {
    private String apiKey = "XfiXXYwAxHmQIlgcA00wag==5is4FYIdgwMi6LZF";
    private TextField answerTextField;
    private Button submitButton;
    private Label feedbackLabel;
    private ArrayList<String> questionList;
    private int questionNumber = 1;
    private int score = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Quiz App");

        answerTextField = new TextField();
        submitButton = new Button("Submit Answer");
        feedbackLabel = new Label();
        questionList = new ArrayList<>();

        VBox layout = new VBox(20);
        layout.getChildren().addAll( feedbackLabel, answerTextField, submitButton);

        Scene scene = new Scene(layout, 600, 400);

        submitButton.setOnAction(e -> processAnswer());

        primaryStage.setScene(scene);
        primaryStage.show();

        initializeQuiz();
    }

    private void initializeQuiz() {
        displayQuestion();
    }

    private void displayQuestion() {
        try {
            questionList = getQuestion();

            if (!questionList.isEmpty()) {
                String question = "Category: " + questionList.get(0) +
                        "\nQuestion: " + questionList.get(1);
                feedbackLabel.setText(question);
            } else {
                feedbackLabel.setText("Oops! No questions available. Try again later.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processAnswer() {
        if (!questionList.isEmpty()) {
            String correctAnswer = questionList.get(2);
            String userAnswer = answerTextField.getText().trim();

            String feedbackMessage;
            if (userAnswer.equalsIgnoreCase(correctAnswer)) {
                score++;
                feedbackMessage = "Correct! The answer is: " + correctAnswer;
            } else {
                feedbackMessage = "Wrong! The answer is: " + correctAnswer;
            }

            feedbackLabel.setText(feedbackMessage);
            answerTextField.clear();

            questionNumber++;
            if (questionNumber <= 10) {
                displayQuestion();
            } else {
                endQuiz();
            }
        }
    }

    private void endQuiz() {
        feedbackLabel.setText("Your score: " + score + "\nThank you for playing!");
    }

    public ArrayList<String> getQuestion() throws Exception {
        String category = getTheme();
        String url = "https://api.api-ninjas.com/v1/trivia?category=" + category;

        HttpClient client = HttpClient.newHttpClient();
        URI uri = new URI(url);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .header("X-Api-Key", apiKey)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ArrayList<String> questionList = new ArrayList<>();
        JSONArray jsonArray;

        if (response.statusCode() == 200) {
            jsonArray = new JSONArray(response.body());

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);

                String qCategory = json.getString("category");
                String question = json.getString("question");
                String answer = json.getString("answer");

                questionList.add(qCategory);
                questionList.add(question);
                questionList.add(answer);
            }
        }

        return questionList;
    }

    public String getTheme() {
        ArrayList<String> categoryList = new ArrayList<>();
        categoryList.add("artliterature");
        categoryList.add("language");
        categoryList.add("sciencenature");
        categoryList.add("general");
        categoryList.add("fooddrink");
        categoryList.add("peopleplaces");
        categoryList.add("geography");
        categoryList.add("historyholidays");
        categoryList.add("entertainment");
        categoryList.add("toysgames");
        categoryList.add("music");
        categoryList.add("mathematics");
        categoryList.add("religionmythology");
        categoryList.add("sportsleisure");

        Random random = new Random();
        int randomIndex = random.nextInt(categoryList.size());

        return categoryList.get(randomIndex);
    }
}
