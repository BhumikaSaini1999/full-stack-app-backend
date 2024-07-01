package com.fullstackapp.ecommerce.dao;

import com.fullstackapp.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

//JpaRepository<Entity Type, Primary Key Type>
@RepositoryRestResource
public interface ProductRepository extends JpaRepository<Product, Long> {
    //Query Method - Match by category id
    //Behind the scenes - Spring will execute query similar to this - SELECT * from product where category_id=?
    Page<Product> findByCategoryId(@Param("id") Long id, Pageable pageable);

    //Behind the scenes - Spring will execute query similar to this - SELECT * from Product p WHERE p.name LIKE CONCAT('%', :name, '%')
    Page<Product> findByNameContaining(@Param("name") String name, Pageable pageable);
}
