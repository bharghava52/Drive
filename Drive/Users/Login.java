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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        try {
            cp = ConnectionPooling.getInstance("com.mysql.jdbc.Driver","jdbc:mysql://localhost:3306/Drive","root","root");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        String Id=request.getParameter("name");
        String Pass=request.getParameter("pass");
        Connection con=null;
        ResultSet rs;
        try {
            con=cp.getConnection();
            PreparedStatement prepStmt = con.prepareStatement("select fname,mail,pass from users where mail=?");
            prepStmt.setString(1,Id);
            rs = prepStmt.executeQuery();
            System.out.println(rs.toString());
            if(rs.next()){
             if(Pass.equals(rs.getString("pass"))){
                Cookie loginCookie = new Cookie("user",rs.getString("mail"));
                //Cookie path=new Cookie("path",rs.getString("mail"));
                loginCookie.setMaxAge(24*60*60);
                response.addCookie(loginCookie);
                //response.addCookie(path);
                processRequest(request,response);
             }else{
                RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.html");
                PrintWriter out= response.getWriter();
                out.println("<font color=red>Either user name or password is wrong.</font>");
                rd.include(request, response);
             }
            }
        }catch(Exception e){
        }finally{
          cp.free(con);
        }    
    }
}
