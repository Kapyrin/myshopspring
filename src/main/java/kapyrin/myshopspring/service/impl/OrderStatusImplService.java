package kapyrin.myshopspring.service.impl;

import kapyrin.myshopspring.entity.OrderStatus;
import kapyrin.myshopspring.exception.entity.OrderStatusException;
import kapyrin.myshopspring.repository.OrderStatusRepository;
import kapyrin.myshopspring.service.interfaces.CrudOneParameterInMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderStatusImplService implements CrudOneParameterInMethod<OrderStatus> {
    private final OrderStatusRepository orderStatusRepository;

    public OrderStatusImplService(OrderStatusRepository orderStatusRepository) {
        this.orderStatusRepository = orderStatusRepository;
    }

    @Override
    public void add(OrderStatus orderStatus) {
        log.info("Adding order status: {}", orderStatus);
        try {
            orderStatusRepository.save(orderStatus);
            log.info("Successfully added order status: {}", orderStatus);
        } catch (Exception e) {
            log.error("Error adding order status: {}", orderStatus);
            throw new OrderStatusException("Error adding order status: " + orderStatus);
        }

    }

    @Override
    public void update(OrderStatus orderStatus) {
        log.info("Updating order status: {}", orderStatus);
        try {
            orderStatusRepository.save(orderStatus);
            log.info("Successfully updated order status: {}", orderStatus);
        } catch (Exception e) {
            log.error("Error updating order status: {}", orderStatus);
            throw new OrderStatusException("Error updating order status: " + orderStatus);
        }

    }

    @Override
    public void deleteById(long id) {
        log.info("Deleting order status: {}", id);
        try {
            orderStatusRepository.deleteById(id);
            log.info("Successfully deleted order status: {}", id);
        } catch (Exception e) {
            log.error("Error deleting order status: {}", id);
            throw new OrderStatusException("Error deleting order status: " + id);
        }

    }

    @Override
    public void deleteByEntity(OrderStatus orderStatus) {
        log.info("Deleting order status: {}", orderStatus);
        try {
            orderStatusRepository.delete(orderStatus);
            log.info("Successfully deleted order status: {}", orderStatus);
        } catch (Exception e) {
            log.error("Error deleting order status: {}", orderStatus);
            throw new OrderStatusException("Error deleting order status: " + orderStatus);
        }
    }

    @Override
    public List<OrderStatus> getAll() {
        log.info("Getting all order status");
        try {
            List<OrderStatus> orderStatuses = orderStatusRepository.findAll();
            log.info("Successfully retrieved all order status");
            return orderStatuses;
        }catch (Exception e) {
            log.error("Error retrieving all order status");
            throw new OrderStatusException("Error retrieving all order status: " + e);
        }
    }

    @Override
    public Optional<OrderStatus> getById(long id) {
       log.info("Getting order status: {}", id);
       try  {
           Optional<OrderStatus> orderStatus = orderStatusRepository.findById(id);
           log.info("Successfully retrieved order status: {}", orderStatus);
           return orderStatus;
       }catch (Exception e) {
           log.error("Error retrieving order status: {}", id);
           throw new OrderStatusException("Error retrieving order status: " + id);
       }
    }



}
