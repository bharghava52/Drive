package Users;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "GetFiles", urlPatterns = {"/GetFiles"})
public class GetFiles extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try{ 
            response.setContentType("text/html");  
            PrintWriter out = response.getWriter();  
            Cookie ck[]=request.getCookies();    
            File folder = new File(ck[0].getValue());
            File[] listOfFiles = folder.listFiles();
            for (int i = 0; i < listOfFiles.length; i++) 
            {
                if(listOfFiles[i].isDirectory()) 
                {
                    out.println("<tr><td>"+"<a href='#' class='directory' path='"+folder+"/"+ listOfFiles[i].getName()+"'>" + listOfFiles[i].getName()+"</a></td></tr>");
                }else if (listOfFiles[i].isFile()) 
                {
                    out.println("<tr><td>"+"<a href=\"DownloadFile?path="+folder+"/"+ listOfFiles[i].getName()+"&file="+listOfFiles[i].getName()+"\">" + listOfFiles[i].getName()+"</a></td></tr>");
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

}