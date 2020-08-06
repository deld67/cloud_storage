package ru.geekbrains.common.command;

import java.io.File;
import java.io.Serializable;

public class PutFileCommand implements Serializable {
    private final File srcfile;
    private final long fileSize;
    private final File dstFile;

    public PutFileCommand(File srcfile, File dstFile) {
        this.srcfile = srcfile;
        this.fileSize = srcfile.length();
        this.dstFile = dstFile;
    }

    public File getSrcfile() {
        return srcfile;
    }

    public File getDstFile() {
        return dstFile;
    }

    public long getFileSize() {
        return fileSize;
    }
}
