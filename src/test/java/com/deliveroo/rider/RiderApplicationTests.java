package com.deliveroo.rider;

import com.deliveroo.rider.pojo.DayOfWeek;
import com.deliveroo.rider.pojo.Month;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

//@SpringBootTest
public class RiderApplicationTests {

	static SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	static String TOKEN = null;
//	@Test
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

	@Test
	public void createTokenTest(){
		Date issedDate = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
		Date expirationDate = Date.from(LocalDateTime.of(2024,2,7,0,0,0).atZone(ZoneId.systemDefault()).toInstant());

		Long id = 839527L;
		 TOKEN = Jwts.builder()
				.setId(UUID.randomUUID().toString())
				.setSubject("Subject")
				.setIssuer("Issuer")
				.setAudience("Audience")
				//
				.claim("riderId",id)
				.claim("email","fuengwang95@gmail.com")
				.setIssuedAt(issedDate)
				.setExpiration(expirationDate)
				.signWith(secretKey)
				.compact();
		System.out.println(TOKEN);
	}

	@Test
	public void parseTokenTest(){
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(secretKey)
				.build()
				.parseClaimsJws(TOKEN)
				.getBody();
		System.out.println(claims);
	}
}
