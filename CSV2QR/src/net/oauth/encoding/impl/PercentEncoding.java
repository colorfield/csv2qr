/**
 *  Copyright 2010-2011 Buhake Sindi

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */
package net.oauth.encoding.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import net.oauth.encoding.Encoding;

import org.apache.log4j.Logger;

/**
 * @author Bienfait Sindi
 *
 */
public class PercentEncoding implements Encoding {
	
	private static final Logger logger = Logger.getLogger(PercentEncoding.class);

	/* (non-Javadoc)
	 * @see net.oauth.encoding.OAuthEncoding#decode(java.lang.String)
	 */
	@Override
	public String decode(String encodedData) {
		// TODO Auto-generated method stub
		String data = null;
		
		try {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < encodedData.length(); i++) {
				char percent = encodedData.charAt(i);
				if ('%' == percent) {
					char char1 = encodedData.charAt(i + 1);
					char char2 = encodedData.charAt(i + 2);
					if ('2' == char1 && 'A' == char2) {
						sb.append("*");
					} else if ('2' == char1 && '0' == char2) {
						sb.append("+");
					} else if ('7' == char1 && 'E' == char2) {
						sb.append("~");
					} else {
						sb.append(percent).append(char1).append(char2);
					}
					i += 2;
				} else {
					sb.append(percent);
				}
			}
			encodedData = sb.toString();
			data = URLDecoder.decode(encodedData, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error(e);
		}
		
		return data;
	}

	/* (non-Javadoc)
	 * @see net.oauth.encoding.OAuthEncoding#encode(java.lang.String)
	 */
	@Override
	public String encode(String data) {
		// TODO Auto-generated method stub
		String encodedData = null;

		try {
			encodedData = URLEncoder.encode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			logger.error(e.getLocalizedMessage(), e);
		}
		
		StringBuffer buf = new StringBuffer();
        char focus;
        
        for (int i = 0; i < encodedData.length(); i++) {
            focus = encodedData.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%' && (i + 1) < encodedData.length() && encodedData.charAt(i + 1) == '7' && encodedData.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        
        return buf.toString();
		
//		return OAuthURLUTF8Encoder.encode(data).replace("+", "%20");
	}
}
