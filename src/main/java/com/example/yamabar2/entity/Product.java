package com.example.yamabar2.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String name;
    private String price;
    private String description;
    private Long imageId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY,
    mappedBy = "product")
    List<Image> images = new ArrayList<>();

    public void addImageToProduct(Image image) {
        image.setProduct(this);
        images.add(0, image);
    }
}
