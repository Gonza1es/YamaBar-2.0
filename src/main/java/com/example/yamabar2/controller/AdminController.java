package com.example.yamabar2.controller;

import com.example.yamabar2.DTO.ProductDTO;
import com.example.yamabar2.entity.Product;
import com.example.yamabar2.response.MessageResponse;
import com.example.yamabar2.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;

    @Autowired
    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAdmin(@RequestParam(name = "p") String password) {
        if (!password.equals("HZrjORJGj"))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);

        List<ProductDTO> productList = productService.getList();

        return productList != null && !productList.isEmpty()
                ? new ResponseEntity<>(productList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/create")
    public ResponseEntity<?> saveProduct(@RequestPart(value = "file", required = false) MultipartFile file,
                                         @RequestPart("newProduct") Product product) {
        try {
            productService.saveProduct(product, file);
            return new ResponseEntity<>(new MessageResponse("Product saved"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("Error on the server, something went wrong."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {

        return new ResponseEntity<>(productService.deleteProduct(id));
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<MessageResponse> editProduct(@PathVariable Long id,
                                         @RequestPart(value = "file", required = false) MultipartFile file,
                                         @RequestPart("newProduct") Product product) {
        try {
            productService.editProduct(id, product, file);
            return new ResponseEntity<>(new MessageResponse("Edited"), HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(new MessageResponse("Error on the server, something went wrong."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}