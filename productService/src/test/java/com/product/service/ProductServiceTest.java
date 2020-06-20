package com.product.service;

import com.product.exception.ProductNotFoundException;
import com.product.avalability.ProductDetails;
import com.product.dao.ProductRepository;
import com.product.mapper.ProductMapper;
import com.product.model.Product;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    private static EasyRandom easyRandom;
    private ProductService productService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ProductRepository productRepository;

    @Before
    public void setUp(){
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

        productService = new ProductService(productMapper,productRepository);
    }

    @Test
    public void testGetProducts(){

        final Iterable<Product> products = Arrays.asList(easyRandom.nextObject(Product[].class));

        final int size = getSize(products);

        final ProductDetails productDetails = easyRandom.nextObject(ProductDetails.class);

        when(productRepository.findAll()).thenReturn(products);
        when(productMapper.toProductDetailDoc(any())).thenReturn(productDetails);

        final List<ProductDetails> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(size,result.size());
    }

    @Test
    public void testGetProductById(){

        final Product product = easyRandom.nextObject(Product.class);
        final Long productId = easyRandom.nextLong();
        product.setId(productId);
        product.setDeptId(2L);
        product.setDeptName("Shirts");
        product.setName("Sleeves");

        final ProductDetails productDetails = easyRandom.nextObject(ProductDetails.class);
        productDetails.setDeptId(product.getDeptId());
        productDetails.setDeptName(product.getDeptName());
        productDetails.setId(productId);
        productDetails.setName(product.getName());

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(productMapper.toProductDetailDoc(any())).thenReturn(productDetails);

        final ProductDetails details = productService.getByProductId(productId);

        assertNotNull(details);
        assertEquals(details.getDeptId(),productDetails.getDeptId());
        assertEquals(productId,details.getId());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testGetProductByIdWithNull(){

        final Product product = easyRandom.nextObject(Product.class);
        final Long productId = easyRandom.nextLong();
        product.setId(productId);
        product.setDeptId(2L);
        product.setDeptName("Shirts");
        product.setName("Sleeves");

        final ProductDetails productDetails = easyRandom.nextObject(ProductDetails.class);
        productDetails.setDeptId(product.getDeptId());
        productDetails.setDeptName(product.getDeptName());
        productDetails.setId(productId);
        productDetails.setName(product.getName());

        when(productRepository.findById(anyLong())).thenReturn(null);

        productService.getByProductId(productId);

        verify(productMapper,never()).toProductDetailDoc(any());
    }

    @Test
    public void testSaveProduct(){

        final Product product = easyRandom.nextObject(Product.class);
        when(productRepository.save(product)).thenReturn(product);

        final Product result = productService.save(product);

        assertNotNull(result);
        assertEquals(result ,product);
    }

    @Test
    public void testDeleteProduct(){

        final Long productId = easyRandom.nextLong();
        final Product product = easyRandom.nextObject(Product.class);
        doNothing().when(productRepository).deleteById(anyLong());

        productService.removeProductById(productId);

        verify(productRepository).deleteById(anyLong());


    }

    @Test
    public void testUpdateProduct(){

        final Long productId = easyRandom.nextLong();
        final Product product = easyRandom.nextObject(Product.class);
        final ProductDetails details = easyRandom.nextObject(ProductDetails.class);
        details.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        when(productMapper.toProductDetailDoc(any())).thenReturn(details);

        final ProductDetails result = productService.update(product,productId);

        assertNotNull(result);
        assertEquals(productId,result.getId());

    }

    @Test
    public void testUpdateProductWithNoEntity(){

        final Long productId = easyRandom.nextLong();
        final Product product = easyRandom.nextObject(Product.class);
        final ProductDetails details = easyRandom.nextObject(ProductDetails.class);
        details.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.ofNullable(null));

        final ProductDetails result = productService.update(product,productId);

        assertNull(result);

    }

    private int getSize(Iterable<Product> products) {
        if(products instanceof Collection<?>){
            return ((Collection<?>)products).size();
        }

        return 0;
    }


}
