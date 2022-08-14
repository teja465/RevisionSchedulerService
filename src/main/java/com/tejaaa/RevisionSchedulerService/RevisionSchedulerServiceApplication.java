package com.tejaaa.RevisionSchedulerService;

import com.tejaaa.RevisionSchedulerService.entitys.AppUser;
import com.tejaaa.RevisionSchedulerService.entitys.UserLearningItem;
import com.tejaaa.RevisionSchedulerService.entitys.UserProfile;
import com.tejaaa.RevisionSchedulerService.service.AppUserService;
import com.tejaaa.RevisionSchedulerService.service.UserLearningItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootApplication
@EntityScan(basePackages = {"com.tejaaa.RevisionSchedulerService"})
@EnableJpaRepositories( basePackages = "com.tejaaa.RevisionSchedulerService")
public class RevisionSchedulerServiceApplication  implements CommandLineRunner {

	@Autowired
	private AppUserService appUserService;

	@Autowired
	private UserLearningItemService userLearningItemService;

	public static void main(String[] args) {
		SpringApplication.run(RevisionSchedulerServiceApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//		AppUser user = new AppUser(null,"tejaaa@gmail.com","tejaaa",true,new ArrayList<>(),new UserProfile(),null);
//		user.getUserProfile().setRevisionPattern("5,10,15");
//		appUserService.saveUser(user);
//
//		LocalDate date = LocalDate.now();
//		UserLearningItem item = new UserLearningItem(null,"tejaaa@gmail.com","Spring Security architecture",
//				"Learned spring  security architecture from www.spring.io", date,date,1,null);
//		userLearningItemService.saveUserLearningItem(item);
	}
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
}


//select * from app_user;SELECT * FROM user_profile;
// select * from user_learning_item;select * from user_learning_revision_schedules;
