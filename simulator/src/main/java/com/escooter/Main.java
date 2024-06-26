package com.escooter;

import java.util.Scanner;

import com.escooter.repository.MemberCardRepository;
import com.escooter.service.EscooterSimulateService;

public class Main {

    private static final EscooterSimulateService escooterSimulateService = new EscooterSimulateService();
    private static final MemberCardRepository memberCardRepository = new MemberCardRepository();

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("> ");
                String command = scanner.nextLine().trim();

                if (command.equalsIgnoreCase("start")) {
                    escooterSimulateService.start();
                } else if (command.equalsIgnoreCase("stop")) {
                    escooterSimulateService.stop();
                } else if (command.startsWith("stop ")) {
                    String[] parts = command.split(" ");
                    if (parts.length == 2) {
                        String escooterId = parts[1];
                        escooterSimulateService.stopEscooter(escooterId);
                    } else {
                        System.out.println("Invalid command. Usage: stop \"escooterId\"");
                    }
                } else if (command.startsWith("bind ")) {
                    String[] parts = command.split(" ");
                    memberCardRepository.addMemberCard(parts[1], parts[2], parts[3]);
                    System.out.println(parts[1] + " bind member card");
                } else {
                    System.out.println("Unknown command.");
                }
            }
        }
    }
}
