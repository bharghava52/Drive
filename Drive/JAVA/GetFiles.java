package JAVA;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
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
                    //<div class='content'> <img src='IMAGES/folder.jpg'/> </div>
                    out.println("<tr><td>"+"<a   href='#' class='directory' path='"+folder+"/"+ listOfFiles[i].getName()+"'>" + listOfFiles[i].getName()+"</a></td></tr>");
                } 
            }
            for (int i = 0; i < listOfFiles.length; i++) 
            {
                if (listOfFiles[i].isFile()) 
                    {
                        //String type=findtype(listOfFiles[i]);<div class='content'> </div>
                        out.println("<tr><td>"+"<a  class='file' href=\"DownloadFile?path="+folder+"/"+ listOfFiles[i].getName()+"&file="+listOfFiles[i].getName()+"\" path='"+folder+"/"+ listOfFiles[i].getName()+"' file='"+listOfFiles[i].getName()+"'>" + listOfFiles[i].getName()+"</a></td></tr>");
                    }
            }
            out.close();  
        }catch(Exception e){
            System.out.println(e);
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

    private String findtype(File file) {
        String fileType = "Undetermined"; 
        try {
            fileType = Files.probeContentType(file.toPath());
        } catch (IOException ex) {
            Logger.getLogger(GetFiles.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(fileType.equals(".zip")||fileType.equals(".gz")||fileType.equals(".bz2")||fileType.equals(".xz")||fileType.equals(".rar")||fileType.equals(".tar")||fileType.equals(".tgz")||fileType.equals(".tbz2")||fileType.equals(".z")||fileType.equals(".7z"))
            return "<img src='IMAGES/zip.jpg' />";
        if(fileType.equals(".mp3"))
            return "<img src='IMAGES/mp3.jpg' />";
        if(fileType.equals(".xls")||fileType.equals(".xlsx"))
            return "<img src='IMAGES/excel.jpg' />";
        if(fileType.equals(".png")||fileType.equals(".jpg")||fileType.equals(".jpeg")||fileType.equals(".gif")||fileType.equals(".mpeg"))
            return "<img src='IMAGES/image.jpg' />";
        if(fileType.equals(".pdf"))
            return "<img src='IMAGES/pdf.jpg' />";
        if(fileType.equals(".ppt")||fileType.equals(".pptx"))
            return "<img src='IMAGES/ppt.jpg' />";
        if(fileType.equals(".txt")||fileType.equals(".log"))
            return "<img src='IMAGES/txt.jpg' />";
        if(fileType.equals(".doc")||fileType.equals(".docx"))
            return "<img src='IMAGES/word.jpg' />";
        return fileType;
    }

}
