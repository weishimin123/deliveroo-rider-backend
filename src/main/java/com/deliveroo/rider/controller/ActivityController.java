package com.deliveroo.rider.controller;

import com.deliveroo.rider.component.CustomProperties;
import com.deliveroo.rider.entity.Activity;
import com.deliveroo.rider.entity.FeeBoost;
import com.deliveroo.rider.entity.Order;
import com.deliveroo.rider.entity.OrderDetail;
import com.deliveroo.rider.pojo.DayOfWeek;
import com.deliveroo.rider.pojo.Month;
import com.deliveroo.rider.pojo.WorkingType;
import com.deliveroo.rider.pojo.dto.*;
import com.deliveroo.rider.repository.ActivityRepository;
import com.deliveroo.rider.repository.FeeBoostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalTime;
import java.util.*;

import static com.deliveroo.rider.util.Utils.mapToDayOfWeek;

@RestController
public class ActivityController {

    @Autowired
    private ActivityRepository repository;

    @Autowired
    private FeeBoostRepository feeBoostRepository;

    @Autowired
    private CustomProperties customProperties;

    private static volatile List<FeeBoost> feeBoostList;

    @GetMapping("/activities/{startYear}/{startMonth}/months/{months}")
    public ResponseEntity<List<MonthlyActivitySummary>> monthlyActivities(@PathVariable("startYear")int year, @PathVariable("startMonth") Month startMonth, @PathVariable("months") int length ){
        List<MonthlyActivitySummary> list = new ArrayList<>();
        for(int i=0; i<length; i++) {
            Month[] months = Month.values();
            int index = startMonth.ordinal()-i;
            if(index < 0) {
                if(index == -1){
                    year--;
                }
                index = 12 + index;
            }
            Month month = months[index];
            List<Activity> monthlyActivities = repository.findByDateInYearAndMonth(year, month.ordinal() + 1);
            int orders = calculateTotalOrders(monthlyActivities);
            double earnings = calculateTotalEarnings(monthlyActivities);
            list.add(new MonthlyActivitySummary(month.getAbbreviation(),orders,earnings));
        }
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/activities/{year}/{month}")
    public ResponseEntity<MonthlyActivity> monthlyActivity(@PathVariable("year") int year
            , @PathVariable("month") Month month){
        List<Activity> monthlyActivities = repository.findByDateInYearAndMonth(year, month.ordinal() + 1);
        double monthlyEarnings = calculateTotalEarnings(monthlyActivities);
        int monthlyOrders = calculateTotalOrders(monthlyActivities);
        int activityDays = calculateActivityDays(monthlyActivities);
        List<DailyActivitySummary> dailyActivities = new ArrayList<>();
        for(Activity activity : monthlyActivities){
            int dailyOrders = calculateTotalOrders(activity);
            double dailyEarnings = calculateTotalEarnings(activity);
            Long id = activity.getId();
            DailyActivitySummary dailyActivity = new DailyActivitySummary(dailyOrders,
                    dailyEarnings,
                    activity.getDate(),id);
            dailyActivities.add(dailyActivity);
        }
        MonthlyActivity monthlyActivity = new MonthlyActivity(month.getAbbreviation(), monthlyOrders,monthlyEarnings,activityDays,dailyActivities);
        return ResponseEntity.ok().body(monthlyActivity);
    }

    @GetMapping("/activity/{id}")
    public ResponseEntity<DailyActivity> dailyActivity(@PathVariable("id") Long id){
        Optional<Activity> optional = repository.findById(id);
        if(optional.isPresent()) {
            Activity activity = optional.get();
            int dailyOrders = calculateTotalOrders(activity);
            double dailyEarnings = calculateTotalEarnings(activity);
            double fees = calculateTotalFees(activity);
            double tips = calculateTotalTips(activity);
            double extras = calculateTotalExtraFees(activity);
            List<OrderSummary> orderSummaries = new ArrayList<>();
            for(Order order: activity.getOrders()) {
                double earnings = calculateTotalEarnings(order);
                LocalTime completeTime = calculateCompleteTime(order);
                int subOrders = calculateSubOrders(order);
                OrderSummary orderSummary = new OrderSummary(order.getShop(),completeTime,earnings,subOrders,order.getId());
                orderSummaries.add(orderSummary);
            }
            DailyActivity dailyActivity = new DailyActivity(activity.getDate(),fees,extras,tips,orderSummaries);
            dailyActivity.setOrders(dailyOrders);
            dailyActivity.setId(activity.getId());
            dailyActivity.setDailyEarnings(dailyEarnings);
            return ResponseEntity.ok().body(dailyActivity);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/activity")
    @Transactional
    public ResponseEntity<Activity> addActivity(@RequestBody Activity activity) {
        Activity saved = repository.save(activity);
        if (saved != null) {
            return ResponseEntity.ok().body(saved);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/activities")
    public ResponseEntity<String> addActivities(@RequestBody Collection<Activity> activities) {
        Iterable<Activity> iterable = repository.saveAll(activities);
        if (iterable != null) {
            Iterator<Activity> iterator = iterable.iterator();
            int sum = 0;
            while (iterator.hasNext()) {
                iterator.next();
                sum++;
            }
            return ResponseEntity.ok().body(sum + " activities added.");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/activities")
    public void deleteAllActivities() {
        repository.deleteAll();
    }

    @GetMapping("/activities/{workingType}/months/{months}")
    @Transactional
    public void generateMockedActivities(@PathVariable("workingType") WorkingType workingType,
                                         @PathVariable("months") Integer months) {
        /**
         * the default working type is normal
         * Busy working type work 7 days per week,                              15 orders in average per day,
         * normal working type work from monday to Saturday, be off on Sundays, 10 orders in average per day,
         * easy working type, work from monday to Friday, be off at weekends, 5 orders in average per day,
         */
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -months);
        Calendar now = Calendar.getInstance();
        ArrayList<Activity> bufferActivities = new ArrayList<>();
        while (true) {
            switch (calendar.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    //sunday
                    if (workingType == WorkingType.BUSY) {
                        bufferActivities.add(randomActivity(calendar, workingType));
                    }
                    break;
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    bufferActivities.add(randomActivity(calendar, workingType));
                    break;
                case 7:
                    //Saturday
                    if (workingType == WorkingType.BUSY || workingType == WorkingType.NORMAL) {
                        bufferActivities.add(randomActivity(calendar, workingType));
                    }
                    break;
            }
            if (bufferActivities.size() == 10) {
                repository.saveAll(bufferActivities);
                bufferActivities.clear();
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if (calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                    calendar.get(Calendar.MONTH) == now.get(Calendar.MONTH) &&
                    calendar.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)) {
                repository.saveAll(bufferActivities);
                bufferActivities.clear();
                break;
            }
        }
    }

    private Activity randomActivity(Calendar calendar, WorkingType workingType) {
        Activity activity = new Activity();
        activity.setDate(calendar.getTime());
        DayOfWeek dayOfWeek = mapToDayOfWeek(calendar);
            switch (workingType) {
                case BUSY:
                    activity.setOrders(randomOrders(15, dayOfWeek));
                case NORMAL:
                    activity.setOrders(randomOrders(10, dayOfWeek));
                case EASY:
                    activity.setOrders(randomOrders(5, dayOfWeek));

            }

        return activity;
    }

    private List<Order> randomOrders(int maxOrders, DayOfWeek dayOfWeek) {
        List<Order> orders = new ArrayList<>();
        for (int i = 1; i <= maxOrders; i++) {
            orders.add(randomOrder(dayOfWeek));
        }
        return orders;
    }

    private Order randomOrder(DayOfWeek dayOfWeek) {
        Order order = new Order();
        order.setFee(randomFee(dayOfWeek));
        order.setOrderDetails(randomOrderDetails());
        order.setTip(randomTip(1,5));
        order.setShop(randomShop());
        order.setPlace(randomPlace());
        order.setExtra(randomExtra(dayOfWeek,order));
        return order;
    }

    private Double randomExtra(DayOfWeek dayOfWeek, Order order) {
        if(feeBoostList == null){
            searchFeeBoostList();
        }
        Optional<FeeBoost> optional = feeBoostList.stream()
                .filter(item -> item.getDayOfWeek() == dayOfWeek)
                .findFirst();
        if(optional.isPresent()){
            FeeBoost feeBoost = optional.get();
            if(ifInExtraPeriod(feeBoost, order.getOrderDetails())){
                BigDecimal result = BigDecimal.valueOf(order.getFee())
                        .multiply(BigDecimal.valueOf(feeBoost.getRate() - 1));
                return  formatDouble(result.doubleValue());
            }
        }
        return 0.0;
    }

    private boolean ifInExtraPeriod(FeeBoost feeBoost,List<OrderDetail> orderDetails){
        return orderDetails.stream().anyMatch(ele->
                ele.getStart().isAfter(feeBoost.getStart()) &&
                        ele.getComplete().isBefore(feeBoost.getComplete()));
    }

    private Double randomFee(DayOfWeek dayOfWeek) {
        double random;
        if (dayOfWeek == DayOfWeek.FRIDAY || dayOfWeek == DayOfWeek.SATURDAY) {
            random =  2.9 + (Math.random() * (14 - 2.9));
        } else {
            random =  2.9 + (Math.random() * (9 - 2.9));
        }
        return formatDouble(random);
    }

    private Double formatDouble(Double value){
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(value));
    }

    private Double randomTip(int min, int max){
        if( Math.random() > 0.87){
            Random random = new Random();
            return formatDouble(min + (max - min) * random.nextDouble());
        }
        return 0.0;
    }

    private String randomShop() {
        List<String> shops = customProperties.getShops();
        int index = new Random().nextInt(shops.size() );
        return shops.get(index);
    }

    private String randomPlace() {
        List<String> places = customProperties.getPlaces();
        int index = new Random().nextInt(places.size() );
        return places.get(index);
    }

    private String randomOrderNo() {
        int random = (int) (Math.random() * 10000);
        if (random < 1000) {
            return "0" + random;
        } else {
            return String.valueOf(random);
        }
    }

    private List<OrderDetail> randomOrderDetails() {
        List<OrderDetail> orderDetails = new ArrayList<>();
        OrderDetail orderDetail = randomOrderDetail();
        orderDetails.add(orderDetail);
        Random random = new Random();
        if (random.nextInt(10) > 8.5) {
            orderDetails.add(randomOrderDetail(orderDetail.getStart(), orderDetail.getComplete()));
        }
        return orderDetails;
    }

    private OrderDetail randomOrderDetail() {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderNo(randomOrderNo());
        int startHour = randomStartHour();
        int startMinute = randomStartMinute();
        int minutes = randomTimeElapsed(5, 45);
        orderDetail.setStart(calculateStartTime(startHour, startMinute));
        orderDetail.setComplete(calculateCompleteTime(startHour, startMinute + minutes));
        return orderDetail;
    }

    private OrderDetail randomOrderDetail(LocalTime start, LocalTime complete) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderNo(randomOrderNo());
        int minutes = randomTimeElapsed(5, 15);
        orderDetail.setStart(start);
        orderDetail.setComplete(calculateCompleteTime(complete.getHour(), complete.getMinute() + minutes));
        return orderDetail;
    }

    private LocalTime calculateCompleteTime(int hour, int minute) {
        if (minute > 59) {
            hour++;
            minute -= 60;
        }
        if(hour>23){
            hour-=24;
        }
        return LocalTime.of(hour, minute);
    }

    private LocalTime calculateStartTime(int hour, int minute) {
        return LocalTime.of(hour, minute);
    }

    private int randomStartMinute() {
        Random random = new Random();
        return random.nextInt(59);
    }

    private int randomStartHour() {
        Random random = new Random();
        return random.nextInt(24);
    }

    private int randomTimeElapsed(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }

    private void searchFeeBoostList(){
        if(feeBoostList == null){
            synchronized (ActivityController.class){
                if(feeBoostList == null){
                    Iterable<FeeBoost> feeBoosts = feeBoostRepository.findAll();
                    feeBoostList = new ArrayList<>();
                    for (FeeBoost next : feeBoosts) {
                        feeBoostList.add(next);
                    }
                }
            }
        }
    }
    private double calculateTotalEarnings(List<Activity> activities) {
        double earnings = activities.stream()
                .flatMap(monthly -> monthly.getOrders().stream())
                .mapToDouble(order -> BigDecimal.valueOf(order.getFee())
                        .add(BigDecimal.valueOf(order.getExtra()))
                        .add(BigDecimal.valueOf(order.getTip()))
                        .doubleValue())
                .sum();
        return formatDouble(earnings);
    }

    private double calculateTotalEarnings(Activity activity) {
        double earnings = activity.getOrders()
                .stream()
                .mapToDouble(order -> BigDecimal.valueOf(order.getFee())
                        .add(BigDecimal.valueOf(order.getExtra()))
                        .add(BigDecimal.valueOf(order.getTip()))
                        .doubleValue())
                .sum();
        return formatDouble(earnings);
    }

    private double calculateTotalEarnings(Order order) {
        double earnings = BigDecimal.valueOf(order.getFee())
                .add(BigDecimal.valueOf(order.getExtra()))
                .add(BigDecimal.valueOf(order.getTip()))
                .doubleValue();
        return formatDouble(earnings);
    }

    private LocalTime calculateCompleteTime(Order order){
        Optional<OrderDetail> max = order.getOrderDetails().stream()
                .max(Comparator.comparing(OrderDetail::getComplete));
        return max.map(OrderDetail::getComplete).orElse(null);
    }

    private int calculateSubOrders(Order order){
        return order.getOrderDetails().size();
    }

    private double calculateTotalFees(Activity activity) {
        double earnings = activity.getOrders()
                .stream()
                .mapToDouble(order -> BigDecimal.valueOf(order.getFee()).doubleValue())
                .sum();
        return formatDouble(earnings);
    }

    private double calculateTotalExtraFees(Activity activity) {
        double earnings = activity.getOrders()
                .stream()
                .mapToDouble(order -> BigDecimal.valueOf(order.getExtra()).doubleValue())
                .sum();
        return formatDouble(earnings);
    }

    private double calculateTotalTips(Activity activity) {
        double earnings = activity.getOrders()
                .stream()
                .mapToDouble(order -> BigDecimal.valueOf(order.getTip()).doubleValue())
                .sum();
        return formatDouble(earnings);
    }

    private int calculateTotalOrders(List<Activity> monthlyActivities) {
        return monthlyActivities.stream()
                .flatMap(monthly -> monthly.getOrders().stream())
                .mapToInt(order -> 1)
                .sum();
    }

    private int calculateTotalOrders(Activity activity){
        return activity.getOrders().size();
    }

    private int calculateActivityDays(List<Activity> monthlyActivities){
        return monthlyActivities.size();
    }

}
