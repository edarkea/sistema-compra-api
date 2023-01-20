package org.edarkea.compra.api.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/test")
public class TestController {

    @RequestMapping(value = "all", method = RequestMethod.GET)
    public ResponseEntity<List<String>> obtenerTodosItems() {
        return new ResponseEntity(new ArrayList<String>() {
            {
                add("Valor 1");
                add("Valor 2");
                add("Valor 3");
                add("Valor 4");
            }
        }, HttpStatus.OK);
    }

}
