package ru.geekbrains.shop7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.geekbrains.university.model.Product;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    default boolean getProductByIdExistsAndNameTrueAndPriceIsTrue(Product product) {
        boolean b = false;
        return b;
    }

    @Query("select min(price) from Product")
    public int min();

    @Query("select max(price) from Product")
    public int max();

    List<Product> findAllProductsBetweenMinMax(int min, int max);
    List<Product> findAllProductsByMaxPrice (int min);
    List<Product> findAllProductsByMinPrice (int max);

}
