package com.example;

class FolderIcon implements Icon {
    private final String color; // Intrinsic state: color of the folder icon
    private final String imageName; // Intrinsic state: image name specific to folder icon

    FolderIcon(String color, String imageName) {
        this.color = color;
        this.imageName = imageName;
    }

    public void display(int x, int y) {
        // Simulated logic to load and draw image
        System.out.println("Drawing folder icon with color " + color + 
        " and image " + imageName + " at position (" + x + ", " + y + ")");
    }
}
