package org.db.ioc.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Dome01 extends  CRUDUtils{
    public List<Object> persontest(Class classes, String sql, Object[] objects){
        ResultSet rs=QUERYUtils(sql, objects);
        List<Object> classesObject=new ArrayList<>();
        try {
            while (rs.next()){
                ResultSetMetaData metaData= rs.getMetaData();
                //获取列数
                int count=metaData.getColumnCount();
                //System.out.println(count);

                Object instanceClass=classes.getDeclaredConstructor().newInstance();

                for (int c=1;c<=count;c++){
                    //获取列数名字
                    String columnName=metaData.getColumnName(c);
                    //System.out.println(columnName);
                    //获取每一列对应数据表中的值
                  Object columnValue= rs.getObject(columnName);
                    //System.out.println(columnValue);
                   //获取对象中所有的方法
                    Method[] methods=classes.getDeclaredMethods();
                    for (Method method :methods){
                        String methodName=method.getName();
                        if (methodName.equalsIgnoreCase("set"+columnName)){
                            //把这些值加载到对应的对象的set+columnName方法中
                            method.invoke(instanceClass,columnValue);
                        }
                    }
                }
                classesObject.add(instanceClass);
            }
        } catch (SQLException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException throwables) {
            throwables.printStackTrace();
        }finally {
            closeResultSet(rs);
        }
        return classesObject;
    }

    public static void main(String[] args) {
        String sql="select * from person";
        Dome01 dome01=new Dome01();
        List<Object> persons=dome01.persontest(Person.class,sql,null);
        System.out.println(persons);
    }
}
