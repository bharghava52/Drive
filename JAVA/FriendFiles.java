package JAVA;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@WebServlet(name = "FriendFiles", urlPatterns = {"/FriendFiles"})
public class FriendFiles extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = null;
        DBOperations dbo=new DBOperations();
        try {
            String Id=request.getSession().getAttribute("user").toString();
            out= response.getWriter();      
            ResultSet rs= dbo.Select("select * from share where mail='"+Id+"'? and path like '"+request.getParameter("friend").toString()+"%"+"'");
            out.println("<tr><td><a href='#' class='sharedwithme'><div class='content'><img src='IMAGES/back.jpg'/></div></a></td></tr>");
            while(rs.next()){
                String path=rs.getString("path");
                String path1=path.split("/")[path.split("/").length-1];
                if(path1.indexOf('.')==-1)
                    out.println("<tr><td><div class='content'><a href='#' class='frienddata folder"+rs.getString("mode")+"' path='"+path+"'><img src='IMAGES/folder.jpg'/>"+path1+"</a></div><tr><td>");
                else{
                    String type=findtype(path1); 
                    out.println("<tr><td><div class='content'><a  class='friendfile file"+rs.getString("mode")+"' href=\"DownloadFile?path="+path+"&file="+path1+"\" path='"+path+"' file='"+path1+"'>"+type +path1+"</a></div></td></tr>");
                }
            }
        }catch(Exception e){
            out.print("<tr><td><a href='#'>error:"+e+"</td></tr>");
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
   
    private String findtype(String file) {
        if(file.toString().contains(".zip")||file.toString().contains(".gz")||file.toString().contains(".bz2")||file.toString().contains(".xz")||file.toString().contains(".rar")||file.toString().contains(".tar")||file.toString().contains(".tgz")||file.toString().contains(".tbz2")||file.toString().contains(".z")||file.toString().contains(".7z"))
            return "<img src='IMAGES/zip.jpg' />";
        if(file.toString().contains(".mp3"))
            return "<img src='IMAGES/mp3.jpg' />";
        if(file.toString().contains(".xls")||file.toString().contains(".xlsx"))
            return "<img src='IMAGES/excel.jpg' />";
        if(file.toString().contains(".png")||file.toString().contains(".jpg")||file.toString().contains(".jpeg")||file.toString().contains(".gif")||file.toString().contains(".mpeg"))
            return "<img src='IMAGES/image.jpg' />";
        if(file.toString().contains(".pdf"))
            return "<img src='IMAGES/pdf.jpg' />";
        if(file.toString().contains(".ppt")||file.toString().contains(".pptx"))
            return "<img src='IMAGES/ppt.jpg' />";
        if(file.toString().contains(".txt")||file.toString().contains(".log"))
            return "<img src='IMAGES/txt.jpg' />";
        if(file.toString().contains(".doc")||file.toString().contains(".docx"))
            return "<img src='IMAGES/word.jpg' />";
        if(file.toString().contains(".html"))
            return "<img src='IMAGES/html.jpg' />";
        return "<img src='IMAGES/txt.jpg' />";
    }
}
