package by.zborovskaya.final_project.util.mail;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * The type Mail class.
 */
public class Mail {
    private static final Logger logger = LogManager.getLogger();
    private static final String FILE_NAME = "config/mail.properties";

    /**
     * Create mail.
     *
     * @param mailTo  the mail to
     * @param subject the subject
     * @param body    the body
     */
    public static void createMail(String mailTo, String subject, String body){
        Properties properties = new Properties();
        try{
            properties.load(new FileReader(getPath()));
        }catch (IOException e) {
            logger.log(Level.ERROR,"Exception while reading " + FILE_NAME + " "+ e);
            throw new RuntimeException("Exception while reading " + FILE_NAME, e);
        }
        MailSender sender = new MailSender(mailTo, subject, body, properties);
        sender.send();
    }
    private static String getPath(){
        String path;
        ClassLoader loader = Mail.class.getClassLoader();
        URL resource = loader.getResource(FILE_NAME);
        if(resource != null) {
            path = resource.getFile();
        }else{
            logger.log(Level.ERROR,"Resource is null! " + FILE_NAME);
            throw new IllegalArgumentException("Resource is null!");
        }
        return path;
    }
}
