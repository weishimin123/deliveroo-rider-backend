package com.deliveroo.rider;

import com.deliveroo.rider.pojo.DayOfWeek;
import com.deliveroo.rider.pojo.Month;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Calendar;
import java.util.Date;

//@SpringBootTest
public class RiderApplicationTests {

	@Test
    public void contextLoads() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.setFirstDayOfWeek(Calendar.MONDAY);



		calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		System.out.println(calendar.getTime());

		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		System.out.println(calendar.getTime());

		calendar.add(Calendar.DAY_OF_WEEK,-1);
		System.out.println(calendar.getTime());

		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		System.out.println(calendar.getTime());

		calendar.add(Calendar.DAY_OF_WEEK,-1);
		System.out.println(calendar.getTime());

		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		System.out.println(calendar.getTime());
	}

}
