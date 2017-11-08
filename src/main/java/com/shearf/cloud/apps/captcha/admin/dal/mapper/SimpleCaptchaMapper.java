package com.shearf.cloud.apps.captcha.admin.dal.mapper;

import com.shearf.cloud.apps.captcha.admin.domain.model.SimpleCaptcha;
import com.shearf.cloud.apps.captcha.admin.domain.param.SimpleCaptchaQueryParam;
import com.shearf.cloud.apps.commons.foundation.mybatis.IGenericMapper;
import org.springframework.stereotype.Repository;

/**
 * @author xiahaihu2009@gmail.com
 */
@Repository
public interface SimpleCaptchaMapper extends IGenericMapper<SimpleCaptcha, Integer> {

    /**
     * 根据时间范围对验证码进行删除
     *
     * @param param
     * @return
     */
    int deleteByParam(SimpleCaptchaQueryParam param);
}
