package com.balance.service;

import com.balance.dao.ProductsAvailabilityRepository;
import com.balance.exception.InvalidDataException;
import com.balance.exception.MicroServiceException;
import com.balance.mapper.ProductsAvailabilityMapper;
import com.balance.model.AvailableProductEntity;
import com.product.avalability.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductsAvailabilityService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private static final String SLASH = "/";
    private static final String PRODUCTS = "products";
    private static final String PRODUCTS_DETAILS = "products/details";
    private static final String LOCATIONS = "locations";
    private final ProductsAvailabilityMapper productsAvailabilityMapper;
    private final ProductsAvailabilityRepository productsAvailabilityRepository;
    private final String productEndpoint;
    private final String locationEndpoint;
    private final RestTemplate restTemplate;

    final ProductsAvailableResponse EMPTY_RESPONSE = ProductsAvailableResponse.builder()
            .productsAvailableDocs(Collections.emptyList()).build();

    @Autowired
    public ProductsAvailabilityService(@Value("${product.service.endpoint}") final String productEndpoint,
                                       @Value("${location.service.endpoint}") final String locationEndpoint,
                                       final ProductsAvailabilityMapper mapper,
                                       final ProductsAvailabilityRepository availabilityRepository,
                                       final RestTemplate restTemplate) {

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

        if (CollectionUtils.isEmpty(availableProductEntities)) {
            return Collections.emptyList();
        }

        final List<ProductsAvailableDoc> productsAvailabilityList = new ArrayList<>();
        availableProductEntities.stream().forEach(availableProductEntity -> {

            final LocationDetails locationDetails = restTemplate.getForEntity(locationUrl.concat("" + availableProductEntity.getLocationId()), LocationDetails.class).getBody();
            final ProductDetails productDetails = restTemplate.getForEntity(productUrl.concat("" + availableProductEntity.getProductId()), ProductDetails.class).getBody();
            ;
            productsAvailabilityList.add(productsAvailabilityMapper.toProductsAvailabilityDoc(locationDetails, productDetails, availableProductEntity.getBalance()));

        });
        return productsAvailabilityList;

    }

    public ProductsAvailableResponse getAvailableProducts(final String item, final String department) {

        if (!isValidData(item, department)) {
            throw new InvalidDataException("invalid Input");
        }

        try {

            final String productUrl = productEndpoint.concat(SLASH).concat(PRODUCTS_DETAILS).concat(SLASH);
            final String locationUrl = locationEndpoint.concat(SLASH).concat(LOCATIONS).concat(SLASH);

            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(productUrl)
                    .queryParam("product", item)
                    .queryParam("department", department);

            final ProductsDetailsListResponse productDetails = restTemplate.getForEntity(builder.toUriString(),ProductsDetailsListResponse.class).getBody();

            if (productDetails != null && CollectionUtils.isNotEmpty(productDetails.getProductDetails())) {
                return getProductsAvailableDocs(locationUrl, productDetails.getProductDetails());
            }

            return EMPTY_RESPONSE;

        } catch (final Exception ex) {
            log.error(ex.getLocalizedMessage(), ex);
            throw new MicroServiceException("Failed to get Items details");
        }

    }

    private boolean isValidData(String item, String department) {
        if (StringUtils.isNotBlank(item) || StringUtils.isNotBlank(department)) {
            return true;
        }
        return false;

    }

    private ProductsAvailableResponse getProductsAvailableDocs(final String locationUrl, final List<ProductDetails> productDetails) {

        final List<Long> productIds = productDetails.stream()
                .filter(Objects::nonNull)
                .map(ProductDetails::getId)
                .collect(Collectors.toList());

        final Map<Long, ProductDetails> productDetailsMap = productDetails.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(ProductDetails::getId, v -> v));

        final List<AvailableProductEntity> availableProductEntities = productsAvailabilityRepository.getAvailableProductsByProductId(productIds);
        if (CollectionUtils.isEmpty(availableProductEntities)) {
            return EMPTY_RESPONSE;
        }
        final List<ProductsAvailableDoc> productsAvailabilityList = new ArrayList<>();
        availableProductEntities.stream()
                .filter(entity -> entity.getBalance() > 0)
                .forEach(availableProductEntity -> {
                    LocationDetails locationDetails = null;
                    try {
                        locationDetails = restTemplate.getForEntity(locationUrl.concat("" + availableProductEntity.getLocationId()), LocationDetails.class).getBody();
                    } catch (final Exception ex) {
                        log.error(ex.getLocalizedMessage(), ex);
                    }
                    productsAvailabilityList.add(productsAvailabilityMapper.toProductsAvailabilityDoc(locationDetails, productDetailsMap.get(availableProductEntity.getProductId()), availableProductEntity.getBalance()));

                });

        return ProductsAvailableResponse.builder()
                .productsAvailableDocs(productsAvailabilityList)
                .build();
    }
}
