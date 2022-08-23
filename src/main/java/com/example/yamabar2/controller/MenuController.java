package com.example.yamabar2.controller;

import com.example.yamabar2.DTO.ProductDTO;
import com.example.yamabar2.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@CrossOrigin
public class MenuController {

    private final ProductService productService;

    @Autowired
    public MenuController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<ProductDTO>> getMenu() {
        List<ProductDTO> products = productService.getList();

        return products != null && !products.isEmpty()
                ? new ResponseEntity<>(products, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
