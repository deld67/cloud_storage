package ru.geekbrains.common.command;

import java.io.File;
import java.io.Serializable;

public class PutFileCommand implements Serializable {
    private final File file;

    public PutFileCommand(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
