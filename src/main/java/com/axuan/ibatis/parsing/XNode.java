package com.axuan.ibatis.parsing;


import org.w3c.dom.CharacterData;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 对org.w3c.dom.Node的包装
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/1 19:39
 */
public class XNode {

    // org.w3c.dom.Node
    private Node node;
    // 以下都是预先将信息都解析好，放到map等数据结构中（内存中）
    private String name;
    private String body;
    private Properties attributes;
    private Properties variables;
    // XPathParser方便xpath解析
    private XPathParser xpathParser;

    // 构造时就把一些信息（属性，body）全部解析好，以便我们直接通过getter函数取得
    public XNode(XPathParser xpathParser, Node node) {
        this.node = node;
        this.name = node.getNodeName();
        this.body = parseBody(node);
        this.attributes = parseAttributes(node);
        this.xpathParser = xpathParser;
    }

    public XNode newXNode(Node node) {
        return new XNode(xpathParser, node);
    }

    // 以下方法就是把XPathParser的方再重复一遍

    public List<XNode> evalNodes(String expression) {
        return xpathParser.evalNodes(node, expression);
    }



    public XNode evalNode(String expression) {
        return xpathParser.evalNode(node, expression);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    // 以下是一些getBody的方法

    public String getStringBody() {
        return getStringBody(null);
    }

    public String getStringBody(String def) {
        if (body == null) {
            return def;
        } else {
            return body;
        }
    }

    public String getStringAttribute(String name) {
        return getStringAttribute(name, null);
    }

    public String getStringAttribute(String name, String def) {
        String value = attributes.getProperty(name);
        if (value == null) {
            return def;
        } else {
            return value;
        }
    }

    // 得到孩子，原理是调用Node.getChildNodes
    public List<XNode> getChildren() {
        List<XNode> children = new ArrayList<>();
        NodeList nodeList = node.getChildNodes();
        if (nodeList != null) {
            for (int i = 0, n = nodeList.getLength(); i < n; i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    children.add(new XNode(xpathParser, node));
                }
            }
        }
        return children;
    }

    // 得到孩子，返回Properties，孩子的格式肯定都有name,value属性
    public Properties getChildrenAsProperties() {
        Properties properties = new Properties();
        for (XNode child : getChildren()) {
            String name = child.getStringAttribute("name");
            String value = child.getStringAttribute("value");
            if (name != null && value != null) {
                properties.setProperty(name, value);
            }
        }
        return properties;
    }


    // 以下2个方法在构造时就解析
    private Properties parseAttributes(Node n) {
        Properties attributes = new Properties();
        NamedNodeMap attributeNodes = n.getAttributes();
        if (attributeNodes != null) {
            for (int i = 0; i < attributeNodes.getLength(); i++) {
                Node attribute = attributeNodes.item(i);
                String value = PropertyParser.parse(attribute.getNodeValue(), variables);
                attributes.put(attribute.getNodeName(), value);
            }
        }
        return attributes;
    }


    private String parseBody(Node node) {
        // 取不到body，循环取孩子的body，只要去到第一个，立即返回
        String data = getBodyData(node);
        if (data == null) {
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                Node child = children.item(i);
                data = getBodyData(child);
                if (data != null) {
                    break;
                }
            }
        }

        return data;
    }

    private String getBodyData(Node child) {
        if (child.getNodeType() == Node.CDATA_SECTION_NODE
            || child.getNodeType() == Node.TEXT_NODE) {
            String data = ((CharacterData)child).getData();
            data = PropertyParser.parse(data,variables);
            return data;
        }
        return null;
    }


    public boolean getBooleanAttribute(String name, Boolean def) {
        String value = attributes.getProperty(name);
        if (value == null) {
            return def;
        } else {
            return Boolean.valueOf(value);
        }
    }
}
