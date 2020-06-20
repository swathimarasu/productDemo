package com.balance.controller;

import com.balance.model.AvailableProductEntity;
import com.balance.service.ProductsAvailabilityService;
import com.product.avalability.ProductsAvailableDoc;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class ProductsAvailabilityControllerTest {
    private static EasyRandom easyRandom;

    @InjectMocks
    private ProductsAvailabilityController productsAvailabilityController;

    @Mock
    ProductsAvailabilityService productsAvailabilityService;

    @BeforeClass
    public static void oneTimeSetUp() {

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
    }

    @Test
    public void testSaveAvailableProduct() {

        final AvailableProductEntity availableProductEntity = easyRandom.nextObject(AvailableProductEntity.class);

        when(productsAvailabilityService.save(any())).thenReturn(availableProductEntity);

        final ResponseEntity<AvailableProductEntity> result = productsAvailabilityController.saveAvailableProduct(availableProductEntity);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertTrue(result.hasBody());
        assertEquals(availableProductEntity, result.getBody());
    }

    @Test
    public void testGetAvailableProductsByLocationId() {

        final long locationId = easyRandom.nextLong();

        final List<ProductsAvailableDoc> availableDocs = Arrays.asList(easyRandom.nextObject(ProductsAvailableDoc[].class));

        when(productsAvailabilityService.getAvailableProductsByLocId(anyLong())).thenReturn(availableDocs);

        final ResponseEntity<List<ProductsAvailableDoc>> result = productsAvailabilityController.getAvailableProductsByLocationId(locationId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertTrue(result.hasBody());
        assertEquals(availableDocs, result.getBody());
    }
}
