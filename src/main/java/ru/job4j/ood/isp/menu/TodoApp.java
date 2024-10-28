package ru.job4j.ood.isp.menu;

import java.util.Scanner;
import java.util.function.Function;

public class TodoApp {

    private static final ActionDelegate DEFAULT_ACTION = () -> System.out.println("Some action");
    private final Menu menu;

    public TodoApp(Menu menu) {
        this.menu = menu;
    }

    public boolean addRoot(String rootName) {
        return menu.add(Menu.ROOT, rootName, DEFAULT_ACTION);
    }

    public boolean addChild(String parentName, String childName) {
        return menu.add(parentName, childName, DEFAULT_ACTION);
    }

    public void executeAction(String itemName) {
        menu.select(itemName).ifPresentOrElse(item -> item.getActionDelegate().delegate(),
                                        () -> System.out.println("Пункт меню не найден."));
    }

    public void printMenu() {
        new Printer().print(menu);
        System.out.println();
    }

    private static void showActions() {
        System.out.println("___| Menu |___");
        System.out.println("1. Добавить элемент в корень меню.");
        System.out.println("2. Добавить элемент к родительскому элементу.");
        System.out.println("3. Вывести пункт меню.");
        System.out.println("4. Вывести все меню.");
        System.out.println("5. Выход...");
    }

    private static void addMenuItem(String prompt, Scanner scanner, Function<String, Boolean> addFunction) {
        boolean added = false;
        while (!added) {
            System.out.print(prompt);
            String itemName = scanner.nextLine();
            if (addFunction.apply(itemName)) {
                System.out.println("Пункт меню был добавлен.");
                added = true;
            } else {
                System.out.println("Пункт меню уже существует или родительский элемент не найден.");
                break;
            }
        }
    }

    public static void main(String[] args) {
        TodoApp app = new TodoApp(new SimpleMenu());
        Scanner scanner = new Scanner(System.in);
        while (true) {
            int select = -1;
            boolean invalid = true;
            showActions();
            do {
                try {
                    System.out.print("Выбрать: ");
                    select = Integer.parseInt(scanner.nextLine());
                    invalid = false;
                } catch (NumberFormatException nfe) {
                    System.out.println("Пожалуйста, введите цифру.");
                }
            } while (invalid);

            switch (select) {
                case 1:
                    System.out.println("--- Создание корневого пункта меню ---");
                    addMenuItem("Введите имя корневого пункта: ", scanner, app::addRoot);
                    break;
                case 2:
                    System.out.println("--- Создание подпункта меню ---");
                    System.out.print("Введите имя родительского пункта: ");
                    String parent = scanner.nextLine();
                    addMenuItem("Введите имя подпункта: ", scanner, childName -> app.addChild(parent, childName));
                    break;
                case 3:
                    System.out.print("Введите имя пункта меню: ");
                    String itemName = scanner.nextLine();
                    app.executeAction(itemName);
                    break;
                case 4:
                    app.printMenu();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Пожалуйста, введите вариант из предложенных.");
                    break;
            }
        }
    }

}
