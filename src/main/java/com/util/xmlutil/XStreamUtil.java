package com.util.xmlutil;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * XStream工具类 xml 和 bean 之间的转换
 *
 * @author zt1994 2019/8/16 15:56
 */
public class XStreamUtil {

    /**
     * java 转换成xml
     *
     * @param obj 对象实例
     * @return String xml字符串
     */
    public static String beanToXml(Object obj) {
        System.out.println("XStream:beanToXml开始");
        XStream xstream = new XStream();
        //如果没有这句，xml中的根元素会是<包.类名>；或者说：注解根本就没生效，所以的元素名就是类的属性
        //通过注解方式的，一定要有这句话
        xstream.processAnnotations(obj.getClass());
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + xstream.toXML(obj);
    }


    /**
     * 将传入xml文本转换成Java对象
     * <p>
     * 调用的方法实例：PersonBean person=XmlUtil.toBean(xmlStr, PersonBean.class);
     *
     * @param xmlStr xml字符串
     * @param cls    xml对应的class类
     * @param <T>    xml对应的class类的实例对象
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T xmlToBean(String xmlStr, Class<T> cls) {
        XStream xstream = new XStream(new DomDriver());
        xstream.processAnnotations(cls);
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(new Class[]{cls});
        return (T) xstream.fromXML(xmlStr);
    }

}
