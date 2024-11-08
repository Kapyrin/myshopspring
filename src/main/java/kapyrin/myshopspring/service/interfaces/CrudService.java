package kapyrin.myshopspring.service.interfaces;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {
    void add(T entity);

    void update(T entity);

    void deleteByEntity(T entity);

    List<T> getAll();

}
