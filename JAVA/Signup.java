package JAVA;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Signup", urlPatterns = {"/signup"})
public class Signup extends HttpServlet {
    ConnectionPooling cp;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html"); 
        String fname=request.getParameter("fname");
        String lname=request.getParameter("lname");
        String mail=request.getParameter("mail");
        String pass=request.getParameter("pass");
        Connection con=null;
        ResultSet rs;
        try{
            cp = ConnectionPooling.getInstance("jdbc:mysql://localhost:3306/Drive?autoReconnect=true&useSSL=false", "root","root");
            con=cp.getConnection();
            PreparedStatement prepStmt = con.prepareStatement("insert into users set fname=?,lname=?,mail=?,pass=?");
            prepStmt.setString(1,fname);
            prepStmt.setString(2,lname);
            prepStmt.setString(3,mail);
            prepStmt.setString(4,pass);
            int Status=prepStmt.executeUpdate();
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.html");
            PrintWriter out= response.getWriter();
            if(Status==1){
                File theDir = new File(mail);
                if (!theDir.exists()) {
                    theDir.mkdir();     
                }
                else{
                    out.println("<h2 color='red'>Drive is not ready </h2>");
                }
                out.println("<h2 color='green' class='loginBox'>Login has been created you can login now</h2>");
                rd.include(request, response);
            }else{
              RequestDispatcher view = request.getRequestDispatcher("Unable to create Login");
              view.forward(request, response);
            }
        }catch(Exception e){
        }finally{
         cp.free(con);
        }    
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
