package com.ioc.container.context;

import com.ioc.container.domain.Bean;
import com.ioc.container.domain.BeanDefinition;
import com.ioc.container.reader.BeanDefinitionReader;
import com.ioc.container.reader.XMLBeanDefinitionReader;

import java.util.List;

public class ClassPathApplicationContext implements ApplicationContext<Bean> {
    private BeanDefinitionReader beanDefinitionReader;
    private List<Bean> beans;
    List<BeanDefinition> beanDefinitions;

    // the source that should be loaded
    public ClassPathApplicationContext(String... stream) {
        beanDefinitionReader = new XMLBeanDefinitionReader(stream);

        beanDefinitions = getBeanDefinitions();
        beans = createBeans(beanDefinitions);

        injectValueDependencies(beanDefinitions, beans);
        injectRefDependencies(beanDefinitions, beans);
    }

    private List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitionReader.readBeanDefinitions();
    }


    private List<Bean> createBeans(List<BeanDefinition> beanDefinitionList) {
        return null;
    }

    private void injectValueDependencies(List<BeanDefinition> beanDefinitionList, List<Bean> beanList) {

    }

    private void injectRefDependencies(List<BeanDefinition> beanDefinitionList, List<Bean> beanList) {

    }

    @Override
    public Object getBean(String beanId) {
        return null;
    }

    @Override
    public Bean getBean(Class<Bean> clazz) {
        return null;
    }

    @Override
    public Bean getBean(String id, Class<Bean> clazz) {
        return null;
    }

    @Override
    public List<String> getBeanNames() {
        return null;
    }
}
