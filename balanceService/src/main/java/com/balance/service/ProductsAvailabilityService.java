package com.balance.service;

import com.balance.dao.ProductsAvailabilityRepository;
import com.balance.mapper.ProductsAvailabilityMapper;
import com.balance.model.AvailableProductEntity;
import com.product.avalability.LocationDetails;
import com.product.avalability.ProductDetails;
import com.product.avalability.ProductsAvailableDoc;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductsAvailabilityService {


    private  static final String SLASH = "/";
    private static final String PRODUCTS = "products";
    private static final String LOCATIONS = "locations";
    private final ProductsAvailabilityMapper productsAvailabilityMapper;
    private final ProductsAvailabilityRepository productsAvailabilityRepository;
    private final String productEndpoint;
    private final String locationEndpoint;
    private final RestTemplate restTemplate;

    @Autowired
    public ProductsAvailabilityService(@Value("${product.service.endpoint}") final String productEndpoint,
                                       @Value("${location.service.endpoint}") final String locationEndpoint,
                                       final ProductsAvailabilityMapper mapper,
                                       final ProductsAvailabilityRepository availabilityRepository,
                                       final RestTemplate restTemplate){

        this.productsAvailabilityRepository = availabilityRepository;
        this.productsAvailabilityMapper = mapper;
        this.restTemplate = restTemplate;
        this.productEndpoint = productEndpoint;
        this.locationEndpoint = locationEndpoint;
    }


    public AvailableProductEntity save(final AvailableProductEntity availableProductEntity) {
        return productsAvailabilityRepository.save(availableProductEntity);
    }



    public List<ProductsAvailableDoc> getAvailableProductsByLocId(final long locationId) {

        final String productUrl = productEndpoint.concat(SLASH).concat(PRODUCTS).concat(SLASH);
        final String locationUrl = locationEndpoint.concat(SLASH).concat(LOCATIONS).concat(SLASH);

        final List<AvailableProductEntity> availableProductEntities = productsAvailabilityRepository.getAvailableProductsByLocationId(locationId);

        if(CollectionUtils.isEmpty(availableProductEntities)){
            return Collections.emptyList();
        }

        final List<ProductsAvailableDoc> productsAvailabilityList = new ArrayList<>();
        availableProductEntities.stream().forEach(availableProductEntity -> {

            final LocationDetails locationDetails = restTemplate.getForEntity(locationUrl.concat(""+availableProductEntity.getLocationId()),LocationDetails.class).getBody();
            final ProductDetails productDetails = restTemplate.getForEntity(productUrl.concat(""+availableProductEntity.getProductId()),ProductDetails.class).getBody();;
            productsAvailabilityList.add(productsAvailabilityMapper.toProductsAvailabilityDoc(locationDetails,productDetails,availableProductEntity.getBalance()));

        });
      return productsAvailabilityList;

    }
}
