package org.yiqr.hibiscus.common.core.web;

import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.CustomValidatorBean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

/**
 * @author rose
 * @create 2023/7/14 19:26
 */
public abstract class AbstractWebMvcConfiguration implements WebMvcConfigurer {
    private static final String MESSAGE_BASE_NAMES = "classpath:i18n/messages";
    private static final String DEFAULT_ENCODING = "UTF-8";
    protected MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename(MESSAGE_BASE_NAMES);
        messageSource.setDefaultEncoding(DEFAULT_ENCODING);
        return messageSource;
    }


    protected LocaleResolver localeResolver() {
        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
        sessionLocaleResolver.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return sessionLocaleResolver;
    }

    protected LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        return localeChangeInterceptor;
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource());
        return localValidatorFactoryBean;
    }

    public jakarta.validation.Validator beanValidator() {
        CustomValidatorBean validatorBean = new CustomValidatorBean();
        validatorBean.setMessageInterpolator(new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource())));
        return validatorBean;
    }

    public MethodValidationPostProcessor methodValidationPostProcessor(Environment environment) {
        MethodValidationPostProcessor processor = new MethodValidationPostProcessor();
        processor.setProxyTargetClass(determineProxyTargetClass(environment));
        processor.setValidator(beanValidator());
        return processor;
    }

    private static boolean determineProxyTargetClass(Environment environment) {
        return environment.getProperty("spring.aop.proxy-target-class", Boolean.class, true);
    }

    public MessageSourceAccessor messageSourceAccessor() {
        return new MessageSourceAccessor(messageSource());
    }


}