package com.deliveroo.rider.repository;

import com.deliveroo.rider.entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account,Long> {
    @Override
    <S extends Account> S save(S entity);

    @Override
    void deleteById(Long aLong);


}
