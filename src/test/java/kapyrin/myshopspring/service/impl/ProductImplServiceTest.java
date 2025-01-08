package kapyrin.myshopspring.service.impl;

import kapyrin.myshopspring.AbstractTest;
import kapyrin.myshopspring.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)

class ProductImplServiceTest extends AbstractTest {
    @Autowired
    private ProductImplService productImplService;
    @Autowired
    ServiceTestUtil serviceTestUtil;
    private Product product;

    @Test
    void deleteById() {
        Optional<Product> productOptional = productImplService.getById(getProductId());
        assertTrue(productOptional.isPresent());

        productImplService.deleteById(getProductId());
        Optional<Product> deleteProductOptional = productImplService.getById(getProductId());
        assertFalse(deleteProductOptional.isPresent());
    }

    @Test
    void getById() {
        Optional<Product> productOptional = productImplService.getById(getProductId());
        assertTrue(productOptional.isPresent());

        Optional<Product> deleteProductOptional = productImplService.getById(100);
        assertFalse(deleteProductOptional.isPresent());
    }

    @Test
    void add() {
        Product addedProduct = Product.builder()
                .productName("AddedTestProduct")
                .productDescription("Add some product")
                .price(100.0)
                .productRemain(50)
                .build();

        productImplService.add(addedProduct);
        Optional<Product> productOptional = productImplService.getById(addedProduct.getId());
        assertTrue(productOptional.isPresent());
    }

    @Test
    void update() {
        String description = "Updated description";
        String productName = "Updated productName";
        Optional<Product> productOptional = productImplService.getById(getProductId());
        product = productOptional.get();
        product.setProductName(productName);
        product.setProductDescription(description);
        productImplService.update(product);

        Optional<Product> updatedProduct = productImplService.getById(getProductId());
        assertEquals(description, updatedProduct.get().getProductDescription());
        assertEquals(productName, updatedProduct.get().getProductName());
    }

    @Test
    void deleteByEntity() {
        Optional<Product> productOptional = productImplService.getById(getProductId());
        assertTrue(productOptional.isPresent());

        productImplService.deleteByEntity(product);
        Optional<Product> deleteProductOptional = productImplService.getById(getProductId());
        assertFalse(deleteProductOptional.isPresent());
    }

    @Test
    void getAll() {
        List<Product> products = productImplService.getAll();
        assertTrue(products.size() > 0);
    }

    @Override
    protected void createTestEntity() {
        product = serviceTestUtil.product();

    }

    @Override
    protected void saveTestEntity() {
        productImplService.add(product);
    }

    private Long getProductId() {
        return product.getId();
    }
}