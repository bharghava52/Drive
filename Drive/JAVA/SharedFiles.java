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
        try {
            cp = ConnectionPooling.getInstance("jdbc:mysql://localhost:3306/Drive?autoReconnect=true&useSSL=false","root","root");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        String Id=request.getSession().getAttribute("user").toString();
        PrintWriter out= response.getWriter();
        Connection con = null;
        try {
            con = cp.getConnection();
        } catch (SQLException ex) {
           out.println("unable to create connection");
        }
        ResultSet rs;
        try {
            PreparedStatement prepStmt = con.prepareStatement("select sharedby from share where mail=?");
            prepStmt.setString(1,Id);
            rs = prepStmt.executeQuery();
            if(rs.next()){
                out.println("starting the result to object");
                JSONParser parser=new JSONParser();
                JSONObject jobj =(JSONObject) parser.parse(rs.getString("sharedby"));
                out.println("keys set created");
                for(Object key : jobj.keySet()){
                    String keyStr = (String)key;
                    out.println("<tr><td><a href='#' class='friend' user='"+keyStr+"'>shared by:"+keyStr+"</a><tr><td>");
                }
            }
        }catch(Exception e){
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
