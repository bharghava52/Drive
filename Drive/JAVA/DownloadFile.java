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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
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
            }else
            {
                int checkfriend=0;
                ConnectionPooling cp = null;
                try {
                    cp = ConnectionPooling.getInstance("jdbc:mysql://localhost:3306/Drive?autoReconnect=true&useSSL=false","root","root");
                } catch (ClassNotFoundException ex) {
                   out.println("<font color=red>class not found error</font>");
                } catch (SQLException ex) {
                    out.println("<font color=red>sqlException</font>");
                }
                Connection con = null;
                try {
                    con = cp.getConnection();
                } catch (SQLException ex) {
                   out.println("<font color=red>unable to create connection with database</font>");
                }
                PreparedStatement prepStmt = con.prepareStatement("select sharedby from share where mail=?");
                prepStmt.setString(1,ck.getAttribute("user").toString());
                ResultSet rs = prepStmt.executeQuery();
                if(rs.next()){
                    JSONParser parser=new JSONParser();
                    JSONObject jobj = null;
                    try{
                    jobj =(JSONObject) parser.parse(rs.getString("sharedby"));
                    }catch(Exception e){
                        out.println("<font color=red>Please login to Download6:"+e+"</font>");
                    }
                    JSONArray jary= (JSONArray) jobj.get(path.split("/")[0]);
                    for(Object obj : jary){
                        JSONObject jobj1=(JSONObject) obj;
                        for(Object key : jobj1.keySet()){
                            String keyStr = (String)key;
                            if(checkfriend==0){
                                if(path.contains(keyStr)){
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
                            }
                        }    
                    }
                }    
                if(checkfriend==0){
                    out.println("<font color=red>Please login to Download</font>");
                    rd.include(request, response);
                }
            }
        }catch(Exception e){
            out.println("<font color=red>Please login to Download</font>");
            rd.include(request, response);
        }    
        out.close();  
    }

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

}
