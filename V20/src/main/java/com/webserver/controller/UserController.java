package com.webserver.controller;

import com.webserver.annotations.Controller;
import com.webserver.annotations.RequestMapping;
import com.webserver.core.ClientHandler;
import com.webserver.entity.User;
import com.webserver.http.HttpServletRequest;
import com.webserver.http.HttpServletResponse;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**处理和用户相关的业务类*/


@Controller
public class UserController {

    private static File userDir;

    static {
        userDir = new File("./users");
        if(!userDir.exists()){
            userDir.mkdirs();
        }

    }



    @RequestMapping("/userList")
    public void userList(HttpServletRequest request,HttpServletResponse response){
       //读回来，反序列化，拼页面
        System.out.println("开始处理动态页面！！！");
        List<User> userList=new ArrayList<>();//一个存储User对象的列表
        File[] subs=userDir.listFiles(f->f.getName().endsWith("obj"));
        for(File file:subs){
            try (
                    FileInputStream fis=new FileInputStream(file);
                    ObjectInputStream ois=new ObjectInputStream(fis);
            ){
                User user=(User)ois.readObject();
                userList.add(user);
                 } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        System.out.println(userList);

        try {
            PrintWriter pw=new PrintWriter("./userList.html");

         /**注册页面乱码解决办法
            PrintWriter pw=new PrintWriter(
                    new BufferedWriter(
                            new OutputStreamWriter(
                                    new FileOutputStream("./userList.html"),
                                    StandardCharsets.UTF_8
                            )
                    )

                    );
          */

            pw.println("<!DOCTYPE html>");
            pw.println("<html lang=\"en\">");
            pw.println("<head>");
            pw.println("<meta charset=\"UTF-8\">");
            pw.println(" <title>我的首页</title>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println(" <center>");
            pw.println("<h1>用户列表</h1>");
            pw.println("<table border=\"3\">");

            pw.println("<tr>");
            pw.println(" <td>用户名</td>");
            pw.println(" <td>密码</td>");
            pw.println(" <td>昵称</td>");
            pw.println(" <td>年龄</td>");
            pw.println(" <td>操作</td>");
            pw.println("</tr>");
            /*
            * <form action=,method=>
            <input type name=>
            * </form>
            * 用超链接*/


            for(User user:userList){
                pw.println("<tr>");
                pw.println(" <td>"+user.getUsername()+"</td>");
                pw.println(" <td>"+user.getPassword()+"</td>");
                pw.println(" <td>"+user.getNickname()+"</td>");
                pw.println(" <td>"+user.getAge()+"</td>");
                pw.println(" <td><a href='/deleteUser?username="+user.getUsername()+"'>删除</a></td>");
                pw.println("</tr>");
            }

            pw.println("</table>");
            pw.println("</center>");
            pw.println("</body>");
            pw.println("</html>");

            pw.close();

            response.setContentFile(new File("./userList.html"));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/loginUser")
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

    @RequestMapping("/regUser")
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
