package com.shearf.cloud.apps.captcha.admin.domain.model;

import com.shearf.cloud.apps.commons.foundation.base.BaseModel;
import lombok.Data;

import java.util.Date;

/**
 * @author xiahaihu2009@gmail.com
 */
@Data
public class AppAuth extends BaseModel {

    private static final long serialVersionUID = 205816172994300078L;

    /**
     * ID
     */
    private Integer id;

    /**
     * APP ID
     */
    private Integer appId;

    /**
     * 状态
     */
    private Integer status;

    /**
     * appKey
     */
    private String appKey;

    /**
     * appSecret
     */
    private String appSecret;

    /**
     * 过期时间
     */
    private Date expireTime;

}
