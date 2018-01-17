package JAVA;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
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
        DBOperations dbo=new DBOperations();
        if(path.toLowerCase().contains(ck.getAttribute("user").toString().toLowerCase()) || path.contains(".zip")){
            File file = new File(path);
            deleteDir(file);
            try {
                int Status=dbo.Update("mydata where path like '"+path+"%'");
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
            }
            out.close();
        }else{
            try {
                ResultSet rs=dbo.Select("users from mydata where path='"+path+"'");
                if(rs.next()){
                 JSONParser parser=new JSONParser();
                 JSONObject obj=(JSONObject) parser.parse(rs.getString("users"));
                 if(obj.containsKey(ck.getAttribute("user"))){
                  if(obj.get(ck.getAttribute("user")).equals("SHARE")||obj.get(ck.getAttribute("user")).equals("EDIT")){
                    int Status=dbo.Delete("mydata where path like '"+path+"%'");
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
            }
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
