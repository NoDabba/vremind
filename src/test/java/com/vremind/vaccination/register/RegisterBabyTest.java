/**
 * 
 */
package com.vremind.vaccination.register;

import java.util.concurrent.ExecutorService;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vremind.vaccination.services.register.VaccinationReminderBabyService;


/**
 * @author sdoddi
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContextJDBC.xml",
        "classpath:applicationContextDAOs.xml", 
        "classpath:applicationContextServices.xml"})

public class RegisterBabyTest {

	@Autowired
	private VaccinationReminderBabyService vaccinationReminderBabyServiceImpl;
	
	@Autowired
	private static ExecutorService executorService; 
	
	@BeforeClass
    public static void setUpBeforeClass() throws Exception {   
		System.setProperty("vremind.db.url", "jdbc:mysql://localhost:3306/vremind");
		System.setProperty("vremind.db.username", "root");
		System.setProperty("vremind.db.password", "MyNewPass");
	}
	
	
	
	@AfterClass
	public static void closeThreadpool()
	{
		try {
			Thread.sleep(10000);
		}
		catch(Exception ex){}
		//executorService.shutdown();
	}
	
}
