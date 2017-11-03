package com.shearf.cloud.apps.captcha.admin.job;

import com.shearf.cloud.apps.captcha.admin.domain.bean.ConfigValue;
import com.shearf.cloud.apps.captcha.admin.domain.model.SimpleCaptcha;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @author xiahaihu2009@gmail.com
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleCaptchaJobTests {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedisTemplate<String, SimpleCaptcha> redisTemplate;

    @Resource
    private ConfigValue configValue;

    @Before
    public void setUp() throws Exception {
        configValue.setCreateCaptchaPoolNum(2);
        configValue.setThreadCacheCaptchaNum(100);
        configValue.setDailyTotalCaptcha(10000);
    }

    @Resource
    private SimpleCaptchaJob simpleCaptchaJob;

    @Test
    public void createDailyCaptcha() throws Exception {
        simpleCaptchaJob.createDailyCaptcha();
    }

    @Test
    public void cacheDailyCaptcha() {
        simpleCaptchaJob.cacheDailyCaptcha();
    }

    @Test
    public void testRedis() {
        ValueOperations<String, String> valueOperations =  stringRedisTemplate.opsForValue();
        valueOperations.set("test", "test");
    }

}