package com.irsan.templateproject.utility.util;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 24/05/2024
 */
@Component
public class FileUtil {

    private final LoggerUtil log = new LoggerUtil(FileUtil.class);

    public void printStackTrace(Throwable e, String path) {
        File file = new File(path);
        try {
            boolean newFile = file.createNewFile();
            if (newFile) {
                log.info("File log stack trace created successfully");
            } else {
                log.warn("Failed to create file log stack trace");
            }
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            e.printStackTrace(ps);
            ps.close();
        } catch (IOException ex) {
            printStackTrace(ex, path);
        }
    }

}