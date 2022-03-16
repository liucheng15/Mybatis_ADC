package org.db.ioc.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IoctoDATA extends  CRUDUtils{

    public  Object databaseIocObject(Class<?> classes, ResultSet rs){
        Object ob=null;
        if (rs !=null){
            try {
                if (rs.next()){
                    ob=classes.getDeclaredConstructor().newInstance();
                    ResultSetMetaData metaData=rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i=1;i<=columnCount;i++){
                        String columnName=metaData.getColumnName(i);
                        Object object=rs.getObject(columnName);
                        Method[] methods=classes.getDeclaredMethods();
                        String methodName=null;
                        for (Method method:methods){
                            methodName=method.getName();
                            if (methodName.equalsIgnoreCase("set"+columnName)){
                                method.invoke(ob,object);
                                break;
                            }
                        }
                    }
                }
            } catch (SQLException | NoSuchMethodException | InstantiationException
                    | IllegalAccessException | InvocationTargetException throwables) {
                throwables.printStackTrace();
            }finally {
              closeResultSet(rs);
            }
        }
        return ob;
    }
    public List<Object> databaseIocObjectList(Class<?> classes, ResultSet rs){
        Object ob=null;
        List<Object> objects=new ArrayList<>();
        if (rs !=null){
            try {
                while (rs.next()){
                    ob=classes.getDeclaredConstructor().newInstance();
                    ResultSetMetaData metaData=rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    for (int i=1;i<=columnCount;i++){
                        String columnName=metaData.getColumnName(i);
                        Object object=rs.getObject(columnName);
                        Method[] methods=classes.getDeclaredMethods();
                        String methodName=null;
                        for (Method method:methods){
                            methodName=method.getName();
                            if (methodName.equalsIgnoreCase("set"+columnName)){
                                method.invoke(ob,object);
                                break;
                            }
                        }
                    }
                    objects.add( ob );
                }
            } catch (SQLException | NoSuchMethodException | InstantiationException
                    | IllegalAccessException | InvocationTargetException throwables) {
                throwables.printStackTrace();
            }finally {
                closeResultSet(rs);
            }
        }
        return objects;
    }
   public  Object getMapper(Class<?> classes,String sql,Object[] objects){
        CRUDUtils crudUtils=new CRUDUtils();
        ResultSet rs=crudUtils.QUERYUtils(sql, objects);
        Object object=databaseIocObject(classes,rs);
        return  object;
   }
    public  List<Object> getMapperList(Class<?> classes,String sql,Object[] objects){
        CRUDUtils crudUtils=new CRUDUtils();
        ResultSet rs=crudUtils.QUERYUtils(sql, objects);
        List<Object> obs=databaseIocObjectList(classes,rs);
        return  obs;
    }

    public static void main(String[] args) {
        String sql="select * from phone where phoneId=?";
        IoctoDATA ioctoDATA=new IoctoDATA();
        Object[] objects={1};
        Phone phone=(Phone) ioctoDATA.getMapper(Phone.class,sql,objects);
        System.out.println(phone.getPhoneId());
        System.out.println(phone.getPhoneName());
        System.out.println(phone.getPersonId());

    }
}
