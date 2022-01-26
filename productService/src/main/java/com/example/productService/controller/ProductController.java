package com.example.productService.controller;

import com.example.productService.dto.MerchantProductDto;
import com.example.productService.dto.ProductDto;
import com.example.productService.dto.ProductMerchantReturnDto;
import com.example.productService.dto.Tmp;
import com.example.productService.entity.Product;
import com.example.productService.service.ProductService;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private DirectExchange exchange;

    @GetMapping(value = "/a")
    void test()
    {
        Tmp tmp = new Tmp("name");
        rabbitTemplate.convertAndSend(exchange.getName(),"routing.ProductSearch",tmp);
    }

    @GetMapping("/{id}")
    ProductDto getProduct(@PathVariable String id) {
        ProductDto productDto = new ProductDto();
        Product product = productService.get(id);

        BeanUtils.copyProperties(product, productDto);

        return productDto;
    }

    @GetMapping("")
    List<ProductDto> getAllProduct() {
        List<Product> products = productService.getAll();
        List<ProductDto> productDtos = new ArrayList<>();

        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            BeanUtils.copyProperties(product, productDto);
            productDtos.add(productDto);
        }
        return productDtos;
    }

    @PostMapping(value = "/addNewProduct")
    void saveNewProduct(@RequestBody ProductDto productDto) {
        Product product = new Product();

        BeanUtils.copyProperties(productDto, product);

        productService.add(product);

    }

    @RequestMapping(value = "/addProduct", method = {RequestMethod.POST, RequestMethod.PUT})
    void addProduct(@RequestBody MerchantProductDto merchantProductDto) {

        Product product = productService.get(merchantProductDto.getId());
        List<MerchantProductDto> merchantProductDtos = product.getMerchantProducts();
        boolean flag = false;

        for (MerchantProductDto merchantProductDto2 : merchantProductDtos)
            if (merchantProductDto2.getMerchantId().equals(merchantProductDto.getMerchantId())) {

                BeanUtils.copyProperties(merchantProductDto, merchantProductDto2);
                product.setMerchantProducts(merchantProductDtos);
                flag = true;
                break;
            }
        if (!flag) {
            product.addMerchantProduct(merchantProductDto);
        }

        product.sortMerchantProduct();
        productService.add(product);
        rabbitTemplate.convertAndSend(exchange.getName(),"routing.ProductSearch","abc");


    }

    @PutMapping("/updateMerchantProduct/{id}")
    void updateMerchantProduct(@RequestBody MerchantProductDto merchantProductDto, @PathVariable String id) {
        Product product = productService.get(id);
        List<MerchantProductDto> merchantProductDtos = product.getMerchantProducts();
        List<MerchantProductDto> merchantProductDtos1 = new ArrayList<>();

        for (MerchantProductDto merchantProductDto1 : merchantProductDtos) {

            if (merchantProductDto1.getMerchantId().equals(merchantProductDto.getMerchantId())) {

                merchantProductDto1.setPrice(merchantProductDto.getPrice());
                merchantProductDto1.setStock(merchantProductDto.getStock());
                merchantProductDto1.setWeight(merchantProductDto.getWeight());

            }
            merchantProductDtos1.add(merchantProductDto1);
        }
        product.setMerchantProducts(merchantProductDtos1);

        productService.add(product);
    }

    @GetMapping(value = "/getMerchantProducts/{merchantId}")
    List<ProductMerchantReturnDto> getMerchantProducts(@PathVariable Long merchantId) throws NullPointerException {

        List<ProductMerchantReturnDto> productMerchantReturnDtos = new ArrayList<>();
        List<Product> products = productService.getAll();


        try {
            for (Product product : products) {

                List<MerchantProductDto> merchantProductDtos = product.getMerchantProducts();
                if(merchantProductDtos != null) {
                    ProductMerchantReturnDto productMerchantReturnDto = new ProductMerchantReturnDto();

                    for (MerchantProductDto merchantProductDto1 : merchantProductDtos)
                        if (merchantProductDto1.getMerchantId()== merchantId) {
                            BeanUtils.copyProperties(merchantProductDto1,productMerchantReturnDto);
                            BeanUtils.copyProperties(product, productMerchantReturnDto);

                            productMerchantReturnDtos.add(productMerchantReturnDto);

                        }
                }
            }

        }

        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return productMerchantReturnDtos;

    }

    @RequestMapping(value ="/addInitialProduct" ,method = {RequestMethod.POST,RequestMethod.PUT})
    void addInitialProduct(@RequestBody ProductDto initialProductDto){

        Product product = new Product();
        BeanUtils.copyProperties(initialProductDto,product);
        productService.add(product);

    }


    @DeleteMapping(value = "/deleteProduct/{id}")
    void deleteProduct(@PathVariable String id)
    {
        productService.delete(id);
    }


    @GetMapping(value = "/getProduct/{productId}/{merchantId}")
    ProductMerchantReturnDto getProduct(@PathVariable("productId") String productId,@PathVariable("merchantId")Long merchantId){
        ProductMerchantReturnDto productMerchantReturnDto = new ProductMerchantReturnDto();

        Product product = productService.get(productId);
        System.out.println(product.getId()+product.getMerchantProducts());
        List<MerchantProductDto> merchantProductDtos = product.getMerchantProducts();

        for(MerchantProductDto merchantProductDto : merchantProductDtos){
            System.out.println(merchantProductDto.getMerchantId() + "outer" + merchantId);
            if(merchantProductDto.getMerchantId()==merchantId){

                System.out.println(merchantProductDto.getMerchantId() + "dkfjkjdshfkjhdsf");
                BeanUtils.copyProperties(product,productMerchantReturnDto);
                BeanUtils.copyProperties(merchantProductDto,productMerchantReturnDto);
                break;
            }
        }
        return productMerchantReturnDto;
    }

}
