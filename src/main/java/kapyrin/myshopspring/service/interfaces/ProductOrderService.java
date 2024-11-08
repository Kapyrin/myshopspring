package kapyrin.myshopspring.service.interfaces;

import kapyrin.myshopspring.entity.Product;
import kapyrin.myshopspring.entity.ProductOrder;

import java.util.List;

public interface ProductOrderService extends CrudTwoParameterInMethod<ProductOrder> {
    List<Product> productFromProductOrder(long productOrderId);

}
