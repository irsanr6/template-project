package com.irsan.templateproject.controller;

import com.irsan.templateproject.exception.AppException;
import com.irsan.templateproject.model.enumeration.Role;
import com.irsan.templateproject.utility.annotation.Logging;
import com.irsan.templateproject.utility.annotation.RolesAllowed;
import com.irsan.templateproject.utility.helper.GlobalHelper;
import com.irsan.templateproject.utility.helper.ResponseHelper;
import com.irsan.templateproject.utility.util.FileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @RolesAllowed(roles = {Role.ROLE_SUPER_ADMIN})
    @GetMapping("stack-trace")
    public ResponseEntity<?> getLogStackTrace(@RequestParam(required = false, defaultValue = "", value = "trace-id") String traceId) {
        List<Map<String, Object>> files = fileUtil.getFiles(ERROR_STACKTRACE_LOG_PATH).stream()
                .filter(file -> {
                    if (!traceId.isEmpty()) {
                        return GlobalHelper.getString(file.get("traceId")).equals(traceId);
                    }

                    return true;
                }).collect(Collectors.toList());
        return ResponseHelper.build(files, SUCCESS, HttpStatus.OK);
    }

    @Logging(traceId = true)
    @RolesAllowed(roles = {Role.ROLE_SUPER_ADMIN})
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

    @Logging(traceId = true)
    @RolesAllowed(roles = {Role.ROLE_SUPER_ADMIN})
    @DeleteMapping("stack-trace/delete-all")
    public ResponseEntity<?> deleteLogStackTrace(@RequestParam(required = false, defaultValue = "", value = "file-name") String fileName) {
        fileUtil.deleteFiles(fileName);
        return ResponseHelper.build("Success delete log file", SUCCESS, HttpStatus.OK);
    }

}
