package JAVA;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String Id=request.getParameter("name");
            String Pass=request.getParameter("pass");
            PrintWriter out= response.getWriter();
            DBOperations dbo=new DBOperations();
            ResultSet rs=dbo.Select("* from users where mail='"+Id+"'");
            try{
                if(rs.next()){
                    if(Pass.equals(rs.getString("pass"))){
                       HttpSession session = request.getSession(true);
                       session.setAttribute("user",rs.getString("mail"));
                       response.setContentType("text/html;charset=UTF-8");
                       RequestDispatcher view = request.getRequestDispatcher("Drive.html");
                       view.forward(request, response);
                    }else{
                       RequestDispatcher rd =request.getRequestDispatcher("index.html");
                       out.println("<font color=red>hi "+rs.getString("fname")+" looks like your password is wrong.</font>");
                       rd.include(request, response);
                    }
                }else{
                    RequestDispatcher rd = request.getRequestDispatcher("index.html");
                    out.println("<font color=red>no such user exist</font>");
                    rd.include(request, response);
                }
            }catch(Exception e){
                out.println("<font color=red>error:"+e+"</font>");
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
