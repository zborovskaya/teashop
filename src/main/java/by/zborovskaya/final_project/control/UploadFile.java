package by.zborovskaya.final_project.control;

import by.zborovskaya.final_project.service.ProductService;
import by.zborovskaya.final_project.service.ServiceException;
import by.zborovskaya.final_project.service.ServiceFactory;
import by.zborovskaya.final_project.service.XmlWorker;
import org.apache.logging.log4j.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collection;

@WebServlet(urlPatterns = "/UploadXml")
@MultipartConfig(fileSizeThreshold=1024*1024*2, // 2MB
        maxFileSize=1024*1024*10,      // 10MB
        maxRequestSize=1024*1024*50)   // 50MB
public class UploadFile extends HttpServlet {
    private final ProductService productService = ServiceFactory.getInstance().getProductService();
    private static final String ABSOLUTE_PATH = "F:/uploadProducts/";
    private static final String PICTURE_PATH = "picture_path";
    private static final long serialVersionUID = 205242440643911308L;

    /**
     * Directory where uploaded files will be saved, its relative to
     * the web application directory.
     */
    private static final String UPLOAD_DIR = "uploads";

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException, IOException {
        // gets absolute path of the web application
//        String appPath = request.getServletContext().getRealPath("");
        // constructs path of the directory to save uploaded file
//        String savePath = appPath + File.separator +UPLOAD_DIR;
        //Get File name
        String preFileName = "";
        for(Part part : request.getParts()){
            //out.println("PN: "+ part.getName());
            Collection<String> headers = part.getHeaders("content-disposition");
            if (headers == null)
                continue;
            for(String header : headers) {
                if(header.contains("filename=")) {
                    preFileName = header;
                }
                //out.println("CDH: " + header);//CDH: form-data; name="file"; filename="HERE.xml"
            }
        }
        String[] splitBySpacePreFileName = preFileName.split(" ");
        String fileNameWithData = splitBySpacePreFileName[splitBySpacePreFileName.length - 1];
        String tempStr = fileNameWithData.substring(fileNameWithData.indexOf('"')+1);
        String finalFileName=tempStr.substring(0,tempStr.indexOf('"'));
        // creates the save directory if it does not exists
        File fileSaveDir = new File(ABSOLUTE_PATH);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }
        String pathXml =null;
        for (Part part : request.getParts()) {
            String fileName = extractFileName(part);
            // refines the fileName in case it is an absolute path
            fileName = new File(fileName).getName();
            if (!fileName.isEmpty()) {
                pathXml = ABSOLUTE_PATH + fileName;
                part.write(pathXml);
            }
        }
        String error ="";
        try {
            productService.create(XmlWorker.deserializeFromXML(pathXml));
        }catch (Exception e){
            error="?error=incorrect xml";
        }
        response.sendRedirect(request.getContextPath() + "/main"+error);
    }

    /**
     * Extracts file name from HTTP header content-disposition
     */
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length()-1);
            }
        }
        return "";
    }
}
