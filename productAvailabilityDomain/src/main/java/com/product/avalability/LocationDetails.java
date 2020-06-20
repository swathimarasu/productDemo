package com.product.avalability;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationDetails {

        private long id;
        private String name;
        private String zipCode;
}
