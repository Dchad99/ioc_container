package com.ioc.container.parser;

import com.ioc.container.domain.BeanDefinition;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ReadXMLApplicationContext extends DefaultHandler {
    private List<BeanDefinition> beanDefinitionList;
    private BeanDefinition beanDefinition;

    private String id;
    private String className;
    private Map<String, String> valueDependencies;
    private Map<String, String> refDependencies;

    boolean isBeanScope = false;

    public ReadXMLApplicationContext() {
        beanDefinitionList = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        beanDefinition = new BeanDefinition();
        if (qName.equalsIgnoreCase("bean")) {
            if (attributes.getValue("id") == null) {
                log.warn("Parse error, bean doesn't have attribute ID");
            }
            id = attributes.getValue("id");

            if (attributes.getValue("class") == null) {
                log.warn("Parse error, bean doesn't have attribute CLASS");
            }
            className = attributes.getValue("class");

            valueDependencies = new HashMap<>();
            refDependencies = new HashMap<>();
            isBeanScope = true;
        } else if (qName.equalsIgnoreCase("property")) {
            if (!isBeanScope) {
                log.warn("Parse error, because property tag is out of bean");
            }

            if (attributes.getValue("ref") != null) {
                if (attributes.getValue("name") == null || attributes.getValue("ref") == null) {
                    log.warn("Parse error, property tag doesn't have attributes NAME or REF");
                }
                refDependencies.put(attributes.getValue("name"), attributes.getValue("ref"));
            } else {
                if (attributes.getValue("name") == null || attributes.getValue("value") == null) {
                    log.warn("Parse error, property tag doesn't have attributes NAME or VALUE");
                }
                valueDependencies.put(attributes.getValue("name"), attributes.getValue("value"));
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("bean")) {
            isBeanScope = false;
            beanDefinition.setId(id);
            beanDefinition.setClassName(className);
            beanDefinition.setValueDependencies(valueDependencies);
            beanDefinition.setRefDependencies(refDependencies);
            beanDefinitionList.add(beanDefinition);
        }
    }

    public List<BeanDefinition> getBeanDefinitionList() {
        return beanDefinitionList;
    }

}
