package ru.job4j.ood.isp.menu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class PrinterTest {

    public static final ActionDelegate STUB_ACTION = System.out::println;
    private final ByteArrayOutputStream outStream = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStream() {
        outStream.reset();
        System.setOut(new PrintStream(outStream));
    }

    @AfterEach
    public void restoreStream() {
        System.setOut(System.out);
    }

    private Menu getMenu() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add(Menu.ROOT, "Покормить собаку", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить продукты", STUB_ACTION);
        menu.add("Купить продукты", "Купить хлеб", STUB_ACTION);
        menu.add("Купить продукты", "Купить молоко", STUB_ACTION);
        return menu;
    }

    @Test
    public void whenAddElementsInMenuThenPrintCorrectInfo() {
        Menu menu = getMenu();
        MenuPrinter printer = new Printer();
        printer.print(menu);
        String expected = String.join(System.lineSeparator(),
                "1.Сходить в магазин",
                          "    1.1.Купить продукты",
                          "        1.1.1.Купить хлеб",
                          "        1.1.2.Купить молоко",
                          "2.Покормить собаку",
                          "");
        assertEquals(outStream.toString(), expected);
    }
}