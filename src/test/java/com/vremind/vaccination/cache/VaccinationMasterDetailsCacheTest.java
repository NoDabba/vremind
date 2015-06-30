/**
 * 
 */
package com.vremind.vaccination.cache;

import java.util.List;
import java.util.concurrent.ExecutorService;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.vremind.vaccination.domain.MasterVaccinationTO;
import com.vremind.vaccination.services.VaccinationMasterDetailsService;

/**
 * @author sdoddi
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContextJDBC.xml",
        "classpath:applicationContextDAOs.xml", 
        "classpath:applicationContextServices.xml"})
public class VaccinationMasterDetailsCacheTest {

	@Autowired
	private VaccinationMasterDetailsService vaccinationMasterDetailsService;
	
	@Autowired
	private static ExecutorService executorService; 
	
	@BeforeClass
    public static void setUpBeforeClass() throws Exception {   
		System.setProperty("vremind.db.url", "jdbc:mysql://localhost:3306/vremind");
		System.setProperty("vremind.db.username", "root");
		System.setProperty("vremind.db.password", "MyNewPass");
	}
	
	
	@Test
	public void testGetVaccinationMasterDetailsFromDB()
	{
		List<MasterVaccinationTO> list = vaccinationMasterDetailsService.getVaccinationDetails();
		Assert.assertNotNull(list);
		Assert.assertSame(list.size() > 0, true);
	}
	
	@Test
	public void testGetVaccinationMasterDetailsFromCache()
	{
		List<MasterVaccinationTO> list = vaccinationMasterDetailsService.getVaccinationDetails();
		Assert.assertNotNull(list);
		Assert.assertSame(list.size() > 0, true);
	}
	
	@Test
	public void testRemoveVaccinationMasterDetailsFromCache()
	{
		
		List<MasterVaccinationTO> list = vaccinationMasterDetailsService.getVaccinationDetails();
		Assert.assertNotNull(list);
		Assert.assertSame(list.size() > 0, true);
		vaccinationMasterDetailsService.saveVaccinationDetails();
		list = vaccinationMasterDetailsService.getVaccinationDetails();
		Assert.assertNotNull(list);
		Assert.assertSame(list.size() > 0, true);
	}
	
	@AfterClass
	public static void closeThreadpool()
	{
		executorService.shutdown();
	}
}
