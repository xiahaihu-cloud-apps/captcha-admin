package com.shearf.cloud.apps.captcha.admin.job;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.shearf.cloud.apps.captcha.admin.common.Constant;
import com.shearf.cloud.apps.captcha.admin.domain.bean.ConfigValue;
import com.shearf.cloud.apps.captcha.admin.domain.model.SimpleCaptcha;
import com.shearf.cloud.apps.captcha.admin.domain.param.SimpleCaptchaQueryParam;
import com.shearf.cloud.apps.captcha.admin.service.SimpleCaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author xiahaihu2009@gmail.com
 */
@Component
@Slf4j
public class SimpleCaptchaJob implements InitializingBean {

    @Resource
    private SimpleCaptchaService simpleCaptchaService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ConfigValue configValue;

    @Scheduled(cron = "0 0 4 * * ?")
    public void createDailyCaptcha() {
        log.info("执行创建简单验证码的任务");

        ExecutorService executorService = Executors.newFixedThreadPool(configValue.getCreateCaptchaPoolNum());
        int total = configValue.getDailyTotalCaptcha();
        int peer = configValue.getThreadCreateCaptchaNum();
        int n = total / peer;
        List<Future> futures = new ArrayList<>(n);
        while (n-- > 0) {
            Future<Boolean> result = executorService.submit(() -> {
                simpleCaptchaService.create(peer);
                return true;
            });
            futures.add(result);
        }
        executorService.shutdown();

        boolean success = true;

        while (true) {
            int successNum = 0;

            for (Future future : futures) {
                if (future.isDone()) {
                    try {
                        boolean a = (Boolean) future.get();
                        if (!a) {
                            success = false;
                            break;
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        success = false;
                        break;
                    }
                    successNum++;
                }
            }

            if (successNum == futures.size()) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (success) {
            cacheDailyCaptcha();
        } else {
            log.error("创建验证码发生错误");
        }
        log.info("执行创建验证码任务成功");
    }

    public void cacheDailyCaptcha() {
        log.info("缓存验证码");
        int pageSize = configValue.getThreadCacheCaptchaNum();
        DateTime nowDateTime = new DateTime();
        DateTime startDateTime = nowDateTime.withTimeAtStartOfDay();
        DateTime endDateTime = startDateTime.plusDays(1);
        SimpleCaptchaQueryParam param = new SimpleCaptchaQueryParam();
        param.setStartTime(startDateTime.toDate());
        param.setEndTime(endDateTime.toDate());

        PageInfo<SimpleCaptcha> pageInfo = null;
        int i = 1;
        do {
            int page = null == pageInfo ? 1 : pageInfo.getNextPage();
            PageHelper.startPage(page, pageSize);
            List<SimpleCaptcha> captchaList = simpleCaptchaService.queryByParam(param);
            for (SimpleCaptcha captcha : captchaList) {
                ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
                valueOperations.set(Constant.SAMPLE_CAPTCHA_PREFIX + String.valueOf(i),
                        captcha.getCode() + "_" + captcha.getImgUrl(), 48, TimeUnit.HOURS);
                i++;
            }

            pageInfo = new PageInfo<>(captchaList);

        } while (!pageInfo.isIsLastPage());
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (configValue.isInitCaptcha()) {
            createDailyCaptcha();
        }
    }
}
