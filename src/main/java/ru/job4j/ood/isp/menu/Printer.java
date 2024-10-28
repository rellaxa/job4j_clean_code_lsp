package ru.job4j.ood.isp.menu;

public class Printer implements MenuPrinter {

    private static final String indent = "    ";

    @Override
    public void print(Menu menu) {
        menu.forEach(menuItem -> {
            String num = menuItem.getNumber();
            int countIndent = num.split("\\.").length - 1;
            String line = indent.repeat(countIndent) + num + menuItem.getName();
            System.out.println(line);
        });
    }

}
