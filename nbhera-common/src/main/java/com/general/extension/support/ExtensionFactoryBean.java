package com.general.extension.support;

import com.general.nbhera.extension.ExtensionPoints;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;

import java.util.Objects;

/**
 * @param <T> 泛型
 * @author xvanning
 * date: 2021/6/6 22:08
 * desc: 扩展点代理对象bean
 */
@Slf4j
public class ExtensionFactoryBean<T extends ExtensionPoints> implements FactoryBean<T> {

    /**
     * 目标class
     */
    private Class<T> target;

    /**
     * 构造函数
     *
     * @param target 目标class
     */
    public ExtensionFactoryBean(Class<T> target) {
        Objects.requireNonNull(target);
        this.target = target;
    }

    @Override
    public T getObject() throws Exception {
        T proxy = ExtensionProxy.getProxyInstance(target);
        log.info("|EXT_PROXY| ExtensionFactoryBean getObject, proxy = {}", proxy.toString());
        return proxy;
    }

    @Override
    public Class<?> getObjectType() {
        return target;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
