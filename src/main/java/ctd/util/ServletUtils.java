package ctd.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.StringTokenizer;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;


public class ServletUtils {
	public static final long ONE_MINUTE_SECONDS = 60;
	public static final long FIFTEEN_MINUTE_SECONDS = ONE_MINUTE_SECONDS * 15;
	public static final long HALF_HOUR_SECONDS = ONE_MINUTE_SECONDS * 30;
	public static final long ONE_HOUR_SECONDS = ONE_MINUTE_SECONDS * 60;
	public static final long ONE_DAY_SECONDS = ONE_HOUR_SECONDS * 24;
	public static final long ONE_WEEK_SECONDS = ONE_DAY_SECONDS * 7;
	public static final long ONE_MONTH_SECONDS = ONE_DAY_SECONDS * 30;
	public static final long ONE_YEAR_SECONDS = ONE_DAY_SECONDS * 365;
	
	public static final String EXCEL_TYPE = "application/vnd.ms-excel";
	public static final String HTML_TYPE = "text/html";
	public static final String JS_TYPE = "text/javascript";
	public static final String JSON_TYPE = "application/json";
	public static final String XML_TYPE = "text/xml";
	public static final String TEXT_TYPE = "text/plain";
	public static final String CSS_TYPE = "text/css";
	public static final String FLASH_TYPE = "application/x-shockwave-flash";
	
	public static final String DEFAULT_ENCODING = "utf-8";
	
	
	public static void setNoCacheHeader(HttpServletResponse response) {
		response.setDateHeader("Expires", 1L);
		response.addHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
	}
	
	public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
		if(expiresSeconds == 0){
			setNoCacheHeader(response);
		}
		else{
			response.setDateHeader("Expires", System.currentTimeMillis() + expiresSeconds * 1000);
			response.setHeader("Cache-Control", "private, max-age=" + expiresSeconds);
		}
	}
	
	public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
		response.setDateHeader("Last-Modified", lastModifiedDate);
	}
	
	public static void setEtag(HttpServletResponse response, String etag) {
		response.setHeader("ETag", etag);
	}
	
	public static boolean isAcceptGzip(HttpServletRequest request) {
		String acceptEncoding = request.getHeader("Accept-Encoding");
		return StringUtils.contains(acceptEncoding, "gzip");
	}
	
	public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response, long lastModified) {
		long ifModifiedSince = request.getDateHeader("If-Modified-Since");
		if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		return true;
	}
	
	public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
		String headerValue = request.getHeader("If-None-Match");
		if (headerValue != null) {
			boolean conditionSatisfied = false;
			if (!"*".equals(headerValue)) {
				StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");
				while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
					String currentToken = commaTokenizer.nextToken();
					if (currentToken.trim().equals(etag)) {
						conditionSatisfied = true;
					}
				}
			} else {
				conditionSatisfied = true;
			}

			if (conditionSatisfied) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				response.setHeader("ETag", etag);
				return false;
			}
		}
		return true;
	}
	
	public static boolean checkAndSetExpiresHeaders(HttpServletRequest request,HttpServletResponse response,long lastModi,long expires){
		String etag = "W/\"" + lastModi + "\"";
		setExpiresHeader(response,expires);
		if (!checkIfModifiedSince(request, response, lastModi) || !checkIfNoneMatchEtag(request, response, etag)) {
			return false;
		}
		setLastModifiedHeader(response,lastModi);
		setEtag(response, etag);
		
		return true;
	}
	
	public static boolean checkAndSetExpiresHeaders(HttpServletRequest request,HttpServletResponse response,long lastModi){
		return checkAndSetExpiresHeaders(request,response,lastModi,ServletUtils.ONE_YEAR_SECONDS);
	}
	
	public static void setFileDownloadHeader(HttpServletResponse response, String fileName) {
		try {
			
			String encodedfileName = new String(fileName.getBytes("GBK"), "ISO8859-1");
			response.setCharacterEncoding(ServletUtils.DEFAULT_ENCODING);
			response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedfileName + "\"");
		} 
		catch (UnsupportedEncodingException e) {
		}
	}
	
	public static OutputStream buildGzipOutputStream(HttpServletResponse response) throws IOException {
		response.setHeader("Content-Encoding", "gzip");
		response.setHeader("Vary", "Accept-Encoding");
		return new GZIPOutputStream(response.getOutputStream());
	}
	
	 public static String getIpAddress(HttpServletRequest request) {
	     String ip = request.getHeader("x-forwarded-for");
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	         ip = request.getHeader("Proxy-Client-IP");
	     }
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	         ip = request.getHeader("WL-Proxy-Client-IP");
	     }
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	         ip = request.getRemoteAddr();
	     }
	     return ip;
	 }
}
