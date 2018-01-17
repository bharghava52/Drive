package JAVA;

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
import org.json.simple.JSONObject;

@WebServlet(name = "SharedFiles", urlPatterns = {"/SharedFiles"})
public class SharedFiles extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out= response.getWriter();
        DBOperations dbo=new DBOperations();
        try {
            String Id=request.getSession().getAttribute("user").toString();
            ResultSet rs =dbo.Select("* from share where mail='"+Id+"'");
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
          out.close();
        }    
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
