package com.util.xmlutil;


import com.util.traceutil.StackTraceUtil;
import de.odysseus.staxon.json.JsonXMLConfig;
import de.odysseus.staxon.json.JsonXMLConfigBuilder;
import de.odysseus.staxon.json.JsonXMLInputFactory;
import de.odysseus.staxon.json.JsonXMLOutputFactory;
import de.odysseus.staxon.xml.util.PrettyXMLEventWriter;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;


/**
 * XML 解析
 *
 * @author zt1994 2019/8/16 17:27
 */
public class XmlParserUtil {

    private static final Logger logger = LoggerFactory.getLogger(XmlParserUtil.class);


    /**
     * json 字符转 xml
     *
     * @param json
     * @return
     */
    public static String json2Xml(String json) {
        StringReader input = new StringReader(json);
        StringWriter output = new StringWriter();
        JsonXMLConfig config = new JsonXMLConfigBuilder().multiplePI(false).repairingNamespaces(false).build();
        try {
            XMLEventReader reader = new JsonXMLInputFactory(config).createXMLEventReader(input);
            XMLEventWriter writer = XMLOutputFactory.newInstance().createXMLEventWriter(output);
            writer = new PrettyXMLEventWriter(writer);
            writer.add(reader);
            reader.close();
            writer.close();
        } catch (Exception e) {
            logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
        } finally {
            try {
                output.close();
                input.close();
            } catch (IOException e) {
                logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
            }
        }
        //remove <?xml version="1.0" encoding="UTF-8"?>
        int len = 38;
        if (output.toString().length() >= len) {
            return output.toString().substring(39);
        }
        return output.toString();
    }


    /**
     * xml 字符 转 json 字符
     *
     * @param xml
     * @return
     */
    public static String xml2Json(String xml) {
        StringReader input = new StringReader(xml);
        StringWriter output = new StringWriter();
        JsonXMLConfig config = new JsonXMLConfigBuilder().autoArray(true).autoPrimitive(true).prettyPrint(true).build();
        try {
            XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(input);
            XMLEventWriter writer = new JsonXMLOutputFactory(config).createXMLEventWriter(output);
            writer.add(reader);
            reader.close();
            writer.close();
        } catch (Exception e) {
            logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
        } finally {
            try {
                output.close();
                input.close();
            } catch (IOException e) {
                logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
            }
        }
        return output.toString();
    }


    /**
     * 获取xml文件root参数
     *
     * @param name
     * @param xml
     * @return
     */
    public static String getRootAttribute(String name, String xml) {
        try {
            Document document = DocumentHelper.parseText(xml);
            Element nodeElement = document.getRootElement();
            if (nodeElement != null) {
                List<Attribute> attributes = nodeElement.attributes();
                if (attributes != null && attributes.size() > 0) {
                    return nodeElement.attribute(name).getValue();
                }
            }
            return null;
        } catch (Exception e) {
            logger.error("错误：{}", StackTraceUtil.getStackTraceInfo(e));
        }
        return null;
    }


    /**
     * 是否是xml文本
     *
     * @param xml
     * @return
     */
    public static Boolean isXmlDocument(String xml) {
        boolean flag = true;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            builder.parse(new InputSource(new StringReader(xml)));
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

}
