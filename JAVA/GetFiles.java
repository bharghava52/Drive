package JAVA;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(name = "GetFiles", urlPatterns = {"/GetFiles"})
public class GetFiles extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{ 
            response.setContentType("text/html");  
            PrintWriter out = response.getWriter();  
            HttpSession ck=request.getSession();
            File folder = new File(ck.getAttribute("user").toString());
            File[] listOfFiles = folder.listFiles();
            out.println("<caption><input type='hidden' id='path' value='"+folder+"'></input></caption>");
            for (int i = 0; i < listOfFiles.length; i++) 
            {
                if(listOfFiles[i].isDirectory()) 
                {
                    out.println("<tr><td>"+"<div class='content'><a   href='#' class='directory' path='"+folder+"/"+ listOfFiles[i].getName()+"'><img src='IMAGES/folder.jpg'/>" + listOfFiles[i].getName()+"</a></div></td></tr>");
                } 
            }
            for (int i = 0; i < listOfFiles.length; i++) 
            {
                if (listOfFiles[i].isFile()) 
                    {
                        String type=findtype(listOfFiles[i]); 
                        out.println("<tr><td>"+"<div class='content'><a  class='file' href='#' path='"+folder+"/"+ listOfFiles[i].getName()+"' file='"+listOfFiles[i].getName()+"' "+type+ listOfFiles[i].getName()+"</a></div></td></tr>");
                    }
            }
            out.close();  
        }catch(Exception e){
            System.out.println(e);
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

    private String findtype(File file) {
        if(file.toString().contains(".zip")||file.toString().contains(".gz")||file.toString().contains(".bz2")||file.toString().contains(".xz")||file.toString().contains(".rar")||file.toString().contains(".tar")||file.toString().contains(".tgz")||file.toString().contains(".tbz2")||file.toString().contains(".z")||file.toString().contains(".7z"))
            return "><img src='IMAGES/zip.jpg' />";
        if(file.toString().contains(".mp3"))
            return "><img src='IMAGES/mp3.jpg' />";
        if(file.toString().contains(".xls")||file.toString().contains(".xlsx"))
            return "><img src='IMAGES/excel.jpg' />";
        if(file.toString().contains(".png")||file.toString().contains(".jpg")||file.toString().contains(".jpeg")||file.toString().contains(".gif")||file.toString().contains(".mpeg"))
            return "><img src='IMAGES/image.jpg' />";
        if(file.toString().contains(".pdf"))
            return "><img src='IMAGES/pdf.jpg' />";
        if(file.toString().contains(".ppt")||file.toString().contains(".pptx"))
            return "><img src='IMAGES/ppt.jpg' />";
        if(file.toString().contains(".txt")||file.toString().contains(".log"))
            return "><img src='IMAGES/txt.jpg' />";
        if(file.toString().contains(".doc")||file.toString().contains(".docx"))
            return "><img src='IMAGES/word.jpg' />";
        if(file.toString().contains(".html"))
            return "><img src='IMAGES/html.jpg' />";
        if(file.toString().contains(".mp4"))
            return "onclick='videobox_open()'><img src='IMAGES/video.jpg'/>";
        return "><img src='IMAGES/txt.jpg' />";
    }

}
