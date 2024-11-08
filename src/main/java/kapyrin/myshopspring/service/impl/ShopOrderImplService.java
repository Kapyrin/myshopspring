package kapyrin.myshopspring.service.impl;

import kapyrin.myshopspring.entity.ShopOrder;
import kapyrin.myshopspring.exception.entity.OrderException;
import kapyrin.myshopspring.exception.entity.OrderStatusException;
import kapyrin.myshopspring.repository.ShopOrderRepository;
import kapyrin.myshopspring.service.interfaces.ShopOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ShopOrderImplService implements ShopOrderService {

    private final ShopOrderRepository shopOrderRepository;

    @Autowired
    public ShopOrderImplService(ShopOrderRepository shopOrderRepository) {
        this.shopOrderRepository = shopOrderRepository;
    }


    @Override
    public void add(ShopOrder entity) {
        log.info("Add shop order: {}", entity);
        try {
            shopOrderRepository.save(entity);
            log.info("Added shop order: {}", entity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new OrderException("Cannot add order", e);
        }

    }

    @Override
    public void update(ShopOrder entity) {
        log.info("Update shop order: {}", entity);
        try {
            shopOrderRepository.save(entity);
            log.info("Updated shop order: {}", entity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new OrderException("Cannot update order", e);
        }

    }

    @Override
    public void deleteByEntity(ShopOrder entity) {
        log.info("Delete shop order: {}", entity);
        try {
            shopOrderRepository.delete(entity);
            log.info("Deleted shop order: {}", entity);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new OrderException("Cannot delete order", e);
        }

    }

    @Override
    public List<ShopOrder> getAll() {
        log.info("Get all shop orders");
        try {
            List<ShopOrder> orders = shopOrderRepository.findAll();
            log.info("Get all shop orders");
            return orders;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new OrderException("Cannot get orders", e);
        }
    }

    @Override
    public List<ShopOrder> getAllOrdersByUserId(long userId) {
        log.info("Getting orders by user id: {}", userId);
        try {
            List<ShopOrder> orders = shopOrderRepository.findAllByCustomerId(userId);
            log.info("Retrieved {} orders for user id: {}", orders.size(), userId);
            return orders;
        } catch (Exception e) {
            log.error("Error while getting orders by user id: {}", userId, e);
            throw new OrderException("Error while getting orders by user id: " + userId, e);
        }
    }

    @Override
    public List<ShopOrder> getOrdersByProductId(Long productId) {
        log.info("Getting orders by product id: {}", productId);
        try {
            List<ShopOrder> orders = shopOrderRepository.findOrdersByProductId(productId);
            log.info("Retrieved {} orders for product id: {}", orders.size(), productId);
            return orders;
        } catch (Exception e) {
            log.error("Error while getting orders by product id: {}", productId, e);
            throw new OrderException("Error while getting orders by product id: " + productId, e);
        }
    }

    @Override
    @Transactional
    public void deleteOrdersBeforeDate(Date date) {
        log.info("Deleting orders before date: {}", date);
        try {
            shopOrderRepository.deleteOrdersBeforeDate(date);
            log.info("Deleted orders before date: {}", date);
        } catch (Exception e) {
            log.error("Error while deleting orders before date: {}", date, e);
            throw new OrderException("Error while deleting orders before date: " + date, e);
        }
    }

    @Override
    @Transactional
    public void closeOrder(long orderId) {
        Long closeStatusId = 4L;
        log.info("Closing order: {}", orderId);
        try {
            shopOrderRepository.closeOrder(orderId, closeStatusId);
            log.info("Closed order: {}", orderId);
        } catch (Exception e) {
            log.error("Error while closing order: {}", orderId, e);
            throw new OrderException("Error while closing order: " + orderId, e);
        }
    }

    @Override
    public void updateOrderStatus(long orderId, long statusId) {
        log.debug("Updating order status with orderId: {} to statusId: {}", orderId, statusId);
        try {
            shopOrderRepository.updateOrderStatus(orderId, statusId);
            log.info("Order status updated successfully");
        } catch (Exception e) {
            log.error("Error updating order status with orderId: {} to statusId: {}", orderId, statusId, e);
            throw new OrderStatusException("Failed to update order status with orderId: " + orderId, e);
        }
    }

    @Override
    public void deleteById(long id) {
        log.debug("Deleting order by id: {}", id);
        try {
            shopOrderRepository.deleteById(id);
            log.info("Deleted order by id: {}", id);
        }catch (Exception e) {
            log.error("Error while deleting order by id: {}", id, e);
            throw new OrderException("Failed to delete order by id: " + id, e);
        }

    }

    @Override
    public Optional<ShopOrder> getById(long id) {
        log.debug("Retrieving order by id: {}", id);
        try {
           Optional<ShopOrder> order = shopOrderRepository.findById(id);
            log.info("Retrieved order by id: {}", id);
            return order;
        }catch (Exception e) {
            log.error("Error while retrieving order by id: {}", id, e);
            throw new OrderException("Failed to retrieve order by id: " + id, e);
        }
    }
}
