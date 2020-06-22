package com.balance.service;

import com.balance.dao.ProductsAvailabilityRepository;
import com.balance.exception.InvalidDataException;
import com.balance.exception.MicroServiceException;
import com.balance.mapper.ProductsAvailabilityMapper;
import com.balance.mapper.impl.ProductsAvailabilityMapperImpl;
import com.balance.model.AvailableProductEntity;
import com.product.avalability.*;
import org.hamcrest.Matchers;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(MockitoJUnitRunner.class)
public class ProductsAvailabilityServiceTest {

    private static EasyRandom easyRandom;

    private ProductsAvailabilityService productsAvailabilityService;

    @Mock
    private ProductsAvailabilityMapper productsAvailabilityMapper;

    @Mock
    private ProductsAvailabilityRepository productsAvailabilityRepository;

    @Mock
    private RestTemplate restTemplate;


    private static final String productEndpoint = "http://localhost:8080";
    private static final String locationEndpoint = "http://localhost:8090";
    private  static final String SLASH = "/";
    private static final String PRODUCTS = "products";
    private static final String LOCATIONS = "locations";

    private static final ProductsAvailableResponse EMPTY_RESPONSE = ProductsAvailableResponse.builder()
            .productsAvailableDocs(Collections.emptyList()).build();

    @Before
    public void setUp() {
        final EasyRandomParameters parameters = new EasyRandomParameters()
                .collectionSizeRange(2, 3)
                .stringLengthRange(5, 10)
                .charset(Charset.forName("UTF-8"))
                .dateRange(LocalDate.now().minusDays(7), LocalDate.now())
                .timeRange(LocalTime.MIN, LocalTime.MAX)
                .scanClasspathForConcreteTypes(true)
                .objectPoolSize(20)
                .randomizationDepth(3)
                .overrideDefaultInitialization(false)
                .ignoreRandomizationErrors(true);

        easyRandom = new EasyRandom(parameters);

        productsAvailabilityService = new ProductsAvailabilityService(productEndpoint,
                locationEndpoint,
                productsAvailabilityMapper,
                productsAvailabilityRepository,
                restTemplate);
    }

    @Test
    public void testSaveAvailableProduct(){

        final AvailableProductEntity availableProductEntity = easyRandom.nextObject(AvailableProductEntity.class);

        when(productsAvailabilityRepository.save(any())).thenReturn(availableProductEntity);

        final AvailableProductEntity result = productsAvailabilityService.save(availableProductEntity);

        assertNotNull(result);
        assertEquals(availableProductEntity,result);

    }

    @Test
    public void testGetAvailableProductsByLocIdEmpty(){

        final long locationId = easyRandom.nextLong();

        final List<AvailableProductEntity> availableProductEntities = new ArrayList<>();

        when(productsAvailabilityRepository.getAvailableProductsByLocationId(anyLong())).thenReturn(availableProductEntities);

        final List<ProductsAvailableDoc> availableProducts = productsAvailabilityService.getAvailableProductsByLocId(locationId);

        assertEquals(availableProducts.size(),0);

        verify(productsAvailabilityRepository).getAvailableProductsByLocationId(anyLong());



    }

    @Test
    public void testGetAvailableProductsByLocId(){

        final ProductDetails productDetails = easyRandom.nextObject(ProductDetails.class);
        final LocationDetails locationDetails = easyRandom.nextObject(LocationDetails.class);
        final long locationId = easyRandom.nextLong();
        locationDetails.setId(locationId);

        final List<AvailableProductEntity> availableProductEntities = new ArrayList<>();
        final AvailableProductEntity availableProductEntity = new AvailableProductEntity();
        availableProductEntity.setId(easyRandom.nextLong());
        availableProductEntity.setLocationId(locationId);
        availableProductEntity.setProductId(productDetails.getId());
        availableProductEntity.setBalance(easyRandom.nextInt());
        availableProductEntities.add(availableProductEntity);

        final ProductsAvailableDoc availableDoc = ProductsAvailableDoc.builder()
                .availableCount(availableProductEntity.getBalance())
                .location(locationDetails).productDetails(productDetails).build();

        when(productsAvailabilityRepository.getAvailableProductsByLocationId(anyLong())).thenReturn(availableProductEntities);
        when(restTemplate.getForEntity(anyString(), eq(ProductDetails.class))).thenReturn(new ResponseEntity(productDetails, HttpStatus.OK));
        when(restTemplate.getForEntity(anyString(),eq(LocationDetails.class))).thenReturn(new ResponseEntity(locationDetails, HttpStatus.OK));
        when(productsAvailabilityMapper.toProductsAvailabilityDoc(any(),any(),anyInt())).thenReturn(availableDoc);

        final List<ProductsAvailableDoc> availableProducts = productsAvailabilityService.getAvailableProductsByLocId(locationId);

        assertNotNull(availableProducts);
        assertEquals(availableProductEntities.size(),availableProducts.size());
        assertEquals(availableProductEntity.getLocationId(),availableProducts.get(0).getLocation().getId());
        assertEquals(availableProductEntity.getProductId(),availableProducts.get(0).getProductDetails().getId().longValue());
        assertEquals(availableProductEntity.getBalance(),availableProducts.get(0).getAvailableCount());

        verify(productsAvailabilityRepository,times(1)).getAvailableProductsByLocationId(anyLong());

    }

    @Test(expected = InvalidDataException.class)
    public void testInvalidDataForGetAvailableProducts(){

        productsAvailabilityService.getAvailableProducts(null,null);

    }

    @Test
    public void testEmptyGetAvailableProducts(){

        final String item = easyRandom.nextObject(String.class);
        final String department = easyRandom.nextObject(String.class);


        final ProductsDetailsListResponse productDetails = easyRandom.nextObject(ProductsDetailsListResponse.class);
        productDetails.setProductDetails(Collections.emptyList());

        when(restTemplate.getForEntity(anyString(), eq(ProductsDetailsListResponse.class))).thenReturn(new ResponseEntity(productDetails, HttpStatus.OK));
        final ProductsAvailableResponse result = productsAvailabilityService.getAvailableProducts(item,department);

        assertNotNull(result);
        assertEquals(0,result.getProductsAvailableDocs().size());

    }

    @Test(expected = MicroServiceException.class)
    public void testMicroServiceExceptionGetAvailableProducts(){

        final String item = easyRandom.nextObject(String.class);
        final String department = easyRandom.nextObject(String.class);


        final ProductsDetailsListResponse productDetails = easyRandom.nextObject(ProductsDetailsListResponse.class);
        productDetails.setProductDetails(Collections.emptyList());

        when(restTemplate.getForEntity(anyString(), eq(ProductsDetailsListResponse.class))).thenThrow(new MicroServiceException("Exception in Service"));
        productsAvailabilityService.getAvailableProducts(item,department);

    }

    @Test
    public void testEmptyGetAvailableProductDefaultResponse(){

        final String item = easyRandom.nextObject(String.class);
        final String department = easyRandom.nextObject(String.class);


        final ProductsDetailsListResponse productDetails = easyRandom.nextObject(ProductsDetailsListResponse.class);

        when(restTemplate.getForEntity(anyString(), eq(ProductsDetailsListResponse.class))).thenReturn(new ResponseEntity(productDetails, HttpStatus.OK));
        when(productsAvailabilityRepository.getAvailableProductsByProductId(anyList())).thenReturn(new ArrayList<>());
        final ProductsAvailableResponse result = productsAvailabilityService.getAvailableProducts(item,department);

        assertNotNull(result);
        assertEquals(0,result.getProductsAvailableDocs().size());
        assertEquals(EMPTY_RESPONSE,result);

    }

    @Test
    public void testGetAvailableProducts(){

        final String item = easyRandom.nextObject(String.class);
        final String department = easyRandom.nextObject(String.class);
        final LocationDetails locationDetails = easyRandom.nextObject(LocationDetails.class);
        final ProductDetails productDetails = easyRandom.nextObject(ProductDetails.class);

        final ProductsDetailsListResponse productsDetailsListResponse = easyRandom.nextObject(ProductsDetailsListResponse.class);

        when(restTemplate.getForEntity(anyString(), eq(ProductsDetailsListResponse.class))).thenReturn(new ResponseEntity(productsDetailsListResponse, HttpStatus.OK));
        when(restTemplate.getForEntity(anyString(),eq(LocationDetails.class))).thenReturn(new ResponseEntity(locationDetails, HttpStatus.OK));

        List<AvailableProductEntity> availableProductEntities = Arrays.asList(easyRandom.nextObject(AvailableProductEntity[].class));
        availableProductEntities = availableProductEntities.stream().map(entity-> {
            entity.setProductId(productsDetailsListResponse.getProductDetails().get(0).getId());
            entity.setBalance(10);
            return entity;
        }).collect(Collectors.toList());

        when(productsAvailabilityRepository.getAvailableProductsByProductId(anyList())).thenReturn(availableProductEntities);

        final ProductsAvailableDoc availableDoc = ProductsAvailableDoc.builder()
                .availableCount(availableProductEntities.get(0).getBalance())
                .location(locationDetails).productDetails(productDetails).build();
        when(productsAvailabilityMapper.toProductsAvailabilityDoc(any(),any(),anyInt())).thenReturn(availableDoc);

        final ProductsAvailableResponse result = productsAvailabilityService.getAvailableProducts(item,department);

        assertNotNull(result);
        assertNotNull(result.getProductsAvailableDocs());
        assertEquals(availableProductEntities.size(),result.getProductsAvailableDocs().size());

    }





}
