package com.ioc.container.reader;

import com.ioc.container.domain.BeanDefinition;
import com.ioc.container.parser.ReadXMLApplicationContext;
import lombok.extern.slf4j.Slf4j;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.List;

@Slf4j
public class XMLBeanDefinitionReader implements BeanDefinitionReader {
    private String[] sources;
    private List<BeanDefinition> beanDefinitionList;

    public XMLBeanDefinitionReader(String... sources) {
        this.sources = sources;
    }

    @Override
    public List<BeanDefinition> readBeanDefinitions() {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        for (String source : sources) {
            try {
                SAXParser readXMLApplicationContext = saxParserFactory.newSAXParser();
                ReadXMLApplicationContext handler = new ReadXMLApplicationContext();
                readXMLApplicationContext.parse(
                        this.getClass().getClassLoader().getResourceAsStream(source),
                        handler);
                beanDefinitionList = handler.getBeanDefinitionList();
            } catch (ParserConfigurationException | SAXException | IOException e) {
                log.info("SAX parsing error", e);
            }
        }
        return beanDefinitionList;
    }

}
