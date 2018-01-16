package JAVA;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@WebServlet(name = "SharedFiles", urlPatterns = {"/SharedFiles"})
public class SharedFiles extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ConnectionPooling cp = null;
        Connection con = null;
        response.setContentType("text/html");
        PrintWriter out= response.getWriter();
        try {
            cp = ConnectionPooling.getInstance("jdbc:mysql://localhost:3306/Drive?autoReconnect=true&useSSL=false","root","root");
            String Id=request.getSession().getAttribute("user").toString();
            con = cp.getConnection();
            ResultSet rs;
            PreparedStatement prepStmt = con.prepareStatement("select * from share where mail=?");
            prepStmt.setString(1,Id);
            rs = prepStmt.executeQuery();
            JSONObject jobj=new JSONObject();
            if(rs.next()){
                if(!jobj.containsKey(rs.getString("path").split("/")[0]))
                    jobj.put(rs.getString("path").split("/")[0],"View");
            }
            for(Object obj : jobj.keySet()){
             out.print("<tr><td><a href='#' class='friend' user='"+obj.toString()+"'>SharedBy:"+obj.toString()+"</td></tr>");
            }
        }catch(Exception e){
            out.print("<script>alert(\"error:"+e+"\");</script>");
        }finally{
          cp.free(con);
        }    
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
