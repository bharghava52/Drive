package JAVA;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
        DBOperations dbo=new DBOperations();
        PrintWriter out= response.getWriter();
        try{
            int Status=dbo.Insert("users set fname='"+fname+"',lname='"+lname+"',mail='"+mail+"',pass='"+pass+"'");
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.html");
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
         out.close();
        }    
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
