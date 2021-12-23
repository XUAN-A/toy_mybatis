package com.axuan.ibatis.parsing;

import java.util.Properties;

/**
 * 属性解析器
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/1 19:50
 */
public class PropertyParser {


    public PropertyParser() {
    }

    public static String parse(String string, Properties variables) {
        VariableTokenHandler handler = new VariableTokenHandler(variables);
        GenericTokenParser parser = new GenericTokenParser("${", "}", handler);
        return parser.parse(string);
    }

    // 就是一个map，用对应的value替换key
    private static class VariableTokenHandler implements TokenHandler {
        private Properties variables;

        public VariableTokenHandler(Properties variables) {
            this.variables = variables;
        }

        @Override
        public String handleToken(String content) {
            if (variables != null && variables.contains(content)) {
                return variables.getProperty(content);
            }
            return "${" + content + "}";
        }
    }
}
