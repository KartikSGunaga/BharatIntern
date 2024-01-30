package task01;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        ToDoApp.launch(ToDoApp.class, args);
        System.out.println("Welcome to Kartik's ToDo App!");
        ToDoApp todo = new ToDoApp();
        String taskName;
        boolean flag = true;

        todo.menu();
        while (flag) {
            System.out.println("\nChoose your choice(1-6): ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    todo.menu();
                    break;
                case 2:
                    // Pass the task name as an argument
                    System.out.println("\nEnter the task name: ");
                    taskName = scanner.nextLine();
                    todo.createTask(taskName);
                    break;
                case 3:
                    System.out.println("\nEnter the task name you wish to search: ");
                    taskName = scanner.nextLine().toLowerCase();
                    todo.viewTaskByName(taskName);
                    break;
                case 4:
                    System.out.println("\nEnter the due date of the task: ");
                    String taskDate = scanner.nextLine().replace(" ", "");
                    todo.viewTaskByDate(taskDate);
                    break;
                case 5:
                    System.out.println("\nEnter the task name you wish to update: ");
                    taskName = scanner.nextLine().toLowerCase();
                    todo.updateTask(taskName);
                    break;
                case 6:
                    System.out.println("\nEnter the task name you wish to delete: ");
                    taskName = scanner.nextLine().toLowerCase();
                    todo.deleteTask(taskName);
                    break;
                case 7:
                    flag = false;
                    break;
                default:
                    System.out.println("Invalid input");
            }
        }
        System.out.println("\nThank you for using Kartik's ToDo app!");
    }
}
