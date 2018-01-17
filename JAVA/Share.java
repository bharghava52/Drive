package JAVA;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

@WebServlet(name = "Share", urlPatterns = {"/Share"})
public class Share extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out= response.getWriter();
        ResultSet rs1=null;
        try {
            DBOperations dbo=new DBOperations();
            String Id1=request.getParameter("path").split("/")[0];
            String Id=request.getParameter("mail");
            if(Id.equals(Id1)){
                out.print("cannot share to the owner");
            }else{
                String path=request.getParameter("path");
                ResultSet rs=dbo.Select("users from mydata where path='"+path+"'");
                if(rs.next()){
                    JSONParser parser=new JSONParser();
                    JSONObject jobj =(JSONObject) parser.parse(rs.getString("users"));
                    if(jobj.containsKey(Id) && jobj.get(Id).toString().equals(request.getParameter("type"))){
                     out.print("you have already shared this data with this user");
                    }else{
                        rs=dbo.Select("users,path from mydata where path like '"+path+"%"+"'");
                        while(rs.next()){
                            parser=new JSONParser();
                            jobj =(JSONObject) parser.parse(rs.getString("users"));
                            if(jobj.containsKey(Id)){
                                jobj.replace(Id,request.getParameter("type"));
                                dbo.Update("mydata set users='"+jobj.toString()+"' where path='"+rs.getString("path")+"'");
                            }else{
                                jobj.put(Id,request.getParameter("type"));
                                dbo.Update("mydata set users='"+jobj.toString()+"' where path='"+rs.getString("path")+"'");
                            }
                        }
                        dbo.Delete("from share where mail='"+Id+"' and path='"+path+"'");
                        if(dbo.Insert("share set mail='"+Id+"',path='"+path+"',mode='"+request.getParameter("type")+"'")==1){
                            out.print("files/Folders has been shared");
                            dbo.Delete("from share where mail='"+Id+"' and path like '"+path+"/%"+"'");
                        }    
                    }
                }    
            }
        }catch(Exception e){
            out.printf("error:"+e);
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
