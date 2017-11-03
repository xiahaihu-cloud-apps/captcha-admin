package com.shearf.cloud.apps.captcha.admin;

import com.shearf.cloud.apps.captcha.admin.service.SimpleCaptchaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author xiahaihu2009@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class SimpleCaptchaServiceTests {

    @Resource
    private SimpleCaptchaService simpleCaptchaService;

    @Commit
    @Test
    public void create() {
        simpleCaptchaService.createByDate("20170612", 5);
    }

}
