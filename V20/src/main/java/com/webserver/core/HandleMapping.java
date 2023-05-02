package com.webserver.core;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**维护请求路径对应的业务处理方法*（某个controller的某个方法）*/

public class HandleMapping {
   //key ：请求路径(/regUser)；value： method对象(Method实例 )
    private static Map<String, Method> mapping=new HashMap<>();

    static{
        initMapping();
    }

    private static void initMapping(){
        try {
            File dir=new File(
                    HandleMapping.class.getClassLoader().getResource(".").toURI()
            );
            File controllerDir= new File(dir,"/com/webserver/controller");
            File[] subs=controllerDir.listFiles(f->f.getName().endsWith(".class"));
            for(File sub:subs) {
                String fileName = sub.getName();
                String className = fileName.substring(0, fileName.indexOf("."));
                Class cls = Class.forName("com.webserver.controller." + className);
                if (cls.isAnnotationPresent(Controller.class)) {
                    Method[] methods = cls.getDeclaredMethods();
                    for (Method method : methods) {
                        if (method.isAnnotationPresent(RequestMapping.class)) {
                            RequestMapping rm = method.getAnnotation(RequestMapping.class);
                            String value = rm.value();

                            mapping.put(value, method);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**根据请求路径返回对应的处理方法
     * @param path
     * @return
     * */
    public static Method getMethod(String path){
        return mapping.get(path);
    }

    public static void main(String[] args) {
        Method method=mapping.get("/regUser");
        //通过方法对象可以获取其所属的类的类对象
        Class cls=method.getDeclaringClass();
        System.out.println(cls);
        System.out.println(method);

    }

}
