package JAVA;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.oreilly.servlet.MultipartRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


@WebServlet(name = "Upload", urlPatterns = {"/Upload"})
public class Upload extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();	
        MultipartRequest m = null;
        try{
            m = new MultipartRequest(request,"temp");
        }catch(Exception e){
         out.print("error:"+e);
        }
        Enumeration files = m.getFileNames();
        while (files.hasMoreElements()) 
        { 
            ConnectionPooling cp=null;
            Connection con=null;
            try{
            String name = (String)files.nextElement(); 
            String filename = m.getFilesystemName(name); 
            Path temp = Files.move(Paths.get("temp\\"+filename),Paths.get(m.getParameter("path")+"\\"+filename));
            if(temp!=null){
                cp = ConnectionPooling.getInstance("jdbc:mysql://localhost:3306/Drive?autoReconnect=true&useSSL=false", "root","root");
                con=cp.getConnection();
                PreparedStatement prepStmt = con.prepareStatement("select data from mydata where mail=?");
                prepStmt.setString(1,m.getParameter("path").split("/")[0]);
                ResultSet rs=prepStmt.executeQuery();
                if(rs.next()){
                    JSONParser parser=new JSONParser();
                    JSONObject jobj=(JSONObject) parser.parse(rs.getString("data"));
                    JSONObject jobj1=(JSONObject) parser.parse("{}");
                    if(jobj.get(m.getParameter("path"))==null)
                        jobj.put(m.getParameter("path")+"/"+filename,jobj1);
                    else
                        jobj.put(m.getParameter("path")+"/"+filename,jobj.get(m.getParameter("path")));
                    prepStmt = con.prepareStatement("update mydata set data=? where mail=?");
                    prepStmt.setString(1,jobj.toString());
                    prepStmt.setString(2,m.getParameter("path").split("/")[0]);
                    int status=prepStmt.executeUpdate();
                    if(status==1){
                        out.print("<script type=\"text/javascript\">alert(\"successfully uploaded\");</script>");
                    }else
                        out.print("<script type=\"text/javascript\">alert(\"unable to update to database\");</script>");
                }
                
            }else{
                out.print("<script type=\"text/javascript\">alert(\"unable to upload\");</script>");
            } 
            }catch(Exception e){
               out.print("<script type=\"text/javascript\">alert(\"error:"+e+"\");</script>"); 
            }finally{
             cp.free(con);
            }  
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
