package com.irsan.templateproject.utility.util;

import com.irsan.templateproject.exception.AppException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

import static com.irsan.templateproject.utility.constant.GlobalConstant.ERROR_STACKTRACE_LOG_PATH;

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
        File parentFile = file.getParentFile();
        try {
            if (!parentFile.exists()) {
                boolean mkdirs = file.getParentFile().mkdirs();
                if (!mkdirs) {
                    log.warn("Failed to create directory log stack trace");
                    return;
                }
            }
            boolean newFile = file.createNewFile();
            if (!newFile) {
                log.warn("Failed to create file log stack trace");
                return;
            }
            FileOutputStream fos = new FileOutputStream(file);
            PrintStream ps = new PrintStream(fos);
            e.printStackTrace(ps);
            ps.close();
        } catch (IOException ex) {
            printStackTrace(ex, path);
        }
    }

    public List<Map<String, Object>> getFiles(String dir) {
        try {
            File file = new File(dir);
            File[] files = file.listFiles();

            assert files != null;
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));

            return Arrays.stream(files)
                    .filter(f -> !f.isDirectory())
                    .map(f -> {
                        Map<String, Object> map = new LinkedHashMap<>();
                        map.put("fileName", f.getName());
                        map.put("traceId", f.getName().replaceAll("^logging_|\\.log$", ""));
                        map.put("lastModified", f.lastModified());
                        return map;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void deleteFiles(String fileName) {
        if (!fileName.isEmpty()) {
            File file = new File(ERROR_STACKTRACE_LOG_PATH + fileName);
            boolean deleted = file.delete();
            if (!deleted) {
                throw new AppException("Error deleting file, log file not found");
            } else {
                return;
            }
        }
        File file = new File(ERROR_STACKTRACE_LOG_PATH);
        File[] files = file.listFiles();
        if (files != null) {
            for (File f : files) {
                if (!f.isDirectory()) {
                    if (!f.delete()) {
                        throw new AppException("Error deleting file, log files was empty");
                    }
                }
            }
        }
    }

}