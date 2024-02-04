package com.deliveroo.rider.repository;

import com.deliveroo.rider.entity.FeeBoost;
import com.deliveroo.rider.pojo.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FeeBoostRepository extends CrudRepository<FeeBoost, Long> {

    @Override
    <S extends FeeBoost> Iterable<S> saveAll(Iterable<S> entities);

    @Override
    <S extends FeeBoost> S save(S entity);

    @Override
    void deleteById(Long aLong);

    @Override
    Optional<FeeBoost> findById(Long aLong);

    @Override
    Iterable<FeeBoost> findAll();

    @Override
    Iterable<FeeBoost> findAllById(Iterable<Long> longs);

    @Override
    long count();

    Iterable<FeeBoost> findAllByDayOfWeek(DayOfWeek dayOfWeek);

    @Query(value = "select * from fee_boost where day_of_week in ?1",nativeQuery = true)
    Iterable<FeeBoost> findByDayOfWeekIn(List<Integer> dayOfWeeks);
}
