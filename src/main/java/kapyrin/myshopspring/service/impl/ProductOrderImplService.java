package kapyrin.myshopspring.service.impl;

import kapyrin.myshopspring.entity.Product;
import kapyrin.myshopspring.entity.ProductOrder;
import kapyrin.myshopspring.entity.ProductOrderKey;
import kapyrin.myshopspring.exception.entity.ProductOrderException;
import kapyrin.myshopspring.repository.ProductOrderRepository;
import kapyrin.myshopspring.service.interfaces.ProductOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductOrderImplService implements ProductOrderService {
    private final ProductOrderRepository productOrderRepository;

    @Autowired
    public ProductOrderImplService(ProductOrderRepository productOrderRepository) {
        this.productOrderRepository = productOrderRepository;
    }

@Override
    public Optional<ProductOrder> getByIds(long orderId, long productId) {
        log.info("Get product order by id {}", orderId);
        try {
            Optional<ProductOrder> productOrder = productOrderRepository.findById(new ProductOrderKey(orderId, productId));
            log.info("Get product order by id {}", productOrder);
            return productOrder;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductOrderException("Cannot find product order by id " + orderId);
        }
    }


    @Override
    public void deleteByIds(long orderId, long productId) {
        log.info("Deleting ProductOrder by orderId: {} and productId: {}", orderId, productId);
        try {
            productOrderRepository.deleteById(new ProductOrderKey(orderId, productId));
            log.info("Deleted ProductOrder by orderId: {} and productId: {}", orderId, productId);
        } catch (Exception e) {
            log.error("Error deleting ProductOrder with orderId: {} and productId: {}", orderId, productId, e);
            throw new ProductOrderException("Failed to delete ProductOrder", e);
        }
    }

    @Override
    public void add(ProductOrder productOrder) {
        log.info("Adding ProductOrder {}", productOrder);
        try {
            productOrderRepository.save(productOrder);
            log.info("Added ProductOrder {}", productOrder);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductOrderException("Failed to add ProductOrder", e);
        }

    }

    @Override
    public void update(ProductOrder productOrder) {
        log.info("Updating ProductOrder {}", productOrder);
        try {
            productOrderRepository.save(productOrder);
            log.info("Updated ProductOrder {}", productOrder);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductOrderException("Failed to update ProductOrder", e);
        }

    }

    @Override
    public void deleteByEntity(ProductOrder productOrder) {
        log.info("Deleting ProductOrder {}", productOrder);
        try {
            productOrderRepository.delete(productOrder);
            log.info("Deleted ProductOrder {}", productOrder);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductOrderException("Failed to delete ProductOrder", e);
        }

    }

    @Override
    public List<ProductOrder> getAll() {
        log.info("Getting all ProductOrders");
        try {
            List<ProductOrder> productOrders = productOrderRepository.findAll();
            log.info("Getting all ProductOrders");
            return productOrders;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductOrderException("Failed to find all ProductOrders", e);
        }
    }

    @Override
    public List<Product> productFromProductOrder(long productOrderId) {
        log.info("Getting product from ProductOrder {}", productOrderId);
        try {
            List<Product> fromProductOrder = productOrderRepository.findProductsByOrderId(productOrderId);
            log.info("Getting product from ProductOrder {}", productOrderId);
            return fromProductOrder;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductOrderException("Failed to find product from ProductOrder", e);
        }
    }
}
