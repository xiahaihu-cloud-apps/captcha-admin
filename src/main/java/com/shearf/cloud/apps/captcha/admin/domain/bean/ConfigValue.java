package com.shearf.cloud.apps.captcha.admin.domain.bean;

import lombok.Data;

/**
 * @author xiahaihu2009@gmail.com
 */
@Data
public class ConfigValue {

    /**
     * 验证码图片存储路径
     */
    private String captchaStoragePath;

    /**
     * 验证码图片链接前缀
     */
    private String captchaPathPrefix;

    /**
     * 每日生成验证码总数
     */
    private int dailyTotalCaptcha;

    /**
     * 每个线程生成验证码图片数量
     */
    private int threadCreateCaptchaNum;

    /**
     * 每次缓存验证码图数量
     */
    private int threadCacheCaptchaNum;

    /**
     * 创建验证码线程池大小
     */
    private int createCaptchaPoolNum;

    /**
     * 缓存验证码线程池大小
     */
    private int cacheCaptchaPoolNum;

    /**
     * 是否初始化验证码
     */
    private boolean initCaptcha;

}
