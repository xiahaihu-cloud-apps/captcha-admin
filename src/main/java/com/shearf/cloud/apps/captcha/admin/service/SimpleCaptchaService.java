package com.shearf.cloud.apps.captcha.admin.service;

import com.shearf.cloud.apps.captcha.admin.domain.model.SimpleCaptcha;
import com.shearf.cloud.apps.commons.foundation.mybatis.IGenericService;

/**
 * @author xiahaihu2009@gmail.com
 */
public interface SimpleCaptchaService extends IGenericService<SimpleCaptcha, Integer> {

    /**
     * 生成验证码图片与验证码，并新增记录到数据库中
     */
    void create();

    /**
     * 生成验证码图片与验证码，并新增记录到数据库中
     * @param num 生成数据
     */
    void create(int num);


    /**
     * 根据日期进行创建验证码与图片
     * @param date
     */
    void createByDate(String date);

    /**
     * 根据日期和数量创建验证码与图片
     * @param date
     * @param num
     */
    void createByDate(String date, int num);
}
