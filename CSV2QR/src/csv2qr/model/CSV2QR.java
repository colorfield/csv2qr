package csv2qr.model;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
// import net.oauth.encoding.impl.PercentEncoding;
import csv2qr.tools.GoogleChartImageDownload;
import csv2qr.tools.Texturize;


/**
 * ReadCSV
 * First working version
 * TODO : Clear MVC separation, Factories, ...
 * @author r-daneelolivaw
 */
public class CSV2QR {
  
  private static String VCARD_VERSION = "2.1"; 
  private static String VCARD_ENCODING = "utf-8";
  private static String VCARD_2_1_ENCODING = "ENCODING=QUOTED-PRINTABLE;CHARSET=UTF-8";
  private static String VCARD_DELIMITER = "%0A"; 
  
  //----------- hard coded values provide GUI or CLI
  private static boolean IS_TEST = false;
  
  // CSV FILE
  public static String CSV_FILE_PATH_ORIG =  "addresses.csv";
  public static String CSV_FILE_PATH_DEST =  "addresses_with_qr.csv";
  public static String CSV_FILE_PATH_ORIG_TEST =  "addresses_test.csv";
  public static String CSV_FILE_PATH_DEST_TEST =  "addresses_test_with_qr.csv";
  public static String QR_DIRECTORY_PATH =  "/Users/christophe/Desktop/CSV2QR/files/";
  
  // Common company details (default)
  private String COM_STREET = "Street name, 123";
  private String COM_ZIP = "1234";
  private String COM_CITY = "Brussels";
  private String COM_COUNTRY = "Belgium";
  private String COM_WEBSITE = "http://www.example.com";
  
  /*
   * 0 Company -> vcard
   * 1 Title -> vcard
   * 2 Firstname -> vcard
   * 3 Lastname -> vcard
   * 4 Function EN
   * 5 Function FR
   * 6 Departement EN
   * 7 Service EN
   * 8 Service FR
   * 9 Phone -> vcard
   * 10 Mobile phone -> vcard
   * 11 Fax
   * 12 Email -> vcard
   * 13 Picture
   */
  
  // TODO : map columns order
  private static int CSV_COMPANY  = 0;
  private static int CSV_COMPANY_ID = -1; // TO BE DEFINED
  private static int CSV_TITLE    = 4;
  private static int CSV_FNAME    = 2;
  private static int CSV_LNAME    = 3;
  private static int CSV_BPHONE   = 9;
  private static int CSV_MPHONE   = 10;
  private static int CSV_EMAIL    = 12;
  
  //-----------
  
  /*
    String firstName = workLine[0];
    String lastName = workLine[0];
    String title = workLine[0];
    String street = workLine[0];
    String zip = workLine[0];
    String city = workLine[0];
    String country = workLine[0];
    String company = workLine[0];
    String emailPrivate = "";
    String emailBusiness = workLine[0];
    String phonePrivate = "";
    String phoneMobile = workLine[0];
    String phoneBusiness = workLine[0];
    String website = workLine[0];
    
   */
    
  
  
  public CSV2QR(){
  }

  
  public void csvConvert(){
      csvToList();
  }
  
  
  /**
   * Read a CSV and put it in a List of String arrays (one array per workLine)
   * @return 
   */
  private List csvToList() {
    
    
    String inputCSV = null;
    String outputCSV = null;
    
    if(IS_TEST){
      inputCSV = CSV_FILE_PATH_ORIG_TEST;
      outputCSV = CSV_FILE_PATH_DEST_TEST;
    }else{
      inputCSV = CSV_FILE_PATH_ORIG;
      outputCSV = CSV_FILE_PATH_DEST;
    }
    

    List csvEntries = null;

    try {
      CSVReader reader = new CSVReader(new FileReader(inputCSV),',');
      try {
        csvEntries = reader.readAll();
      } catch (IOException ex) {
        Logger.getLogger(CSV2QR.class.getName()).log(Level.SEVERE, null, ex);
      }
    } catch (FileNotFoundException ex) {
      Logger.getLogger(CSV2QR.class.getName()).log(Level.SEVERE, null, ex);
    }
    

    if (csvEntries != null) {

      Iterator it = csvEntries.iterator();
      // used to manage duplicate names
      int curIndex = 0;
      List<String> qrFiles = new ArrayList<String>(); 
      
      // open the new CSV
      CSVWriter writer = null;
      try {
        writer = new CSVWriter(new FileWriter(outputCSV), ',');
      } catch (IOException ex) {
        Logger.getLogger(CSV2QR.class.getName()).log(Level.SEVERE, null, ex);
      }
      
      while (it.hasNext()) {
        
        // create list to add the future column
        List<String> workLine = new ArrayList<String>();
        
        // used to handle duplicate file names
        ++curIndex;
        
        // store in a first array the original line
        String[] origLine = (String[]) it.next();
        
        // add workLine components to the array list
        for(int c=0; c < origLine.length; ++c){
          System.out.print(origLine[c] + "\t");
          workLine.add(origLine[c]);
        }
        
        System.out.println("\n");
        
        // filename = first+lastname
        String qrFileName = origLine[CSV_FNAME] + "_" +  origLine[CSV_LNAME];
        String cleanQRFileName = Texturize.removeAccents(qrFileName);
        cleanQRFileName = Texturize.removeSpaces(cleanQRFileName);
        String qrPath = "/qr/" + cleanQRFileName + ".png";
       
        // if already in the list (manage duplicate names)
        if(qrFiles.contains(qrPath)){
          // rebuild filename and increment with workline index
          qrPath = "/qr/" + cleanQRFileName + "_" + curIndex +  ".png";
        }
        
        System.out.println("Filename = " + qrPath);
        
        // append the QR filename to the verification list
        qrFiles.add(qrPath);
        // append the QR filename to the current workLine
        workLine.add(qrPath);
        
        
        // convert into array
        String[] newLine = new String[workLine.size()];
        workLine.toArray(newLine);
        
        // create vCard workLine
        String vCardLine = constructVCardStringFromLine(newLine);
        System.out.println(vCardLine);
        
        // download and write the QR (comment for tests...)
        GoogleChartImageDownload.downloadQR(vCardLine, QR_DIRECTORY_PATH, qrPath);
        
        // write each entry on the new CSV
        writer.writeNext(newLine);
        
        // DEBUG
        /*
        for( int i = 0; i < workLine.length - 1; i++){
          String element = workLine[i];
          System.out.print("\t" + element);
        }
        System.out.println("");
        */
        
      }
      
      // close the CSV
      try {
        writer.close();
      } catch (IOException ex) {
        Logger.getLogger(CSV2QR.class.getName()).log(Level.SEVERE, null, ex);
      }

    }
    
    return csvEntries;

  }
  
  
  /**
   * Convert a CSV workLine into vCard format
   * @param workLine
   * @return 
   */
  public String constructVCardStringFromLine(String [] workLine) {
    
    StringBuilder vCardSB = new StringBuilder();
    String testStr = null;
    
    
    
    // Java 6
    switch (Integer.parseInt(workLine[CSV_COMPANY_ID])){
    // Java 7 only
    //switch (workLine[CSV_COMPANY]) {
      case 0: 
      //case "Company A":
        COM_STREET = "Street name, 123";
        COM_ZIP = "1234";
        COM_CITY = "Brussels";
        COM_COUNTRY = "Belgium";
        COM_WEBSITE = "http://www.example.com";
        break;
      case 1: 
      //case "Company B":
        COM_STREET = "Street name, 123";
        COM_ZIP = "1234";
        COM_CITY = "Brussels";
        COM_COUNTRY = "Belgium";
        COM_WEBSITE = "http://www.example.com";
        break;
    }
    
    try {
      // vCard values
      String firstName = workLine[CSV_FNAME];
      String lastName = workLine[CSV_LNAME];
      String title = workLine[CSV_TITLE];
      String street = COM_STREET;
      String zip = COM_ZIP;
      String city = COM_CITY;
      String country = COM_COUNTRY;
      String company = workLine[CSV_COMPANY];
      String emailPrivate = ""; // left empty
      String emailBusiness = workLine[CSV_EMAIL];
      String phonePrivate = ""; // left empty
      String phoneMobile = workLine[CSV_MPHONE];
      String phoneBusiness = workLine[CSV_BPHONE];
      String website = COM_WEBSITE;
      String picture = "http://www.example.com/picture.jpg";
      
      /*
      System.out.println("Title = " + title);
      System.out.println("Phone = " + phoneBusiness);
      System.out.println("Mobile = " + phoneMobile);
      */
      
      // TODO : logging for possible missing data
      
      // String encode = "N;ENCODING=QUOTED-PRINTABLE;CHARSET=UTF-8:Testé";
      
      vCardSB.append(URLEncoder.encode("BEGIN:VCARD",VCARD_ENCODING));
        vCardSB.append(VCARD_DELIMITER);
      vCardSB.append(URLEncoder.encode("VERSION:",VCARD_ENCODING));
      vCardSB.append(URLEncoder.encode(VCARD_VERSION,VCARD_ENCODING));
        vCardSB.append(VCARD_DELIMITER);
      //vCardSB.append(URLEncoder.encode(firstName + " " + lastName,VCARD_ENCODING));
        //vCardSB.append(VCARD_DELIMITER);
      vCardSB.append(URLEncoder.encode("N;" + VCARD_2_1_ENCODING + ":"+ lastName +";" + firstName,VCARD_ENCODING));
        vCardSB.append(VCARD_DELIMITER);
      vCardSB.append(URLEncoder.encode("ORG;" + VCARD_2_1_ENCODING + ":" + company,VCARD_ENCODING));
        vCardSB.append(VCARD_DELIMITER);
      
      vCardSB.append(URLEncoder.encode("TITLE;" + VCARD_2_1_ENCODING + ":" + title,VCARD_ENCODING));
        vCardSB.append(VCARD_DELIMITER);
      
      vCardSB.append(URLEncoder.encode("TEL;WORK;VOICE:" + phoneBusiness,VCARD_ENCODING));
        vCardSB.append(VCARD_DELIMITER);
      vCardSB.append(URLEncoder.encode("TEL;CELL:" + phoneMobile,VCARD_ENCODING));
        vCardSB.append(VCARD_DELIMITER);
      vCardSB.append(URLEncoder.encode("ADR;"+ VCARD_2_1_ENCODING + ";WORK:;;" + COM_STREET + ";"+ COM_CITY + ";;" + COM_ZIP + ";" + COM_COUNTRY, VCARD_ENCODING));
        vCardSB.append(VCARD_DELIMITER);
      //vCardSB.append(URLEncoder.encode("LABEL;WORK;ENCODING=QUOTED-PRINTABLE:100 Waters Edge=0D=0ABaytown, LA 30314=0D=0AUnited States of America",VCARD_ENCODING));
         // vCardSB.append(VCARD_DELIMITER);
      // TODO : check WORK -> PREF ?
      vCardSB.append(URLEncoder.encode("EMAIL;WORK;INTERNET:" + emailBusiness,VCARD_ENCODING));
        vCardSB.append(VCARD_DELIMITER);
      //vCardSB.append(URLEncoder.encode("URL:" + website,VCARD_ENCODING));
        //vCardSB.append(VCARD_DELIMITER);
     
      // vCardSB.append(URLEncoder.encode("PHOTO;JPEG:" + picture,VCARD_ENCODING));
        // vCardSB.append(VCARD_DELIMITER);
     // TODO 
      //vCardSB.append("LABEL;WORK;ENCODING=QUOTED-PRINTABLE:100 Waters Edge=0D=0ABaytown, LA 30314=0D=0AUnited States of America");
      //vCardSB.append("EMAIL;PREF;INTERNET:forrestgump@example.com");
      vCardSB.append(URLEncoder.encode("END:VCARD",VCARD_ENCODING));

      
    } catch (UnsupportedEncodingException ex) {
      
      Logger.getLogger(CSV2QR.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    return vCardSB.toString();
    
  }
  
  
  boolean containsString(ArrayList<String> stringArrayList, String[] stringArray) {
    Set<String> stringSet = new HashSet<String>(stringArrayList); // if same input is used reused, might want this outside the method

      for (String s : stringArray) {
          if (stringSet.contains(s)) {
              return true;
          }
      }

      return false;
  }
  
  
  
  public String rewriteLineWithQRPath(String workLine, String qrPath){
    
    String newLine = "";
    
    return newLine;
  }
  
  
  public void rewriteCSV(){
    
  }
  
  
}
