package csv2qr;

import csv2qr.model.CSV2QR;

/**
 * <h1>Outputs vCard QR codes via a CSV file.</h1>
 * This application was originally built to be used under Adobe InDesign for business cards, so, the CSV file is rewrited with the image path of the QR code.
 * 
 * <h2>TODO</h2>
 * <ul>
 *  <li>Factory to allow QR generator (Google Chart, ZXing) </li>
 *  <li>Factory to allow QR types (vCard, URL, ...)</li>
 *  <li>Put common companies details in another CSV (pseudo relational)</li>
 *  <li></li>
 *  <li>GUI</li>
 * </ul>
 * 
 * <h2>Possible extensions</h2>
 * <ul>
 *  <li>Factory to allow other types of QR codes</li>
 * </ul>
 * 
 * @author r-daneelolivaw
 */
public class Main {

  /**
   * TODO : CSV path and later a GUI on top of it
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    
    
    CSV2QR csv2qr = new CSV2QR();
    csv2qr.csvConvert();
    
  }
}
