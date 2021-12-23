package com.axuan.ibatis.binding;

import com.axuan.ibatis.mapping.MappedStatement;
import com.axuan.ibatis.mapping.SqlCommandType;
import com.axuan.ibatis.session.Configuration;
import com.axuan.ibatis.session.SqlSession;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * 映射器方法
 * @author ZhouXuan
 * @version 1.0
 * @date 2021/12/13 8:16
 */
public class MapperMethod {
    private final SqlCommand command;
    private final MethodSignature method;

    public MapperMethod(Class<?> mapperInterface, Method method, Configuration configuration) {
        this.command = new SqlCommand(configuration, mapperInterface, method);
        this.method = new MethodSignature(configuration, method);
    }


    // 执行
    public Object execute(SqlSession sqlSession, Object[] args) {
        Object result = null;
        // 可以看到执行时就是4种情况，insert|update|delete|select,分别调用SqlSession的4大类方法
        if(SqlCommandType.INSERT == command.getType()) {
            result =  sqlSession.insert(command.getName(), args);
        } else if (SqlCommandType.UPDATE == command.getType()) {
            result = sqlSession.update(command.getName(), args);
        } else if (SqlCommandType.DELETE == command.getType()) {
            result = sqlSession.delete(command.getName(), args);
        } else if (SqlCommandType.SELECT == command.getType()) {
            if (method.returnsMany) {
                // 如果结果有多条记录
                result = executeForMany(sqlSession, args);
            } else {
                // 否则就是一条记录
                result = sqlSession.selectOne(command.getName(), args);
            }
        }
        return result;
    }



    // 多条记录
    private <E> Object executeForMany(SqlSession sqlSession, Object[] args) {
        List<E> result;
        result = sqlSession.<E>selectList(command.getName(), args);
        return result;
    }


    // SQL命令，静态内部类
    public static class SqlCommand {

        private final String name;
        private final SqlCommandType type;

        public String getName() {
            return name;
        }

        public SqlCommandType getType() {
            return type;
        }

        public SqlCommand(Configuration configuration, Class<?> mapperInterface, Method method) {
            String statementName = mapperInterface.getName() + "." + method.getName();
            MappedStatement ms = null;
            if (configuration.hasStatement(statementName)) {
                ms = configuration.getMappedStatement(statementName);
            }
            if (ms == null) {
                throw new BindingException("Invalid bound statement (not found):" + statementName);
            }
            name = ms.getId();
            type = ms.getSqlCommandType();
            if (type == SqlCommandType.UNKNOWN) {
                throw new BindingException("Unknown execution method for: " + name);
            }



        }
    }


    // 方法签名，静态内部类
    public static class MethodSignature {
        private final boolean returnsMany;
        private final boolean returnsVoid;
        private final Class<?> returnType;

        public MethodSignature(Configuration configuration, Method method) {
            this.returnType = method.getReturnType();
            this.returnsVoid = void.class.equals(this.returnType);
            this.returnsMany = Collection.class.isAssignableFrom(method.getReturnType());
        }
    }
}
