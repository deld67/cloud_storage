package ru.geekbrains.common.command;

import java.io.File;
import java.io.Serializable;

public class GetFileCommand implements Serializable {
    private final File srcfile;
    private final long fileSize;
    private final File dstFile;

    public GetFileCommand(File srcfile, File dstFile) {
        this.srcfile = srcfile;
        this.dstFile = dstFile;
        this.fileSize = srcfile.length();

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
