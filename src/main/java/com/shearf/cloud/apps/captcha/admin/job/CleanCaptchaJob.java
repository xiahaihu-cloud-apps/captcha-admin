package com.shearf.cloud.apps.captcha.admin.job;

import com.shearf.cloud.apps.captcha.admin.dal.mapper.SimpleCaptchaMapper;
import com.shearf.cloud.apps.captcha.admin.domain.bean.ConfigValue;
import com.shearf.cloud.apps.captcha.admin.domain.param.SimpleCaptchaQueryParam;
import com.shearf.cloud.apps.captcha.admin.service.SimpleCaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.Date;

/**
 * Created by xiahaihu2009@gmail on 2017/11/4.
 */
@Component
@Slf4j
public class CleanCaptchaJob {

    @Resource
    private SimpleCaptchaMapper simpleCaptchaMapper;

    @Resource
    private SimpleCaptchaService simpleCaptchaService;

    @Resource
    private ConfigValue configValue;

    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanCaptchaImages() {

        log.info("执行验证码文件清理任务");
        // 删除前天验证码的数据
        DateTime dateTime = new DateTime();
        DateTime startDateTime = dateTime.withTimeAtStartOfDay().minusDays(2);
        Date endTime = dateTime.withTimeAtStartOfDay().minusDays(1).toDate();
        SimpleCaptchaQueryParam param = new SimpleCaptchaQueryParam();
        param.setStartTime(startDateTime.toDate());
        param.setEndTime(endTime);
        simpleCaptchaMapper.deleteByParam(param);

        //删除前天生成的验证码文件夹
        String dateDirPath = startDateTime.toString("yyyyMMdd");
        int dirDateNumber = Integer.valueOf(dateDirPath);
        File captchaDir = new File(configValue.getCaptchaStoragePath());
        synchronized (this) {
            File[] files = captchaDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    try {
                        int dirNum = Integer.valueOf(file.getName());
                        if (dirNum <= dirDateNumber) {
                            if (!file.delete()) {
                                log.error("删除验证码目录失败, {}", file.getAbsolutePath());
                            }
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
        log.info("执行验证码文件清理任务成功");
    }
}
