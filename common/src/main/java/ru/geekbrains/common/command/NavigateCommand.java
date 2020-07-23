package ru.geekbrains.common.command;

import java.io.File;
import java.io.Serializable;
import java.util.List;

public class NavigateCommand implements Serializable {
    private final List<File> navigates;

    public NavigateCommand(List<File> navigates) {
        this.navigates = navigates;

    }

    public List<File> getNavigates() {
        return navigates;
    }


}
