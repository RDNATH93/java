package com.example;

public class FileIcon implements Icon {
    private final String type;
    private final String imageName;

    FileIcon(String type, String imageName) {
        this.type = type;
        this.imageName = imageName;
    }

    @Override
    public void display(int x, int y) {
        // Simulated logic to load and draw image
        System.out.println("Drawing " + type + " icon with image " +
                imageName + " at position (" + x + ", " + y + ")");

    }
}
