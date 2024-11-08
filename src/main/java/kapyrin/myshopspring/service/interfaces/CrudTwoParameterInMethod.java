package kapyrin.myshopspring.service.interfaces;

import kapyrin.myshopspring.entity.Product;
import kapyrin.myshopspring.entity.ProductOrder;

import java.util.List;
import java.util.Optional;

public interface CrudTwoParameterInMethod <T> extends CrudService<T> {
    void deleteByIds(long firstId, long secondId);

    Optional<T> getByIds(long firstId, long secondId);
}
