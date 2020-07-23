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
                System.out.println(file.getPath());
                for (File f: files) {
                    if (!f.isHidden()) {
                        //navigates.add( new Navigate( f.getName(), f.getAbsolutePath(), f.isDirectory()) );
                        navigates.add(f);
                    }
                }
            }
        }
        return navigates;
    }


}
