package com.ioc.container.context;

import java.util.List;

public interface ApplicationContext<T> {
    Object getBean(String beanId);

    T getBean(Class<T> clazz);

    T getBean(String id, Class<T> clazz);

    List<String> getBeanNames();
}
