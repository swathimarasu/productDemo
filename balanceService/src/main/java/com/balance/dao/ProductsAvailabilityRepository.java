package com.balance.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.balance.model.AvailableProductEntity;

import java.util.*;

@Repository
public interface ProductsAvailabilityRepository extends JpaRepository<AvailableProductEntity,Long> {

    @Query("select ap from AvailableProductEntity  ap " +
            "where locationId = :locationId ")
    List<AvailableProductEntity> getAvailableProductsByLocationId(@Param("locationId") final long locationId);

}
