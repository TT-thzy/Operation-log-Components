package org.operationlog.aware;

import org.operationlog.utils.SecurityUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author Smart Lee 2019/1/15 11:34
 */
public class UserGetterBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {


        if (bean instanceof UserGetterAware) {
            ((UserGetterAware) bean).setUserGetter(SecurityUtils.userGetter());
        }

        return bean;
    }

}
