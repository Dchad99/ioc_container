package com.ioc.container.reader;

import com.ioc.container.domain.BeanDefinition;

import java.util.List;

public interface BeanDefinitionReader {
    List<BeanDefinition> readBeanDefinitions();
}
