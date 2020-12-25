package ru.geekbrains.shop7.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.university.model.Product;
import ru.geekbrains.university.repositories.ProductRepository;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // получение товара по id
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productRepository.findById(id).get();
    }

    // получение всех товаров
    @GetMapping
    public List<Product> getAllProduct() {
        List<Product> allProducts = productRepository.findAll();
        return allProducts;
    }

    // создание нового товара
    @PostMapping
    public void save(@RequestBody Product product) {
        if (productRepository.getProductByIdExistsAndNameTrueAndPriceIsTrue (product)) {
             productRepository.save(product);
        } else {
            System.out.println ("this product is in the database");
        }
    }

    // удаление  товара
    @PostMapping("/{id}")
    public void removeProductById(@PathVariable Long id) {
        productRepository.deleteById (id);
    }

    // получение товаров дороже min цены
    @GetMapping
    public List<Product> getProductsAfterMinPrice() {
        int minPrice = productRepository.min();
        return productRepository.findAllProductsByMaxPrice (minPrice);
    }

    // получение товаров дешевле max цены
    @GetMapping
    public List<Product> getProductsBeforeMaxPrice() {
        int maxPrice = productRepository.max ();
        return productRepository.findAllProductsByMinPrice (maxPrice);
    }

    // получение товаров между max и min ценой
    @GetMapping
    public List<Product> getProductsBetweenMinAndMax() {
        int maxPrice = productRepository.max ();
        int minPrice = productRepository.min ();
        return productRepository.findAllProductsBetweenMinMax(minPrice, maxPrice);
    }

}
