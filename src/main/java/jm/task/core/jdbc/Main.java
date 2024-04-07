package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();

        userService.createUsersTable();

        userService.saveUser("Алексей", "Петров", (byte) 27);
        userService.saveUser("Алёна", "Измайлова", (byte) 22);
        userService.saveUser("Олег", "Ким", (byte) 32);
        userService.saveUser("Юрий", "Александров", (byte) 55);

        System.out.println(userService.getAllUsers());

        userService.cleanUsersTable();

        userService.dropUsersTable();

    }
}
