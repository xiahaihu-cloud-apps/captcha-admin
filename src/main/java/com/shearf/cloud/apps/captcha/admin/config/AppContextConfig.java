package com.shearf.cloud.apps.captcha.admin.config;

import com.github.bingoohuang.patchca.color.SingleColorFactory;
import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.filter.predefined.CurvesRippleFilterFactory;
import com.github.bingoohuang.patchca.word.AdaptiveRandomWordFactory;
import com.shearf.cloud.apps.captcha.admin.domain.bean.ConfigValue;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Repository;

import java.awt.*;

/**
 * @author xiahaihu2009@gmail.com
 */
@Configuration
@MapperScan(basePackages = "com.shearf.cloud.apps.captcha.admin.dal.mapper", annotationClass = Repository.class)
@EnableScheduling
public class AppContextConfig implements EnvironmentAware {

    private RelaxedPropertyResolver resolver;

    @Bean
    public ConfigurableCaptchaService captchaService() {
        ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
        cs.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
        cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));

        AdaptiveRandomWordFactory wordFactory = new AdaptiveRandomWordFactory();
        wordFactory.setMinLength(6);
        wordFactory.setMaxLength(6);

        cs.setWordFactory(wordFactory);
        return cs;
    }

    @Bean
    public ConfigValue configValue() {
        ConfigValue configValue = new ConfigValue();
        configValue.setCaptchaStoragePath(resolver.getRequiredProperty("captcha.storage.path"));
        configValue.setCaptchaPathPrefix(resolver.getRequiredProperty("captcha.path.prefix"));
        configValue.setDailyTotalCaptcha(
                resolver.getProperty("captcha.daily.total", Integer.class ,100000));
        configValue.setThreadCreateCaptchaNum(
                resolver.getProperty("captcha.peer.create.number", Integer.class, 1000));
        configValue.setThreadCacheCaptchaNum(
                resolver.getProperty("captcha.peer.cache.number", Integer.class, 1000));
        configValue.setCreateCaptchaPoolNum(
                resolver.getProperty("captcha.create.pool.num", Integer.class,
                        Runtime.getRuntime().availableProcessors() * 2 + 1));
        configValue.setCacheCaptchaPoolNum(
                resolver.getProperty("captcha.cache.pool.num", Integer.class,
                        Runtime.getRuntime().availableProcessors() * 2 + 1));
        configValue.setInitCaptcha(resolver.getProperty("captcha.init", Boolean.class, false));

        return configValue;
    }

    @Override
    public void setEnvironment(Environment environment) {
        resolver = new RelaxedPropertyResolver(environment, "app.");
    }
}
