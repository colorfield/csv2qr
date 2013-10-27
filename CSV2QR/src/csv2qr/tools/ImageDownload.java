package csv2qr.tools;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Download, name and store an image
 * @author r-daneelolivaw
 */
public class ImageDownload {
  
   /**
    * 
    * @param URLstr
    * @param localPath, with trailing slash
    * @param localName, with extension
    * @return 
    */
   public static boolean downloadAndRenameImageFromURL(String URLstr, String localPath, String localName){
      
      boolean isImageCreated = true;
      
      URL url = null;
      try {
        url = new URL(URLstr);
      } catch (MalformedURLException ex) {
        Logger.getLogger(ImageDownload.class.getName()).log(Level.SEVERE, null, ex);
        isImageCreated = false;
      }
      
      // ---------
      
      InputStream in = null;
      try {
        in = new BufferedInputStream(url.openStream());
      } catch (IOException ex) {
        Logger.getLogger(ImageDownload.class.getName()).log(Level.SEVERE, null, ex);
        isImageCreated = false;
      }
      
      
      // ---------
      
      
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      byte[] buf = new byte[1024];
      int n = 0;
      
      try {
        while (-1!=(n=in.read(buf)))
        {
           out.write(buf, 0, n);
        }
        
        out.close();
        in.close();
      } catch (IOException ex) {
        Logger.getLogger(ImageDownload.class.getName()).log(Level.SEVERE, null, ex);
        isImageCreated = false;
      }
      
      byte[] response = out.toByteArray();
      
      // ---------
      
      FileOutputStream fos = null;
      try {
        fos = new FileOutputStream(localPath + localName);
      } catch (FileNotFoundException ex) {
        Logger.getLogger(ImageDownload.class.getName()).log(Level.SEVERE, null, ex);
        isImageCreated = false;
      }
      
      try {      
        fos.write(response);
        fos.close();
      } catch (IOException ex) {
        Logger.getLogger(ImageDownload.class.getName()).log(Level.SEVERE, null, ex);
        isImageCreated = false;
      } 
      
      
      return isImageCreated;
      
    }
  
}
