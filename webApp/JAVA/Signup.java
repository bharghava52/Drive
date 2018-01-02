package Users;

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
        try {
            cp = ConnectionPooling.getInstance("com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306/Drive", "root","root");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        String fname=request.getParameter("fname");
        String lname=request.getParameter("lname");
        String mail=request.getParameter("mail");
        String pass=request.getParameter("pass");
        Connection con=null;
        ResultSet rs;
        try{
            con=cp.getConnection();
            PreparedStatement prepStmt = con.prepareStatement("insert into users set fname=?,lname=?,mail=?,pass=?");
            prepStmt.setString(1,fname);
            prepStmt.setString(2,lname);
            prepStmt.setString(3,mail);
            prepStmt.setString(4,pass);
            int Status=prepStmt.executeUpdate();
            if(Status==1){
              RequestDispatcher view = request.getRequestDispatcher("Login has been created");
              view.forward(request, response);
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
