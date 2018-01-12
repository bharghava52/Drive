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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession ck=request.getSession();
        response.setContentType("text/html"); 
        PrintWriter out = response.getWriter();
        String path=request.getParameter("path");
        if(path.toLowerCase().contains(ck.getAttribute("user").toString().toLowerCase())){
            File file = new File(path);
            ConnectionPooling cp=null;
            Connection con=null;
            try {
                cp = ConnectionPooling.getInstance("jdbc:mysql://localhost:3306/Drive?autoReconnect=true&useSSL=false", "root","root");
                con=cp.getConnection();
                PreparedStatement prepStmt = con.prepareStatement("select data from mydata where mail=?");
                prepStmt.setString(1,path.split("/")[0]);
                ResultSet rs=prepStmt.executeQuery();
                if(rs.next()){
                    JSONParser parser=new JSONParser();
                    JSONObject jobj=(JSONObject) parser.parse(rs.getString("data"));
                    jobj.remove(path);
                    prepStmt = con.prepareStatement("update mydata set data=? where mail=?");
                    prepStmt.setString(1,jobj.toString());
                    prepStmt.setString(2,path.split("/")[0]);
                    int status=prepStmt.executeUpdate();
                    if(status==1){
                        if(file.delete())
                            out.print("deleted successfully");
                        else
                            out.print("unable to delete");
                    }else
                        out.print("unable to delete record from db");
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
