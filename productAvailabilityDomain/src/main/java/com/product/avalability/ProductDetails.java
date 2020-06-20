package com.product.avalability;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetails {

    private Long id;
    private String name;
    private long deptId;
    private String deptName;

}
