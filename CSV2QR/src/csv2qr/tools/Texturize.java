package csv2qr.tools;


import java.util.StringTokenizer;
import sun.text.Normalizer;

/**
 * Remove accents, spaces, ...
 * @author r-daneelolivaw
 */
public class Texturize {


   /* --- USAGE --- */
   /*
   public static String value = "é à î _ @";

   public static void main(String args[]) throws Exception{
       System.out.println(removeAccents(value));
       // output : e a i _ @
   }
   */
    
   public static String convertSpecialChars(String str){
       String tmp = "";
       return tmp;
   }
   
    public static String percentEncode(String str){
       
       String tmp1 = str.replaceAll("[é]","%C3%A9");
       String tmp2 = tmp1.replaceAll("[è]","%C3%A8");
       String tmp3 = tmp2.replaceAll("[ê]","%C3%AA");
       String tmp4 = tmp3.replaceAll("[ë]","%C3%AB");
       
       String tmp5 = tmp4.replaceAll("[ï]", "%C3%AF");
       String tmp6 = tmp5.replaceAll("[î]", "%C3%AE");
       
       String tmp7 = tmp6.replaceAll("[ù]", "%C3%B9");
       String tmp8 = tmp7.replaceAll("[û]", "%C3%BB");
              
       String tmp9 = tmp8.replaceAll("[à]", "%C3%A0");
       String tmp10 = tmp9.replaceAll("[â]", "%C3%A2");
       
       String tmp11 = tmp10.replaceAll("[ç]", "%C3%A7");
       
       String tmp12 = tmp11.replaceAll("[ô]", "%C3%B4");
       String tmp13 = tmp12.replaceAll("[ö]", "%C3%B6");
       String tmp14 = tmp13.replaceAll("[Ô]", "%C3%94");
                     
       // String tmp99 = tmp14.replaceAll("[:,&,?,•,@,',!,),(,#,+,\"]", "_");
       
       return tmp14;
   }
   
  
    

   public static String removeAccents(String str) {

       // String temp = Normalizer.normalize(s, Normalizer.DECOMP, 0);
       // String tmp = s;
       /*
       tmp.replaceAll("ï", "i");
       tmp.replaceAll("î", "i");
       tmp.replaceAll("é", "e");
       tmp.replaceAll("è", "e");
       tmp.replaceAll("ê", "e");
       tmp.replaceAll("ù", "u");
       tmp.replaceAll("ç", "c");
       tmp.replaceAll("à", "a");
       tmp.replaceAll("@", "_at_");
       tmp.replaceAll("&", "_and_");
       tmp.replaceAll("?", "");
       tmp.replaceAll("'", "-");
       tmp.replaceAll(" ", "-");
       tmp.replaceAll(":", "-");
       */

       String tmp1 = str.replaceAll("[é,è,ê,ë]","e");
       String tmp2 = tmp1.replaceAll("[ï,î]", "i");
       String tmp3 = tmp2.replaceAll("[:,&,?,•,@,',!,),(,#,+,\"]", "_");
       String tmp4 = tmp3.replaceAll("[û,ù]", "u");
       String tmp5 = tmp4.replaceAll("[à,â]", "a");
       String tmp6 = tmp5.replaceAll("[ç]", "c");
       String tmp7 = tmp6.replaceAll("[ô,ö,Ô]", "o");
       String tmp8 = tmp7.replaceAll("[ź]", "z");
       String tmp9 = tmp8.replaceAll("[ł]", "l");
       String tmp10 = tmp9.replaceAll("[.]", "");

       return tmp10;
   }

   public static String removeSpaces(String s) {

      /*
      StringTokenizer st = new StringTokenizer(s," ",false);
      String t="";
      while (st.hasMoreElements()) t += st.nextElement();
      */

      // TODO - optimiser : si plusieurs espaces consécutifs,
      // ne mettre qu'un seul underscore
      String t = s.replaceAll(" ", "_");

      return t;
   }
   
   
   
   public static String safeText(String s){
       
       String t1 = s.trim();
       String t2 = t1.replaceAll("'", "‘");
       
       return t2;
       
   }
   

   /*
   public static String replaceWhiteSpace(String s, String r){
       String str = s.replaceAll(s, r);
       return str;
   }
   */


   /**
    * Conversion d'une string en int hexadécimal
    * http://stackoverflow.com/questions/1599205/hex-to-int-number-format-exception-in-java
    * @param hexStr
    * @return
    */
   /*
   public static int hexStringToInt(String hexStr){

        int y;

        if(hexStr != null && !hexStr.equals("")){
            long x = Long.parseLong(hexStr, 16);
            y = (int)(x & 0xffffffff);
        }else{
            y = 0xcc0000;
        }
        return y;
   }
   */
   

}
