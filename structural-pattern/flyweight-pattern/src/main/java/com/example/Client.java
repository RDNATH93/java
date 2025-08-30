package com.example;

/**
 * https://www.geeksforgeeks.org/system-design/flyweight-design-pattern/
 *
 */
public class Client {
    public static void main( String[] args ){
         IconFactory iconFactory = new IconFactory();

        // display file icons at different positions
        Icon fileIcon1 = iconFactory.getIcon("file");
        fileIcon1.display(100, 100);

        Icon fileIcon2 = iconFactory.getIcon("file");
        fileIcon2.display(150, 150);

        // display folder icons at different positions
        Icon folderIcon1 = iconFactory.getIcon("folder");
        folderIcon1.display(200, 200);

        Icon folderIcon2 = iconFactory.getIcon("folder");
        folderIcon2.display(250, 250);
    }
}
