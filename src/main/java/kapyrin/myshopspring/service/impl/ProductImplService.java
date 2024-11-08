package kapyrin.myshopspring.service.impl;

import kapyrin.myshopspring.entity.Product;
import kapyrin.myshopspring.exception.entity.ProductException;
import kapyrin.myshopspring.repository.ProductRepository;
import kapyrin.myshopspring.service.interfaces.CrudOneParameterInMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductImplService implements CrudOneParameterInMethod<Product> {
    private final ProductRepository productRepository;

    @Autowired
    public ProductImplService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void deleteById(long id) {
        log.info("Deleting product with id {}", id);
        try {
            productRepository.deleteById(id);
            log.info("Deleted product with id {}", id);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductException("Failed to delete product with id " + id);
        }

    }

    @Override
    public Optional<Product> getById(long id) {
        log.info("Getting product with id {}", id);
        try {
            Optional<Product> product = productRepository.findById(id);
            log.info("Found product with id {}", id);
            return product;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductException("Failed to find product with id " + id);
        }
    }

    @Override
    public void add(Product entity) {
        log.info("Adding product {}", entity);
        try {
            productRepository.save(entity);
            log.info("Added product {}", entity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductException("Failed to add product " + entity);
        }
    }

    @Override
    public void update(Product entity) {
        log.info("Updating product {}", entity);
        try {
            productRepository.save(entity);
            log.info("Updated product {}", entity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductException("Failed to update product " + entity);
        }
    }

    @Override
    public void deleteByEntity(Product entity) {
        log.info("Deleting product {}", entity);
        try {
            productRepository.delete(entity);
            log.info("Deleted product {}", entity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductException("Failed to delete product " + entity);
        }
    }

    @Override
    public List<Product> getAll() {
        log.info("Getting all products");
        try {
            List<Product> products = productRepository.findAll();
            log.info("Found {} products", products.size());
            return products;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductException("Failed to find all products");
        }
    }
}
