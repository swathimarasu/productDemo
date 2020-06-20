package com.product.controller;

import com.product.avalability.ProductDetails;
import com.product.model.Product;
import com.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@RestController
@RequestMapping(value="/products")
public class ProductController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ProductService productService;

    @Autowired
    public ProductController(final ProductService productService){
        this.productService = productService;
    }



    @RequestMapping(value="/create",method = RequestMethod.POST,consumes = "application/json",produces = "application/json")
    public ResponseEntity<Product> saveProduct(@RequestBody Product product){

        log.info("Saving product details in the database.");
        final Product productDetails = productService.save(product);
        return new ResponseEntity<>(productDetails,HttpStatus.CREATED);

    }

    @RequestMapping(value="/getAll",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<List<ProductDetails>> getProducts(){

        log.info("retrieve product details from the database.");
        final List<ProductDetails> products = productService.getAllProducts();
        return new ResponseEntity<>(products,HttpStatus.OK);

    }


    @RequestMapping(value="/{productId}",method = RequestMethod.GET,produces = "application/json")
    public ResponseEntity<ProductDetails>  getProductByID(@PathVariable("productId") final Long productId){

        log.info("retrieve product details from the database.");
        final ProductDetails product = productService.getByProductId(productId);
        return new ResponseEntity<>(product,HttpStatus.OK);

    }

    @RequestMapping(value="/update/{productId}",method = RequestMethod.PUT,produces = "application/json")
    public ResponseEntity<ProductDetails>  updateProduct(@RequestBody final Product product,
                                                    @PathVariable("productId") final Long productId){

        log.info("Update Product details in the database");

        return new ResponseEntity<>(productService.update(product,productId),HttpStatus.OK);

    }


    @RequestMapping(value="/{productId}",method = RequestMethod.DELETE,produces = "application/json")
    public ResponseEntity<Void>  removeProduct(@PathVariable("productId") final Long productId){

        log.info("remove Product details from the database by productId",productId);
        productService.removeProductById(productId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
