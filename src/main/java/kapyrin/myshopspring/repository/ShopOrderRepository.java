package kapyrin.myshopspring.repository;

import kapyrin.myshopspring.entity.ShopOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface ShopOrderRepository extends JpaRepository<ShopOrder, Long> {
    List<ShopOrder> findAllByCustomerId(Long customerId);

    @Query("SELECT po.order FROM ProductOrder po WHERE po.product.id = :productId")
    List<ShopOrder> findOrdersByProductId(@Param("productId") Long productId);

    @Modifying
    @Transactional
    @Query("DELETE FROM ShopOrder o WHERE o.orderCreationDate < :date")
    void deleteOrdersBeforeDate(@Param("date") Date date);

    @Modifying
    @Transactional
    @Query("UPDATE ShopOrder o SET o.status.id = :statusId, o.orderCloseDate = CURRENT_DATE WHERE o.id = :orderId")
    void closeOrder(@Param("orderId") Long orderId, @Param("statusId") Long statusId);

    @Modifying
    @Transactional
    @Query("UPDATE ShopOrder o SET o.status.id = :statusId WHERE o.id = :orderId")
    void updateOrderStatus(@Param("orderId") Long orderId, @Param("statusId") Long statusId);
}

