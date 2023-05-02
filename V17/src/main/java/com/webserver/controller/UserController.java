package com.webserver.controller;

import com.webserver.core.ClientHandler;
import com.webserver.entity.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;
import java.net.URISyntaxException;

/**处理和用户相关的业务类*/


public class UserController {

    private static File userDir;

    static {
        userDir = new File("./users");
        if(!userDir.exists()){
            userDir.mkdirs();
        }

    }

    public void login(HttpServletRequest request,HttpServletResponse response){
        System.out.println("开始处理登陆！！！！！");
        //1
        String username=request.getParameters("username");
        String password=request.getParameters("password");

        if(username==null||username.isEmpty()||password==null||password.isEmpty()){
            response.sendRedirect("/login_info_error.html");
            return;
        }

        //2处理登陆,根据登陆用户的用户名去users目录下寻找该用户信息
        File file= new File(userDir,username+".obj") ;
        if(file.exists()){
            try(
                    FileInputStream fis=new FileInputStream(file);
                 ObjectInputStream ois=new ObjectInputStream(fis);
                 ) {
                User user=(User) ois.readObject();
                //比较密码是否一致
                if(user.getPassword().equals(password)){
                    //登陆成功
                    response.sendRedirect("/login_success.html");
                    return;
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        //登陆失败
         response.sendRedirect("/login_fail.html");



    }

    public void reg(HttpServletRequest request, HttpServletResponse response){
        //1获取表单信息
        String username= request.getParameters("username");
        String password= request.getParameters("password");
        String nickname= request.getParameters("nickname");
        String ageStr= request.getParameters("age");
        System.out.println(username+","+password+","+nickname+","+ageStr);

        if(username==null||username.isEmpty()||password==null||password.isEmpty()||
        nickname==null||nickname.isEmpty()||ageStr==null||ageStr.isEmpty()||
        !ageStr.matches("[0-9]+")){
            response.sendRedirect("/reg_info_error.html");
            return;
        }
        int age= Integer.parseInt(ageStr);
        User user=new User(username,password,nickname,age);


        File file= new File(userDir,username+".obj");
        if(file.exists()){
            response.sendRedirect("/have_user.html");
            return;
        }

        try(FileOutputStream fos=new FileOutputStream(file);
            ObjectOutputStream oos= new ObjectOutputStream(fos);) {
            oos.writeObject(user);

        } catch (IOException e) {
            e.printStackTrace();
        }
        response.sendRedirect("/reg_success.html");

    }
}
