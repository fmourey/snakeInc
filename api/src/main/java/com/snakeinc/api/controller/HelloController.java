package com.snakeinc.api.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api/v1/hello")
public class HelloController {

    @GetMapping
    public String hello(@RequestParam String name) {
        return "Hello " + name;
    }

    @PostMapping
    public String postHello(@RequestBody BodyParam name){
        return "post " + name.name();
    }

    private record BodyParam(String name) {
    }
}
