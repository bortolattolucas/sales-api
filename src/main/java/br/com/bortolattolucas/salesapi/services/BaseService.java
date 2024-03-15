package br.com.bortolattolucas.salesapi.services;

import org.hibernate.ObjectNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseService<T, ID> {

    JpaRepository<T, ID> getRepository();

    default T save(T object) {
        return getRepository().save(object);
    }

    default T findById(ID id) {
        return getRepository().findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "Not found"));
    }
}
