package JAVA;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
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
                    PreparedStatement prepStmt = con.prepareStatement("select data from mydata where mail=?");
                    prepStmt.setString(1,request.getParameter("path").split("/")[0]);
                    System.out.println("creating directory: " + theDir.getName());
                    ResultSet rs=prepStmt.executeQuery();
                    if(rs.next()){
                        JSONParser parser=new JSONParser();
                        JSONObject jobj=(JSONObject) parser.parse(rs.getString("data"));
                        JSONObject jobj1=(JSONObject) parser.parse("{}");
                        jobj.put(s,jobj1);
                        prepStmt=con.prepareStatement("update mydata set data=? where mail=?");
                        prepStmt.setString(1,jobj.toString());
                        prepStmt.setString(2,request.getParameter("path").split("/")[0]);
                        int Status=prepStmt.executeUpdate();
                        if(Status==1){
                            theDir.mkdir();
                            out.println("folder created");
                        }else{
                         out.print("unable to create folder");
                        }
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
