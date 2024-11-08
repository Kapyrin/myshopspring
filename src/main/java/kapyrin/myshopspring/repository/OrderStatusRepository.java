package kapyrin.myshopspring.repository;

import kapyrin.myshopspring.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
}

