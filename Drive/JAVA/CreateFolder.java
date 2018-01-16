package JAVA;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet(name = "CreateFolder", urlPatterns = {"/CreateFolder"})
public class CreateFolder extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ParseException {
        response.setContentType("text/html");  
        PrintWriter out = response.getWriter();
        String s=request.getParameter("path")+"/"+request.getParameter("name");
        File theDir = new File(s);
            if (!theDir.exists()) {
                ConnectionPooling cp=null;
                Connection con=null;
                try {
                    cp = ConnectionPooling.getInstance("jdbc:mysql://localhost:3306/Drive?autoReconnect=true&useSSL=false", "root","root");
                    con=cp.getConnection();
                    PreparedStatement prepStmt = con.prepareStatement("Select users from mydata where path=?");
                    prepStmt.setString(1,request.getParameter("path"));
                    ResultSet rs=prepStmt.executeQuery();
                    JSONParser parser=new JSONParser();
                    JSONObject jobj;
                    if(rs.next()){
                     jobj=(JSONObject) parser.parse(rs.getString("users"));
                    }else{
                     jobj=(JSONObject) parser.parse("{}");
                    }
                    prepStmt = con.prepareStatement("insert into mydata set mail=?,path=?,users=?");
                    prepStmt.setString(1,request.getParameter("path").split("/")[0]);
                    prepStmt.setString(2,s);
                    prepStmt.setString(3,jobj.toString());
                    int Status=prepStmt.executeUpdate();
                    if(Status==1){
                        theDir.mkdir();
                        out.println("folder created");
                    }else{
                     out.print("unable to create folder");
                    }
                }catch(Exception e){
                    out.println("error:"+e);
                }finally{
                    cp.free(con);
                }
            }
            else{
                out.println("this folder already exists:");
            }
        out.close();
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(CreateFolder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CreateFolder.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(CreateFolder.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(CreateFolder.class.getName()).log(Level.SEVERE, null, ex);
        }
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
