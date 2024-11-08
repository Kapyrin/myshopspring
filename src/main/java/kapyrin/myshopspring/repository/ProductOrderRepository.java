package kapyrin.myshopspring.repository;

import kapyrin.myshopspring.entity.Product;
import kapyrin.myshopspring.entity.ProductOrder;
import kapyrin.myshopspring.entity.ProductOrderKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductOrderRepository extends JpaRepository<ProductOrder, ProductOrderKey> {
    @Query("SELECT po.product FROM ProductOrder po WHERE po.id.orderId = :orderId")
    List<Product> findProductsByOrderId(@Param("orderId") Long orderId);

  }
