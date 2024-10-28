package ru.job4j.ood.isp.menu;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class SimpleMenuTest {

    public static final ActionDelegate STUB_ACTION = System.out::println;

    @Test
    public void whenAddThenReturnSame() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add(Menu.ROOT, "Покормить собаку", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить продукты", STUB_ACTION);
        menu.add("Купить продукты", "Купить хлеб", STUB_ACTION);
        menu.add("Купить продукты", "Купить молоко", STUB_ACTION);
        assertThat(new Menu.MenuItemInfo("Сходить в магазин",
                List.of("Купить продукты"), STUB_ACTION, "1."))
                .isEqualTo(menu.select("Сходить в магазин").get());
        assertThat(new Menu.MenuItemInfo(
                "Купить продукты",
                List.of("Купить хлеб", "Купить молоко"), STUB_ACTION, "1.1."))
                .isEqualTo(menu.select("Купить продукты").get());
        assertThat(new Menu.MenuItemInfo(
                "Покормить собаку", List.of(), STUB_ACTION, "2."))
                .isEqualTo(menu.select("Покормить собаку").get());
        menu.forEach(i -> System.out.println(i.getNumber() + i.getName()));
    }

    @Test
    public void whenAddRootElementThenReturnTheCorrectInfo() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Задача 1", STUB_ACTION);
        menu.add(Menu.ROOT, "Задача 2", STUB_ACTION);
        Optional<Menu.MenuItemInfo> itemInfo = menu.select("Задача 1");
        assertThat(itemInfo).isPresent();
        assertThat(itemInfo.get().getName()).isEqualTo("Задача 1");
        assertThat(menu.select("Задача 2").get().getName()).isEqualTo("Задача 2");
    }

    @Test
    public void whenAddChildToParentThenReturnThenReturnCorrectInfo() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);
        menu.add("Сходить в магазин", "Купить продукты", STUB_ACTION);
        menu.add("Купить продукты", "Купить хлеб", STUB_ACTION);
        menu.add("Купить продукты", "Купить молоко", STUB_ACTION);

        Optional<Menu.MenuItemInfo> optItem = menu.select("Купить продукты");
        assertThat(optItem).isPresent();
        assertThat(optItem.get().getChildren()).hasSize(2);
        assertThat(optItem.get().getNumber()).isEqualTo("1.1.");
        assertThat(optItem.get().getChildren().containsAll(List.of("Купить хлеб", "Купить молоко"))).isTrue();
    }

    @Test
    public void whenAddParentThenReturnEmpty() {
        Menu menu = new SimpleMenu();
        menu.add(Menu.ROOT, "Сходить в магазин", STUB_ACTION);

        Optional<Menu.MenuItemInfo> itemInfo = menu.select("Помыть кота");
        assertThat(itemInfo).isNotPresent();
    }
}