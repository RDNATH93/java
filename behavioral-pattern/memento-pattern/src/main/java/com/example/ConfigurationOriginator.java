package com.example;

public class ConfigurationOriginator {
    private int height;
    private int width;

    ConfigurationOriginator(int height, int width) {
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    ConfigurationMemento createMemento() {
        return new ConfigurationMemento(height, width);
    }

    void restoreMemento(ConfigurationMemento memento) {
        this.height = memento.getHeight();
        this.width = memento.getWidth();
    }

}
