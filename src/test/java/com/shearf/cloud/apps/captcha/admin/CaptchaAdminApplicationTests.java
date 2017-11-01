package com.shearf.cloud.apps.captcha.admin;

import com.shearf.cloud.apps.captcha.admin.service.SimpleCaptchaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CaptchaAdminApplicationTests {

	@Resource
	private SimpleCaptchaService simpleCaptchaService;

	@Test
	public void contextLoads() {
	}
}
