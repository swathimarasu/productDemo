package com.product.dao;

import com.product.model.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("select p from Product  p " +
            " where p.name = :name " +
            " and p.deptName = :deptName")
    List<Product> findByProductAndDeptNames(@Param("name") final String name,
                                            @Param("deptName") final String deptName);

    @Query("select p from Product  p " +
            " where p.name = :name ")
    List<Product> findByProductName(final String name);

    @Query("select p from Product  p " +
            " where p.deptName = :deptName")
    List<Product> findByDeptName(final String deptName);
}
