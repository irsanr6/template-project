package com.irsan.templateproject.controller;

import com.irsan.templateproject.exception.AppException;
import com.irsan.templateproject.utility.annotation.Logging;
import com.irsan.templateproject.utility.helper.ResponseHelper;
import com.irsan.templateproject.utility.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import static com.irsan.templateproject.utility.constant.GlobalConstant.ERROR_STACKTRACE_LOG_PATH;
import static com.irsan.templateproject.utility.constant.GlobalConstant.SUCCESS;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 27/05/2024
 */
@RestController
@RequestMapping("api/v1/log")
@RequiredArgsConstructor
public class LogController {

    private final FileUtil fileUtil;

    @Logging(traceId = true)
    @GetMapping("stack-trace")
    public ResponseEntity<?> getLogStackTrace() {
        List<Map<String, Object>> files = fileUtil.getFiles(ERROR_STACKTRACE_LOG_PATH);
        return ResponseHelper.build(files, SUCCESS, HttpStatus.OK);
    }

    @Logging(traceId = true)
    @GetMapping("stack-trace/print/{fileName}")
    public String printLogStackTrace(@PathVariable String fileName) {
        File file = new File(ERROR_STACKTRACE_LOG_PATH + fileName);
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            return new String(bytes);
        } catch (IOException e) {
            throw new AppException("Error reading file");
        }
    }

}
