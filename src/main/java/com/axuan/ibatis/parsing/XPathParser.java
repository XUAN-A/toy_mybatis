package com.axuan.ibatis.parsing;

import com.axuan.ibatis.Exceptions.BuilderException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.*;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * XPath解析器，用的都是JDK的类包，封装了以下，使得使用起来更方便
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/1 16:57
 */
public class XPathParser {

    private final Document document;
    private boolean validation;
    private XPath xpath;

    public XPathParser(InputStream inputStream) {
        commonConstructor(false);
        this.document = createDocument(new InputSource(inputStream));
    }


    public XPathParser(InputStream inputStream, boolean validation) {
        commonConstructor(validation);
        this.document = createDocument(new InputSource(inputStream));
    }

    public XPathParser(Document document, boolean validation) {
        commonConstructor(validation);
        this.document = document;
    }

    public XPathParser(InputStream inputStream, boolean validation, Properties variables) {
        commonConstructor(validation);
        this.document = createDocument(new InputSource(inputStream));
    }

    public XPathParser(Document document, boolean validation, Properties variables) {
        commonConstructor(validation);
        this.document = document;
    }


    public XNode evalNode(String expression) {
        return evalNode(document, expression);
    }

    // 返回节点List
    public List<XNode> evalNodes(Object root, String expression) {
        ArrayList<XNode> xnodes = new ArrayList<>();
        NodeList nodes = (NodeList) evaluate(expression, root, XPathConstants.NODESET);
        for (int i = 0; i < nodes.getLength(); i++) {
            xnodes.add(new XNode(this, nodes.item(i)));
        }
        return xnodes;
    }

    // 返回节点
    public XNode evalNode(Object root, String expression) {
        Node node = (Node) evaluate(expression, root, XPathConstants.NODE);
        if (node == null) {
            return null;
        }
        return new XNode(this, node);
    }



    private Object evaluate(String expression, Object root, QName returnType) {
        try {
            // 最终合流到这里，直接调用XPath.evaluate
            return xpath.evaluate(expression, root, returnType);
        } catch (Exception e) {
            throw new BuilderException("Error evaluating XPath. Cause :" + e, e);
        }
    }

    private Document createDocument(InputSource inputSource) {
        try {
            // 这个是DOM解析方式
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(validation);

            // 名称空间
            factory.setNamespaceAware(false);
            // 忽略注释
            factory.setIgnoringComments(true);
            // 忽略空白
            factory.setIgnoringElementContentWhitespace(false);
            // 把 CDATA 节点转换为 Text节点
            factory.setCoalescing(false);
            // 扩展实体引用
            factory.setExpandEntityReferences(true);

            DocumentBuilder builder = factory.newDocumentBuilder();


            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void warning(SAXParseException exception) throws SAXException {

                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }
            });
            return builder.parse(inputSource);
        } catch (Exception e) {
            throw new BuilderException("Error creating document instance. Cause：" + e, e);
        }
    }

    private void commonConstructor(boolean validation) {
        this.validation = validation;
        // 共通构造函数，除了把参数都设置到实例变量里面去以外，还初始了XPath
        XPathFactory factory = XPathFactory.newInstance();
        this.xpath = factory.newXPath();
    }
}
