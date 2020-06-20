package com.product.avalability;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductsAvailableDoc {

    private LocationDetails location;
    private ProductDetails productDetails;
    private int availableCount;
}
