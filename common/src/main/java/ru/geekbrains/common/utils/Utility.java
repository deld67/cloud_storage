package ru.geekbrains.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Utility {
    public static List<File> getFilesList(File file) throws IOException {
        List<File> navigates = new LinkedList<>();
        if (file.exists()){
            File[] files = file.listFiles();
            if (files.length > 0) {
                for (File f: files) {
                    if (!f.isHidden()) {
                        navigates.add(f);
                    }
                }
            }
        }
        return navigates;
    }

    public static List<String> getFilesString(File file) throws IOException {
        List<String> navigates = new LinkedList<>();
        if (file.exists()){
            File[] files = file.listFiles();
            if (files.length > 0) {
                for (File f: files) {
                    if (!f.isHidden()) {
                        navigates.add(f.getName());
                    }
                }
            }
        }
        return navigates;
    }

}
