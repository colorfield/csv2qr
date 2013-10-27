package csv2qr.tools;

/**
 * Download a QR from Google Chart
 * Google Chart API
 * https://developers.google.com/chart/infographics/docs/qr_codes
 * @author r-daneelolivaw
 */
public class GoogleChartImageDownload extends ImageDownload {
  
  private static String GOOGLE_CHART_API = "https://chart.googleapis.com/chart?chs=500x500&choe=UTF-8&cht=qr&chl=";
  
  public static boolean downloadQR(String URLstr, String localPath, String localName){
    
    boolean isQRDownloaded = false;
    
    if(downloadAndRenameImageFromURL(GOOGLE_CHART_API + URLstr, localPath, localName)) {
      isQRDownloaded = true;
    }
    
    return isQRDownloaded;
    
  }
  
}
