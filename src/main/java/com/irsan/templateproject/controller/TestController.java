package com.irsan.templateproject.controller;

import com.irsan.templateproject.model.TestRequest;
import com.irsan.templateproject.utility.helper.ResponseHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.irsan.templateproject.utility.constant.GlobalConstant.SUCCESS;

/**
 * @author : Irsan Ramadhan
 * @email : irsan.ramadhan@iconpln.co.id
 * @date : 17/05/2024
 */
@RestController
@RequestMapping("api/v1/test")
public class TestController {

    @GetMapping
    public ResponseEntity<?> test(@RequestParam String name) {
        String message = String.format("Hello %s, my name Irsan Ramadhan", name);
        return ResponseHelper.build(message, SUCCESS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> testPost(@RequestBody TestRequest request) {
        String message = String.format("Hello %s, my name Irsan Ramadhan. Your message (%s) has been received", request.getName(), request.getMessage());
        return ResponseHelper.build(message, SUCCESS, HttpStatus.OK);
    }

}
