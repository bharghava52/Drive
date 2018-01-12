package JAVA;

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
import javax.servlet.http.HttpSession;

@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {
    ConnectionPooling cp;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
              response.setContentType("text/html;charset=UTF-8");
              RequestDispatcher view = request.getRequestDispatcher("Drive.html");
              view.forward(request, response);        
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Connection con = null;
        try {
            cp = ConnectionPooling.getInstance("jdbc:mysql://localhost:3306/Drive?autoReconnect=true&useSSL=false","root","root");
            String Id=request.getParameter("name");
            String Pass=request.getParameter("pass");
            PrintWriter out= response.getWriter();
            con = cp.getConnection();
            ResultSet rs;
            PreparedStatement prepStmt = con.prepareStatement("select fname,mail,pass from users where mail=?");
            prepStmt.setString(1,Id);
            rs = prepStmt.executeQuery();
            System.out.println(rs.toString());
            if(rs.next()){
             if(Pass.equals(rs.getString("pass"))){
                HttpSession session = request.getSession(true);
                session.setAttribute("user",rs.getString("mail"));
                processRequest(request,response);
             }else{
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.html");
                out.println("<font color=red>Either user name or password is wrong.</font>");
                rd.include(request, response);
             }
            }else{
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.html");
                out.println("<font color=red>no such user exist</font>");
                rd.include(request, response);
            }
        }catch(Exception e){
        }finally{
          cp.free(con);
        }    
    }
}
