package com.example.sp20250610.controller;

import com.example.sp20250610.common.Result;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebController {



    @GetMapping("/hello")
    public Result hello(){
        return Result.success("hello");
    }


}
