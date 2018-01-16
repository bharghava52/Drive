package JAVA;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@WebServlet(name = "DeleteFolder", urlPatterns = {"/DeleteFolder"})
public class DeleteFolder extends HttpServlet {
    
    void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                deleteDir(f);
            }
        }
        file.delete();
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession ck=request.getSession();
        response.setContentType("text/html"); 
        PrintWriter out = response.getWriter();
        String path=request.getParameter("path");
        if(path.toLowerCase().contains(ck.getAttribute("user").toString().toLowerCase()) || path.contains(".zip")){
            File file = new File(path);
            deleteDir(file);
            ConnectionPooling cp=null;
            Connection con=null;
            try {
                cp = ConnectionPooling.getInstance("jdbc:mysql://localhost:3306/Drive?autoReconnect=true&useSSL=false", "root","root");
                con=cp.getConnection();
                PreparedStatement prepStmt = con.prepareStatement("delete from mydata where path like ?");
                prepStmt.setString(1,path+"%");
                int Status=prepStmt.executeUpdate();
                if(Status==1 || Status==2){
                    out.print("deleted files/folder");
                }else{
                    if(path.contains(".zip")){
                    }else
                    out.print("files deleted but unable to update DB:"+Status);
                }
            }catch(Exception e){
             out.print("error :"+e);
            }finally{
                cp.free(con);
            }
            out.close();
        }else{
            ConnectionPooling cp=null;
            Connection con=null;
            try {
                cp = ConnectionPooling.getInstance("jdbc:mysql://localhost:3306/Drive?autoReconnect=true&useSSL=false", "root","root");
                con=cp.getConnection();
                PreparedStatement prepStmt = con.prepareStatement("select users from mydata where path=?");
                prepStmt.setString(1,path);
                ResultSet rs=prepStmt.executeQuery();
                if(rs.next()){
                 JSONParser parser=new JSONParser();
                 JSONObject obj=(JSONObject) parser.parse(rs.getString("users"));
                 if(obj.containsKey(ck.getAttribute("user"))){
                  if(obj.get(ck.getAttribute("user")).equals("SHARE")||obj.get(ck.getAttribute("user")).equals("EDIT")){
                    prepStmt = con.prepareStatement("delete from mydata where path like ?");
                    prepStmt.setString(1,path+"%");
                    int Status=prepStmt.executeUpdate();
                    if(Status==1 || Status==2){
                        out.print("deleted files/folder");
                    }else{
                        out.print("files deleted but unable to update DB:"+Status);
                    }
                  }else
                      out.print("you don't have permission to delete this file");
                 }else
                     out.print("you don't have permission to delete this file");
                }
            }catch(Exception e){
             out.print("error :"+e);
            }finally{
                cp.free(con);
            }
            out.close();
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
