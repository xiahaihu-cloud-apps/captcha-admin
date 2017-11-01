package com.shearf.cloud.apps.captcha.admin.domain.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiahaihu2009@gmail.com
 */
@Data
public class SimpleCaptcha implements Serializable {

    private static final long serialVersionUID = -3625745711355822553L;

    /**
     * ID
     */
    private Integer id;

    /**
     *  验证码
     */
    private String code;

    /**
     * 验证码图片
     */
    private String imgUrl;

    /**
     * 创建时间
     */
    private Date createTime;
}
