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
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet(name = "Share", urlPatterns = {"/Share"})
public class Share extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ConnectionPooling cp = null;
        PrintWriter out= response.getWriter();
        try {
            cp = ConnectionPooling.getInstance("jdbc:mysql://localhost:3306/Drive?autoReconnect=true&useSSL=false","root","root");
        } catch (Exception ex) {
            out.println("<font color=red>connection not pooled</font>"+ex);
        }
        
        Connection con = null;
        try {
            con = cp.getConnection();
        } catch (SQLException ex) {
           out.println("<font color=red>unable to create connection</font>");
        }
        try {
            String Id=request.getParameter("mail");
            String path=request.getParameter("path");
            String path1[]=path.split("/");
            PreparedStatement prepStmt = con.prepareStatement("select * from share where mail=?");
            prepStmt.setString(1,Id);
            ResultSet rs = prepStmt.executeQuery();
            if(rs.next()){
                try{
                    JSONParser parser=new JSONParser();
                    JSONObject jobj =(JSONObject) parser.parse(rs.getString("sharedby"));
                    if(jobj.containsKey(path1[0])){
                        JSONArray jary= (JSONArray) jobj.get(path1[0]);
                        JSONObject jobj1=new JSONObject();
                        jobj1.put(path,"YES");
                        jary.add(jobj1);
                        jobj.put(path1[0], jary);
                    }else{
                        JSONArray jary=new JSONArray();
                        JSONObject jobj1=new JSONObject();
                        jobj1.put(path,"YES");
                        jary.add(jobj1);
                        jobj.put(path1[0],jary);
                    }
                    prepStmt = con.prepareStatement("update share set sharedby=? where mail=?");
                    prepStmt.setString(1,jobj.toString());
                    prepStmt.setString(2,Id);
                    int status=prepStmt.executeUpdate();
                    if(status!=-1){
                        out.print("shared with the user");
                    }
                }catch(Exception e){
                    out.print("Error:"+e); 
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
