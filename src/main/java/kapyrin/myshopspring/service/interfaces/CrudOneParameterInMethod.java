package kapyrin.myshopspring.service.interfaces;

import java.util.Optional;

public interface CrudOneParameterInMethod<T> extends CrudService<T> {
    void deleteById(long id);

    Optional<T> getById(long id);
}
