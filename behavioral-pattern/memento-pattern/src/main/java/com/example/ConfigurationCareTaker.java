package com.example;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationCareTaker {
    private List<ConfigurationMemento> mementoList;

    ConfigurationCareTaker() {
        mementoList = new ArrayList<>();
    }

    void saveMemento(ConfigurationMemento memento) {
        mementoList.add(memento);
    }

    ConfigurationMemento undo() {
        if (!mementoList.isEmpty()) {
            int lastIndex = mementoList.size() - 1;
            var configurationMemento = mementoList.get(lastIndex);
            mementoList.remove(lastIndex);
            return configurationMemento;
        }
        return null;
    }
}
