package task03;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class QuizAppFinal {
    public static ArrayList<String> getQuestion() throws Exception {
        String apiKey = "XfiXXYwAxHmQIlgcA00wag==5is4FYIdgwMi6LZF";
        String category = getTheme();
        String url = "https://api.api-ninjas.com/v1/trivia?category=" + category;

        HttpClient client = HttpClient.newHttpClient();
        URI uri = new URI(url);
        HttpRequest request = HttpRequest.newBuilder().uri(uri).header("X-Api-Key", apiKey).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ArrayList<String> questionList = new ArrayList<>();
        JSONArray jsonArray;

//        System.out.println(response.body());
        if (response.statusCode() == 200) {
//            System.out.println("Request Successful");
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
        } else {
            System.out.println("Oops!");
            throw new RuntimeException("Error calling API response: " + response.body());
        }

        return questionList;
    } 

    public static String getTheme() {
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

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to the quiz!");
        int score = 0;

        for (int i = 0; i < 10; i++){
            System.out.println();
//            String theme = getTheme();
            ArrayList<String> questionList = getQuestion();

            System.out.println("Category: ");
            System.out.println(questionList.get(0));
            System.out.println("Question: ");
            System.out.println(questionList.get(1));
            System.out.println("Answer: ");
            String answer = scanner.nextLine();

            if (answer.equalsIgnoreCase(questionList.get(2))) {
                score += 1;
            }
            System.out.println("The answer is: " + questionList.get(2));
        }
        System.out.println("Your Score: " + score);
        System.out.println("Thank you for playing!");
    }
}
