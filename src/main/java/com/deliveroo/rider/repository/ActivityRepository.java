package com.deliveroo.rider.repository;

import com.deliveroo.rider.entity.Activity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends CrudRepository<Activity, Long> {
    @Override
    <S extends Activity> S save(S entity);

    @Override
    <S extends Activity> Iterable<S> saveAll(Iterable<S> entities);

    @Override
    Iterable<Activity> findAllById(Iterable<Long> longs);

    @Override
    Optional<Activity> findById(Long id);

    @Override
    void deleteById(Long aLong);

    @Override
    void deleteAll();

    @Query(value = "SELECT * FROM activity WHERE YEAR(date) = :year and MONTH(date) = :month",nativeQuery = true)
    List<Activity> findByDateInYearAndMonth(@Param("year") int year, @Param("month") int month);
}
