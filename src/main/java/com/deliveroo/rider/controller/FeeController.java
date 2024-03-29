package com.deliveroo.rider.controller;

import com.deliveroo.rider.entity.FeeBoost;
import com.deliveroo.rider.pojo.DayOfWeek;
import com.deliveroo.rider.pojo.dto.FeeInfo;
import com.deliveroo.rider.repository.FeeBoostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class FeeController {
    @Autowired
    private FeeBoostRepository repository;

    @GetMapping("/feeBoosts/{day}")
    public ResponseEntity<List<FeeInfo>> getFeeBoostsByDay(@PathVariable("day") DayOfWeek dayOfWeek){
        Iterable<FeeBoost> feeBoostIterable = null;
        switch (dayOfWeek){
            case MONDAY:
            case TUESDAY:
            case WEDNESDAY:
            case THURSDAY:
                feeBoostIterable = repository.findAllByDayOfWeek(dayOfWeek);
                break;
            case FRIDAY:
                feeBoostIterable = repository.findByDayOfWeekIn(
                        Arrays.asList(DayOfWeek.FRIDAY.ordinal(),
                                DayOfWeek.SATURDAY.ordinal(),
                                DayOfWeek.SUNDAY.ordinal()));
                break;
            case SATURDAY:
                feeBoostIterable = repository.findByDayOfWeekIn(
                        Arrays.asList(DayOfWeek.SATURDAY.ordinal(),
                                DayOfWeek.SUNDAY.ordinal()));
                break;
            case SUNDAY:
                feeBoostIterable = repository.findAllByDayOfWeek(DayOfWeek.SUNDAY);
                break;
        }
        if(feeBoostIterable!=null){
            Iterator<FeeBoost> iterator = feeBoostIterable.iterator();
            List<FeeBoost> list = new ArrayList<>();
            while(iterator.hasNext()){
                FeeBoost next = iterator.next();
                list.add(next);
            }
            return ResponseEntity.ok().body(formatFeeBoostList(list));
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    private List<FeeInfo> formatFeeBoostList(List<FeeBoost> feeBoostList) {
        Map<String, List<FeeBoost>> map = feeBoostList.stream()
                .collect(Collectors.groupingBy(ele-> ele.getDayOfWeek().toString() ));
        List<FeeInfo> feeInfoList = new ArrayList<>();
        for (Map.Entry<String, List<FeeBoost>> entry : map.entrySet()) {
            FeeInfo feeInfo = new FeeInfo();
            feeInfo.setDate(entry.getKey());
            feeInfo.setFeeList(entry.getValue());
            feeInfoList.add(feeInfo);
        }
        feeInfoList.sort(Comparator.comparing(a -> DayOfWeek.valueOf(a.getDate())));
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM", Locale.ENGLISH);
        for(int i=0;i<feeInfoList.size();i++){
            FeeInfo feeInfo = feeInfoList.get(i);
            LocalDate nextDay = currentDate.plusDays(i);
            feeInfo.setDate(String.format("%s %s",feeInfo.getDate(),nextDay.format(formatter).toUpperCase()));
        }
        return feeInfoList;
    }

    @GetMapping("/feeBoosts")
     public ResponseEntity<List<FeeBoost>> getFeeBoosts() {
        Iterable<FeeBoost> feeBoostIterable = repository.findAll();
        if(feeBoostIterable!=null){
            Iterator<FeeBoost> iterator = feeBoostIterable.iterator();
            List<FeeBoost> list = new ArrayList<>();
            while(iterator.hasNext()){
                FeeBoost next = iterator.next();
                list.add(next);
            }
            return ResponseEntity.ok().body(list);
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/feeBoost/{id}")
    public ResponseEntity<FeeBoost> getFeeBoost(@PathVariable("id") Long id) {
        Optional<FeeBoost> feeBoost = repository.findById(id);
        return feeBoost.map(boost -> ResponseEntity.ok().body(boost)).orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/feeBoost/{id}")
    public ResponseEntity<String> deleteFeeBoost(@PathVariable("id") Long id){
        Optional<FeeBoost> feeBoost = repository.findById(id);
        if(feeBoost.isPresent()){
            repository.deleteById(id);
            return ResponseEntity.ok().body("FeeBoost deleted");
        }else {
            return ResponseEntity.badRequest().body("FeeBoost doesn't exist!");
        }
    }

    @DeleteMapping("/feeBoosts")
    public ResponseEntity<String> deleteFeeBoosts(){
        long count = repository.count();
        if(count>0){
            repository.deleteAll();
            return ResponseEntity.ok().body("Deleted All!");
        }else {
            return ResponseEntity.badRequest().body("No Data exists!");
        }

    }

    @PutMapping("/feeBoost")
    public ResponseEntity<FeeBoost> addFeeBoost(@RequestBody FeeBoost feeBoost) {
        FeeBoost saved = repository.save(feeBoost);
        if(saved!=null){
            return ResponseEntity.ok().body(saved);
        }else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/feeBoosts")
    public ResponseEntity<List<FeeBoost>> addFeeBoost(@RequestBody List<FeeBoost> feeBoosts) {
        Iterable<FeeBoost> iterable = repository.saveAll(feeBoosts);
        if(iterable!=null){
            Iterator<FeeBoost> iterator = iterable.iterator();
            List<FeeBoost> list = new ArrayList<>();
            while(iterator.hasNext()){
                list.add(iterator.next());
            }
            return ResponseEntity.ok().body(list);
        }else {
            return ResponseEntity.badRequest().build();
        }
    }
}
