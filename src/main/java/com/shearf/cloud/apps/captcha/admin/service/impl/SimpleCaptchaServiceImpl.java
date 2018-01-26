package com.shearf.cloud.apps.captcha.admin.service.impl;

import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.utils.encoder.EncoderHelper;
import com.shearf.cloud.apps.captcha.admin.dal.mapper.SimpleCaptchaMapper;
import com.shearf.cloud.apps.captcha.admin.domain.bean.ConfigValue;
import com.shearf.cloud.apps.captcha.admin.domain.model.SimpleCaptcha;
import com.shearf.cloud.apps.captcha.admin.domain.param.SimpleCaptchaQueryParam;
import com.shearf.cloud.apps.captcha.admin.service.SimpleCaptchaService;
import com.shearf.cloud.apps.commons.foundation.mybatis.AbstractGenericService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author xiahaihu2009@gmail.com
 */
@Service
@Slf4j
public class SimpleCaptchaServiceImpl extends AbstractGenericService<SimpleCaptcha, Integer, SimpleCaptchaMapper> implements SimpleCaptchaService {

    @Resource
    private ConfigurableCaptchaService captchaService;

    @Resource
    private ConfigValue configValue;

    @Resource
    private SimpleCaptchaMapper simpleCaptchaMapper;

    @Override
    protected SimpleCaptchaMapper getMapper() {
        return simpleCaptchaMapper;
    }

    @Override
    public void create() {
        create(1);
    }

    @Override
    public void create(int num) {
        String today = DateFormatUtils.format(new Date(), "yyyyMMdd");
        createByDate(today, num);
    }

    @Override
    public void createByDate(String date) {
        createByDate(date, 1);
    }

    @Override
    public void createByDate(String date, int num) {
        log.debug("创建日期为{}, 数量为{}的简单验证码", date, num);

        List<SimpleCaptcha> simpleCaptchaList = new ArrayList<>(num);

        while (num-- > 0) {
            String ext = ".png";
            String captchaFile = UUID.randomUUID().toString().trim().replaceAll("-", "") + ext;
            String captchaPath = configValue.getCaptchaPathPrefix() + "/" + date + "/" + captchaFile;
            String captchaRealDirPath = configValue.getCaptchaStoragePath() + "/" + date;
            String captchaRealPath = captchaRealDirPath + "/" + captchaFile;

            File captchaRealDir = new File(captchaRealDirPath);
            synchronized (this) {
                if (!captchaRealDir.exists()) {
                    if (!captchaRealDir.mkdirs()) {
                        log.error("创建验证码文件夹目录出错, 目录:{}", captchaRealDirPath);
                    }
                }
            }


            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(captchaRealPath);

                String code = EncoderHelper.getChallangeAndWriteImage(captchaService, "png", fos);
                SimpleCaptcha simpleCaptcha = new SimpleCaptcha();
                simpleCaptcha.setCode(code);
                simpleCaptcha.setImgUrl(captchaPath);
                simpleCaptcha.setCreateTime(new Date());

                simpleCaptchaList.add(simpleCaptcha);

            } catch (IOException e) {
                log.error("创建验证码图片失败, imageFile:{}", captchaRealPath);
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        insert(simpleCaptchaList);
    }

    @Override
    public void deleteTodayCaptcha() {
        SimpleCaptchaQueryParam param = new SimpleCaptchaQueryParam();
        DateTime nowDateTime = new DateTime();
        DateTime startDateTime = nowDateTime.withTimeAtStartOfDay();
        DateTime endDateTime = startDateTime.plusDays(1);
        param.setStartTime(startDateTime.toDate());
        param.setEndTime(endDateTime.toDate());
        simpleCaptchaMapper.deleteByParam(param);
    }
}
