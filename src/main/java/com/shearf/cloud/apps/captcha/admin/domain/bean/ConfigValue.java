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

}
