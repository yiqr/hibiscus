package org.yiqr.hibiscus.common.core.component;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * @author rose
 * @create 2023/7/14 19:21
 */
@Component
public class SpringContextHelper implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static void setApplication(ApplicationContext applicationContext) {
        if (SpringContextHelper.applicationContext == null) {
            SpringContextHelper.applicationContext = applicationContext;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        setApplication(applicationContext);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    public static <T> Collection<T> getBeansOfType(Class<T> clazz) {
        Map<String, T> beansOfType = getApplicationContext().getBeansOfType(clazz);
        return beansOfType.values();
    }
}
