package com.util.xmlutil;


import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


/**
 * XML 解析
 *
 * @author zt1994 2019/8/16 17:27
 */
public class XmlParserUtil {

    /**
     * 生成空的xml文件头
     *
     * @param xmlPath
     * @return
     */
    public static Document createEmptyXmlFile(String xmlPath) {
        if (xmlPath == null || StringUtils.equals("", xmlPath)) {
            return null;
        }

        XMLWriter output;
        Document document = DocumentHelper.createDocument();
        OutputFormat format = OutputFormat.createPrettyPrint();
        try {
            output = new XMLWriter(new FileWriter(xmlPath), format);
            output.write(document);
            output.close();
        } catch (IOException e) {
            return null;
        }
        return document;
    }


    /**
     * 根据xml文件路径取得document对象
     *
     * @param xmlPath
     * @return
     * @throws DocumentException
     */
    public static Document getDocument(String xmlPath) {
        if (xmlPath == null || StringUtils.equals(xmlPath, "")) {
            return null;
        }

        File file = new File(xmlPath);
        if (!file.exists()) {
            return createEmptyXmlFile(xmlPath);
        }

        SAXReader reader = new SAXReader();
        Document document = null;
        try {
            document = reader.read(xmlPath);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return document;
    }


    /**
     * 获取 root 文本
     *
     * @param document DOC对象
     * @return
     */
    public static Element getRootNode(Document document) {
        if (document == null) {
            return null;
        }
        return document.getRootElement();
    }


    /**
     * 根据路径直接拿到根节点
     *
     * @param xmlPath
     * @return
     */
    public static Element getRootNode(String xmlPath) {
        if (xmlPath == null || StringUtils.equals(xmlPath.trim(), "")) {
            return null;
        }
        Document document = getDocument(xmlPath);
        if (document == null) {
            return null;
        }
        return getRootNode(document);
    }


    /**
     * 得到指定元素的迭代器
     *
     * @param parent
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Iterator<Element> getIterator(Element parent) {
        if (parent == null) {
            return null;
        }
        return parent.elementIterator();
    }


    /**
     * 根据子节点名称得到指定的子节点
     *
     * @param parent
     * @param childName
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Node> getChildElements(Element parent, String childName) {
        childName = childName.trim();
        if (parent == null) {
            return null;
        }
        childName += "//";
        return parent.selectNodes(childName);
    }


    /**
     * 获取子节点
     *
     * @param node
     * @return
     */
    public static List<Element> getChildList(Element node) {
        if (node == null) {
            return null;
        }
        Iterator<Element> itr = getIterator(node);
        if (itr == null) {
            return null;
        }
        List<Element> childList = new ArrayList<Element>();
        while (itr.hasNext()) {
            Element kidElement = itr.next();
            if (kidElement != null) {
                childList.add(kidElement);
            }
        }
        return childList;
    }


    /**
     * 查询没有子节点的节点，使用xpath方式
     *
     * @param parent
     * @param nodeNodeName
     * @return
     */
    public static Node getSingleNode(Element parent, String nodeNodeName) {
        nodeNodeName = nodeNodeName.trim();
        String xpath = "//";
        if (parent == null) {
            return null;
        }
        if (StringUtils.equals(nodeNodeName, "")) {
            return null;
        }
        xpath += nodeNodeName;
        return parent.selectSingleNode(xpath);
    }


    /**
     * 得到子节点，不使用xpath
     *
     * @param parent
     * @param childName
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Element getChild(Element parent, String childName) {
        childName = childName.trim();
        if (parent == null) {
            return null;
        }
        if (StringUtils.equals(childName, "")) {
            return null;
        }
        Element e = null;
        Iterator it = getIterator(parent);
        while (it != null && it.hasNext()) {
            Element k = (Element) it.next();
            if (k == null) {
                continue;
            }
            if (k.getName().equalsIgnoreCase(childName)) {
                e = k;
                break;
            }
        }
        return e;
    }


    /**
     * 判断节点是否还有子节点
     *
     * @param e
     * @return
     */
    public static boolean hasChild(Element e) {
        if (e == null) {
            return false;
        }
        return e.hasContent();
    }


    /**
     * 得到指定节点的属性的迭代器
     *
     * @param e
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Iterator<Attribute> getAttrIterator(Element e) {
        if (e == null) {
            return null;
        }
        return e.attributeIterator();
    }


    /**
     * 遍历指定节点的所有属性
     *
     * @param e
     * @return
     */
    public static List<Attribute> getAttributeList(Element e) {
        if (e == null) {
            return null;
        }
        List<Attribute> attributeList = new ArrayList<Attribute>();
        Iterator<Attribute> atrIterator = getAttrIterator(e);
        if (atrIterator == null) {
            return null;
        }
        while (atrIterator.hasNext()) {
            Attribute attribute = atrIterator.next();
            attributeList.add(attribute);
        }
        return attributeList;
    }


    /**
     * 得到指定节点的指定属性
     *
     * @param element  指定的元素
     * @param attrName 属性名称
     * @return
     */
    public static Attribute getAttribute(Element element, String attrName) {
        attrName = attrName.trim();
        if (element == null) {
            return null;
        }
        if (StringUtils.equals(attrName, "")) {
            return null;
        }
        return element.attribute(attrName);
    }


    /**
     * 获取指定节点指定属性的值
     *
     * @param e
     * @param attrName
     * @return
     */
    public static String attrValue(Element e, String attrName) {
        attrName = attrName.trim();
        if (e == null) {
            return null;
        }
        if (StringUtils.equals(attrName, "")) {
            return null;
        }
        return e.attributeValue(attrName);
    }


    /**
     * 得到指定节点的所有属性及属性值
     *
     * @param e
     * @return
     */
    public static Map<String, String> getNodeAttrMap(Element e) {
        Map<String, String> attrMap = new HashMap<String, String>();
        if (e == null) {
            return null;
        }
        List<Attribute> attributes = getAttributeList(e);
        if (attributes == null) {
            return null;
        }
        for (Attribute attribute : attributes) {
            String attrValueString = attrValue(e, attribute.getName());
            attrMap.put(attribute.getName(), attrValueString);
        }
        return attrMap;
    }


    /**
     * 遍历指定节点的下没有子节点的元素的text值
     *
     * @param e
     * @return
     */
    public static Map<String, String> getSingleNodeText(Element e) {
        Map<String, String> map = new HashMap<String, String>(16);
        if (e == null) {
            return null;
        }
        List<Element> kids = getChildList(e);
        for (Element e2 : kids) {
            if (e2.getTextTrim() != null) {
                map.put(e2.getName(), e2.getTextTrim());
            }
        }
        return map;
    }


    /**
     * 遍历根节点下，没有子节点的元素节点，并将此节点的text值放入map中返回
     *
     * @param xmlFilePath
     * @return
     */
    public static Map<String, String> getSingleNodeText(String xmlFilePath) {
        xmlFilePath = xmlFilePath.trim();
        if (StringUtils.equals(xmlFilePath, "")) {
            return null;
        }
        Element rootElement = getRootNode(xmlFilePath);
        if (!hasChild(rootElement)) {
            return null;
        }
        return getSingleNodeText(rootElement);
    }


    /**
     * one more
     */
    public enum Flag {one, more}


    /**
     * 根据xml路径和指定的节点的名称，得到指定节点,从根节点开始找
     *
     * @param xmlFilePath
     * @param tagName
     * @param flag
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getNameNode(String xmlFilePath, String tagName, Flag flag) {
        xmlFilePath = xmlFilePath.trim();
        tagName = tagName.trim();
        if (StringUtils.equals(xmlFilePath, "") || StringUtils.equals(tagName, "")) {
            return null;
        }
        Element rootElement = getRootNode(xmlFilePath);
        if (rootElement == null) {
            return null;
        }
        List<Element> tagElementList = getNameElement(rootElement, tagName);
        if (tagElementList == null) {
            return null;
        }
        switch (flag) {
            case one:
                return (T) tagElementList.get(0);
        }
        return (T) tagElementList;
    }


    /**
     * 得到指定节点下所有子节点的属性集合
     *
     * @param parent
     * @return
     */
    public static Map<Integer, Object> getNameNodeAllKidsAttributeMap(Element parent) {
        Map<Integer, Object> allAttrMap = new HashMap<Integer, Object>(16);
        if (parent == null) {
            return null;
        }
        List<Element> childElements = getChildList(parent);
        if (childElements == null) {
            return null;
        }
        for (int i = 0; i < childElements.size(); i++) {
            Element childElement = childElements.get(i);
            Map<String, String> attrMap = getNodeAttrMap(childElement);
            allAttrMap.put(i, attrMap);
        }
        return allAttrMap;
    }


    /**
     * 根据xml文件名路径和指定的节点名称得到指定节点所有子节点的所有属性集合
     *
     * @param xmlFilePath
     * @param nodeName
     * @param flag
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getNameNodeAllAttributeMap(String xmlFilePath, String nodeName, Flag flag) {
        nodeName = nodeName.trim();
        Map<String, String> allAttrMap = null;
        Map<Integer, Map<String, String>> mostKidsAllAttriMap = new HashMap<Integer, Map<String, String>>();
        if (xmlFilePath == null || StringUtils.equals(xmlFilePath, "") || nodeName.equals("")) {
            return null;
        }
        switch (flag) {
            case one:
                Element nameNode = getNameNode(xmlFilePath, nodeName, Flag.one);
                allAttrMap = getNodeAttrMap(nameNode);
                return (T) allAttrMap;
            case more:
                List<Element> nameKidsElements = getNameNode(xmlFilePath, nodeName, Flag.more);
                if (nameKidsElements != null) {
                    for (int i = 0; i < nameKidsElements.size(); i++) {
                        Element kid = nameKidsElements.get(i);
                        allAttrMap = getNodeAttrMap(kid);
                        mostKidsAllAttriMap.put(i, allAttrMap);
                    }
                }
                return (T) mostKidsAllAttriMap;
        }
        return null;
    }


    /**
     * 遍历指定的节点下所有的节点
     *
     * @param element
     * @param allKidsList
     * @return
     */
    public static List<Element> ransack(Element element, List<Element> allKidsList) {
        if (element == null) {
            return null;
        }
        if (hasChild(element)) {
            List<Element> kids = getChildList(element);
            for (Element e : kids) {
                allKidsList.add(e);
                ransack(e, allKidsList);
            }
        }
        return allKidsList;
    }


    /**
     * 得到指定节点下的指定节点集合
     *
     * @param element
     * @param nodeName
     * @return
     */
    public static List<Element> getNameElement(Element element, String nodeName) {
        nodeName = nodeName.trim();
        List<Element> kidsElements = new ArrayList<Element>();
        if (element == null) {
            return null;
        }
        if (StringUtils.equals(nodeName, "")) {
            return null;
        }
        List<Element> allKids = ransack(element, new ArrayList<Element>());
        if (allKids == null) {
            return null;
        }
        for (Element kid : allKids) {
            if (nodeName.equals(kid.getName())) {
                kidsElements.add(kid);
            }
        }
        return kidsElements;
    }


    /**
     * 验证节点是否唯一
     *
     * @param element
     * @return
     */
    public static int validateSingle(Element element) {
        int j = 1;
        if (element == null) {
            return j;
        }
        Element parent = element.getParent();
        List<Element> kids = getChildList(parent);
        if (kids != null) {
            for (Element kid : kids) {
                if (element.equals(kid)) {
                    j++;
                }
            }
        }
        return j;
    }

}
