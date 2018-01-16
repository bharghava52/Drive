package JAVA;


import java.io.FileInputStream;
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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebServlet(name = "DownloadFile", urlPatterns = {"/DownloadFile"})
public class DownloadFile extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ParseException {
        response.setContentType("text/html"); 
        RequestDispatcher rd = getServletContext().getRequestDispatcher("/index.html");
        PrintWriter out = response.getWriter();
        HttpSession ck=request.getSession();
        String path=request.getParameter("path");  
        try{
            if(path.toLowerCase().contains(ck.getAttribute("user").toString().toLowerCase())){
                response.setContentType("APPLICATION/OCTET-STREAM");   
                response.setHeader("Content-Disposition","attachment; filename=\""+request.getParameter("file")+"\"");   
                FileInputStream fileInputStream = new FileInputStream(path);  
                int i;   
                while ((i=fileInputStream.read()) != -1){  
                    out.write(i);   
                }   
                fileInputStream.close(); 
            }
            else{
                int checkfriend=0;
                ConnectionPooling cp = null;
                Connection con = null; 
                try {
                    cp = ConnectionPooling.getInstance("jdbc:mysql://localhost:3306/Drive?autoReconnect=true&useSSL=false","root","root");              
                    con = cp.getConnection();
                    PreparedStatement prepStmt = con.prepareStatement("select users from mydata where path=?");
                    prepStmt.setString(1,request.getParameter("path"));
                    ResultSet rs = prepStmt.executeQuery();
                    if(rs.next()){
                        JSONParser parser=new JSONParser();
                        JSONObject jobj = (JSONObject) parser.parse(rs.getString("users"));
                        if(jobj.containsKey(request.getSession().getAttribute("user"))){
                            response.setContentType("APPLICATION/OCTET-STREAM");   
                            response.setHeader("Content-Disposition","attachment; filename=\""+request.getParameter("file")+"\"");   
                            FileInputStream fileInputStream = new FileInputStream(path);  
                            int i;   
                            while ((i=fileInputStream.read()) != -1){  
                                out.write(i);   
                            }   
                            fileInputStream.close();
                            checkfriend=1;
                        }
                    }else{
                        if(path.contains(".zip")){
                            response.setContentType("APPLICATION/OCTET-STREAM");   
                            response.setHeader("Content-Disposition","attachment; filename=\""+request.getParameter("file")+"\"");   
                            FileInputStream fileInputStream = new FileInputStream(path);  
                            int i;   
                            while ((i=fileInputStream.read()) != -1){  
                                out.write(i);   
                            }   
                            fileInputStream.close(); 
                        }
                    }    
                    if(checkfriend==0){
                        out.println("<script>alert(\"you don't have access to download this file\");</script>");
                    }
                }catch(Exception e){
                    out.println("<script>alert(\"error:"+e+"\");</script>");
                }finally{
                    cp.free(con);
                    rd.include(request, response);
                }
            }    
        out.close();
        }catch(Exception e){
            out.println("<script>alert(\"error:"+e+"\");</script>");
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
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(DownloadFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(DownloadFile.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DownloadFile.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(DownloadFile.class.getName()).log(Level.SEVERE, null, ex);
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
