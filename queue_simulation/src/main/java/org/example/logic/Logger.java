package org.example.logic;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private static final String LOG_FILE = "simulation_log.txt";

    public static void log(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to log: " + e.getMessage());
        }
    }
    public static void clearLog() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE))) {
            writer.write(""); // goleşte conţinutul
        } catch (IOException e) {
            System.err.println("Error clearing log: " + e.getMessage());
        }
    }
}