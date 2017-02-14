package com.prototype;

import com.prototype.model.Address;
import com.prototype.model.AddressData;
import com.prototype.repository.user.UserDao;
import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NeighBroApplicationTests {

	@Autowired
	private UserDao userDao;

	@Test
	public void contextLoads() {
	}

//	@Test
//	public void userDao() {
//		List<AddressData> ret = userDao.getOneTenantsAddressDataPerEachApartment(new ObjectId("584f2146d42e147134e1ea7d"));
//		Assert.assertEquals(1, ret.size());
//	}

}
