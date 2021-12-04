package com.ioc.container.context;

import com.ioc.container.domain.Bean;
import com.ioc.container.domain.BeanDefinition;
import com.ioc.container.reader.BeanDefinitionReader;
import com.ioc.container.reader.XMLBeanDefinitionReader;
import com.ioc.exception.NotFoundBeanException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ClassPathApplicationContext implements ApplicationContext {
    private final BeanDefinitionReader beanDefinitionReader;
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


    @Override
    public Object getBean(String beanId) {
        for (Bean bean : beans) {
            if (bean.getId().equals(beanId)) {
                return bean.getValue();
            }
        }
        return null;
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        T result = null;
        for (Bean bean : beans) {
            Class<?> aClass = bean.getValue().getClass();
            if (clazz.isAssignableFrom(aClass)) {
                result = clazz.cast(bean.getValue());
                return result;
            }
        }
        return null;
    }

    @Override
    public <T> T getBean(String id, Class<T> clazz) {
        for (Bean bean : beans) {
            if (bean.getValue().getClass().isAssignableFrom(clazz) && bean.getId().equals(id)) {
                return (T) bean;
            }
        }
        return null;
    }

    @Override
    public List<String> getBeanNames() {
        return beans.stream().map(Bean::getId).toList();
    }

    private List<BeanDefinition> getBeanDefinitions() {
        return beanDefinitionReader.readBeanDefinitions();
    }

    @SneakyThrows
    private List<Bean> createBeans(List<BeanDefinition> beanDefinitionList) {
        beans = new ArrayList<>();
        for (BeanDefinition beanDefinition : beanDefinitionList) {
            Bean bean = new Bean();

            Object object = Class.forName(beanDefinition.getClassName()).getDeclaredConstructor().newInstance();

            bean.setId(beanDefinition.getId());
            bean.setValue(object);
            beans.add(bean);
        }
        return beans;
    }

    private void injectValueDependencies(List<BeanDefinition> beanDefinitionList, List<Bean> beanList) {
        for (BeanDefinition beanDefinition : beanDefinitionList) {
            if (beanDefinition.getValueDependencies() != null) {
                Map<String, String> values = beanDefinition.getValueDependencies();
                Bean bean = getBeanById(beanList, beanDefinition.getId());
                if(bean != null) {
                    values.forEach((k, v) -> injectValueBySetter(bean, k, v));
                } else {
                    throw new NotFoundBeanException();
                }
            }
        }
    }

    private Bean getBeanById(List<Bean> beans, String id) {
        for (Bean bean : beans) {
            if (bean.getId().equals(id)) {
                return bean;
            }
        }
        return null;
    }

    @SneakyThrows
    private void injectValueBySetter(Bean bean, String fieldName, String fieldValue) {
        try {
            Object storedObject = bean.getValue();
            Field declaredField = storedObject.getClass().getDeclaredField(fieldName);
            declaredField.setAccessible(true);

            for (Method method : storedObject.getClass().getMethods()) {
                if (method.getName().startsWith("set")
                        && method.getName().toLowerCase().endsWith(fieldName.toLowerCase())) {
                    Class<?> type = declaredField.getType();
                    if (int.class.equals(type)) {
                        method.invoke(storedObject, Integer.parseInt(fieldValue));
                    } else if (double.class.equals(type)) {
                        method.invoke(storedObject, Double.parseDouble(fieldValue));
                    } else if (boolean.class.equals(type)) {
                        method.invoke(storedObject, Boolean.parseBoolean(fieldValue));
                    } else if (short.class.equals(type)) {
                        method.invoke(storedObject, Short.parseShort(fieldValue));
                    } else if (char.class.equals(type)) {
                        method.invoke(storedObject, fieldValue.charAt(0));
                    } else if (long.class.equals(type)) {
                        method.invoke(storedObject, Long.parseLong(fieldValue));
                    } else if (byte.class.equals(type)) {
                        method.invoke(storedObject, Byte.parseByte(fieldValue));
                    } else {
                        method.invoke(storedObject, fieldValue);
                    }
                }
            }
        } catch (NoSuchFieldException | IllegalAccessException | InvocationTargetException e) {
            log.warn("Exception");
        }
    }


    private void injectRefDependencies(List<BeanDefinition> beanDefinitionList, List<Bean> beanList) {
        for (BeanDefinition beanDefinition : beanDefinitionList) {
            if (beanDefinition.getRefDependencies() != null) {
                Bean bean = getBeanById(beanList, beanDefinition.getId());
                if (bean != null) {
                    for (Map.Entry<String, String> entry : beanDefinition.getRefDependencies().entrySet()) {
                        Bean beanToInject = getBeanById(beans, entry.getValue());
                        if (beanToInject != null) {
                            injectReferences(bean, entry.getKey(), beanToInject.getValue());
                        }
                    }
                }
            }
        }
    }

    private void injectReferences(Bean bean, String fieldName, Object refObject) {
        try {
            Object storedObject = bean.getValue();
            for (Method method : storedObject.getClass().getMethods()) {
                if (method.getName().startsWith("set") && method.getName().toLowerCase().endsWith(fieldName.toLowerCase())) {
                    method.invoke(storedObject, refObject);
                }
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
