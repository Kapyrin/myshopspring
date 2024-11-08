package kapyrin.myshopspring.service.interfaces;

import kapyrin.myshopspring.entity.ShopOrder;

import java.sql.Date;
import java.util.List;

public interface ShopOrderService extends CrudOneParameterInMethod<ShopOrder>  {
    List<ShopOrder> getAllOrdersByUserId(long userId);

    List<ShopOrder> getOrdersByProductId(Long productId);

    void deleteOrdersBeforeDate(Date date);

    void closeOrder(long orderId);

    void updateOrderStatus(long orderId, long statusId);
}
