package ru.geekbrains.common.command;

import java.io.File;
import java.io.Serializable;

public class GetFileCommand implements Serializable {
    private final File file;

    public GetFileCommand(File file) {
        this.file = file;

    }

    public File getFile() {
        return file;
    }
}
