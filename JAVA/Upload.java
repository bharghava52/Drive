package JAVA;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.oreilly.servlet.MultipartRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
            m = new MultipartRequest(request,"temp",2147483646 );
        }catch(Exception e){
         out.print("error:"+e);
        }
        Enumeration files = m.getFileNames();
        while (files.hasMoreElements()) 
        { 
            DBOperations dbo=new DBOperations();
            try{
                String name = (String)files.nextElement(); 
                String filename = m.getFilesystemName(name); 
                Path temp = Files.move(Paths.get("temp\\"+filename),Paths.get(m.getParameter("path")+"\\"+filename));
                if(temp!=null){
                    ResultSet rs=dbo.Select("users from mydata where path='"+m.getParameter("path")+"'");
                    JSONParser parser=new JSONParser();
                    JSONObject jobj;
                    if(rs.next()){
                     jobj=(JSONObject) parser.parse(rs.getString("users"));
                    }else{
                     jobj=(JSONObject) parser.parse("{}");
                    }
                    int Status=dbo.Insert("mydata set mail='"+m.getParameter("path").split("/")[0]+"',path='"+m.getParameter("path")+"/"+filename+"',users='"+jobj.toString()+"'");
                    if(Status==1){
                        out.println("<script type=\"text/javascript\">alert(\"file uploaded\");</script>");
                    }else{
                     out.print("<script type=\"text/javascript\">alert(\"unable to upload\");</script>");
                    }

                }else{
                    out.print("<script type=\"text/javascript\">alert(\"unable to upload\");</script>");
                } 
            }catch(Exception e){
               files = m.getFileNames();
               while (files.hasMoreElements()) 
               {
                String name = (String)files.nextElement(); 
                File f=new File(m.getParameter("path")+"/"+m.getFilesystemName(name));
                f.delete();
               } 
               out.print("<script type=\"text/javascript\">alert(\"error:"+e+"\");</script>"); 
            }finally{
             out.close();
            }  
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
