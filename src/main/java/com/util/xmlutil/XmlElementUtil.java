package com.util.xmlutil;

import com.util.logutil.MyLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * XML工具类
 *
 * @author zhongtao on 2019/1/3
 */
public class XmlElementUtil {

    /**
     * 日志
     */
    private static final MyLogger LOGGER = new MyLogger(XmlElementUtil.class);

    /**
     * 方法名称：getNodeList
     * 方法功能：获取父结点parent的所有子结点
     *
     * @param parent
     * @return 返回：NodeList
     */
    public NodeList getNodeList(Element parent) {
        return parent.getChildNodes();
    }

    /**
     * 方法名称：getElementsByName
     * 方法功能：在父结点中查询指定名称的结点集
     *
     * @param parent
     * @return Element[]
     */
    public Element[] getElements(Element parent) {
        List<Node> resList = new ArrayList<Node>();
        NodeList nl = getNodeList(parent);
        for (int i = 0; i < nl.getLength(); i++) {
            Node nd = nl.item(i);
            if (nd.getNodeType() == 1) {
                resList.add(nd);
            }
        }
        Element[] res = new Element[resList.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = (Element) resList.get(i);
        }
        LOGGER.debug(parent.getNodeName() + "'s childrens num:" + res.length);
        return res;
    }

    /**
     * 方法名称：getElementsByName
     * 方法功能：在父结点中查询指定名称的结点集
     *
     * @param parent
     * @param name
     * @return Element[]
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Element[] getElementsByName(Element parent, String name) {
        List resList = new ArrayList();
        NodeList nl = getNodeList(parent);
        for (int i = 0; i < nl.getLength(); i++) {
            Node nd = nl.item(i);
            if (nd.getNodeName().equals(name)) {
                resList.add(nd);
            }
        }
        Element[] res = new Element[resList.size()];
        for (int i = 0; i < res.length; i++) {
            res[i] = (Element) resList.get(i);
        }
        LOGGER.debug(parent.getNodeName() + "'s children of " + name + "'s num:" + res.length);
        return res;
    }

    /**
     * 方法名称：getElementName
     * 方法功能：获取指定Element的名称
     *
     * @param element
     * @return String
     */
    public String getElementName(Element element) {
        return element.getNodeName();
    }

    /**
     * 方法名称：getElementValue
     * 方法功能：获取指定Element的值
     *
     * @param element
     * @return String
     */
    public String getElementValue(Element element) {
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            // 是一个Text Node
            if (nl.item(i).getNodeType() == Node.TEXT_NODE) {
                LOGGER.debug(element.getNodeName() + " has a Text Node.");
                return element.getFirstChild().getNodeValue();
            }
        }
        LOGGER.error(element.getNodeName() + " hasn't a Text Node.");
        return null;
    }


    /**
     * 方法名称：getElementAttr
     * 方法功能：获取指定Element的属性attr的值
     *
     * @param element
     * @param attr
     * @return String
     */
    public String getElementAttr(Element element, String attr) {
        return element.getAttribute(attr);
    }

    /**
     * 方法名称：setElementValue
     * 方法功能：设置指定Element的值
     *
     * @param element
     * @param val
     */
    public void setElementValue(Element element, String val) {
        Node node = element.getOwnerDocument().createTextNode(val);
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node nd = nl.item(i);
            // 是一个Text Node
            if (nd.getNodeType() == Node.TEXT_NODE) {
                nd.setNodeValue(val);
                LOGGER.debug("modify " + element.getNodeName() + "'s node value succe.");
                return;
            }
        }
        LOGGER.debug("new " + element.getNodeName() + "'s node value succe.");
        element.appendChild(node);
    }

    /**
     * 方法名称：setElementAttr
     * 方法功能：设置结点Element的属性
     *
     * @param element
     * @param attr
     * @param attrVal
     */
    public void setElementAttr(Element element, String attr, String attrVal) {
        element.setAttribute(attr, attrVal);
    }

    /**
     * 方法名称：addElement
     * 方法功能：在parent下增加结点child
     *
     * @param parent
     * @param child
     */
    public void addElement(Element parent, Element child) {
        parent.appendChild(child);
    }

    /**
     * 方法名称：addElement
     * 方法功能：在parent下增加字符串tagName生成的结点
     *
     * @param parent
     * @param tagName
     */
    public void addElement(Element parent, String tagName) {
        Document doc = parent.getOwnerDocument();
        Element child = doc.createElement(tagName);
        parent.appendChild(child);
    }

    /**
     * 方法名称：addElement
     * 方法功能：在parent下增加tagName的Text结点，且值为text
     *
     * @param parent
     * @param tagName
     * @param text
     */
    public void addElement(Element parent, String tagName, String text) {
        Document doc = parent.getOwnerDocument();
        Element child = doc.createElement(tagName);
        setElementValue(child, text);
        parent.appendChild(child);
    }

    /**
     * 方法名称：removeElement
     * 方法功能：将父结点parent下的名称为tagName的结点移除
     *
     * @param parent
     * @param tagName
     */
    public void removeElement(Element parent, String tagName) {
        LOGGER.debug("remove " + parent.getNodeName() + "'s children by tagName " + tagName + " begin...");
        NodeList nl = parent.getElementsByTagName(tagName);
        List<Node> ndList = new ArrayList<Node>();
        for (int i = 0; i < nl.getLength(); i++) {
            ndList.add(nl.item(i));
        }
        for (Node node : ndList) {
            parent.removeChild(node);
        }
        nl = parent.getElementsByTagName(tagName);
        LOGGER.debug("remove " + parent.getNodeName() + "'s children by tagName " + tagName + " end.");
    }
}
