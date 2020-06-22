package com.product.avalability;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductsAvailableResponse {

    private List<ProductsAvailableDoc> productsAvailableDocs;
}
