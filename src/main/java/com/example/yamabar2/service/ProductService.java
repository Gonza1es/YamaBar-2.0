package com.example.yamabar2.service;

import com.example.yamabar2.DTO.ProductDTO;
import com.example.yamabar2.entity.Image;
import com.example.yamabar2.entity.Product;
import com.example.yamabar2.repository.ImageRepository;
import com.example.yamabar2.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }

    public List<ProductDTO> getList() {
        List<Product> productList = productRepository.findAll();
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product product : productList) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setType(product.getType());
            productDTO.setName(product.getName());
            productDTO.setPrice(product.getPrice());
            productDTO.setDescription(product.getDescription());
            productDTO.setImageId(product.getImageId());

            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    public void saveProduct(Product product, MultipartFile file) throws IOException {
        if (file != null ) {
            if (file.getSize() != 0) {
                Image image = toEntityImage(file);
                product.addImageToProduct(image);
                Product productFromDB = productRepository.save(product);
                productFromDB.setImageId(productFromDB.getImages().get(0).getId());
            }
        }
        productRepository.save(product);
    }


    public void editProduct(Long id, Product product, MultipartFile file) throws IOException {
        Product productFromDB = productRepository.findById(id).orElse(null);
        if (productFromDB != null) {
            productFromDB.setType(product.getType());
            productFromDB.setName(product.getName());
            productFromDB.setPrice(product.getPrice());
            productFromDB.setDescription(product.getDescription());
            if (file != null) {
                if (file.getSize() != 0 && file.getSize() < 1048576L) {
                    if (productFromDB.getImages().isEmpty()) {
                        Image image = toEntityImage(file);
                        productFromDB.addImageToProduct(image);
                        Product editProduct = productRepository.save(productFromDB);
                        editProduct.setImageId(editProduct.getImages().get(0).getId());
                    } else {
                        Image image = setEntityImage(productFromDB, file);
                        imageRepository.save(image);
                    }
                }
            }
            productRepository.save(productFromDB);
        }
    }

    private Image setEntityImage(Product product, MultipartFile file) throws IOException {
        Image image = imageRepository.findById(product.getImageId()).orElse(null);
        if (image !=null) {
            image.setName(file.getName());
            image.setOriginalFileName(file.getOriginalFilename());
            image.setContentType(file.getContentType());
            image.setSize(file.getSize());
            image.setImageBytes(file.getBytes());
        }
        return image;
    }

    public HttpStatus deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);

        if (product != null) {
            productRepository.deleteById(id);
            return HttpStatus.OK;
        } else return HttpStatus.NOT_FOUND;

    }

    private Image toEntityImage(MultipartFile file) throws IOException {
        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setImageBytes(file.getBytes());

        return image;
    }

}
