package ctd.util;

import opensource.jpinyin.PinyinFormat;
import opensource.jpinyin.PinyinHelper;


public class PyConverter {
	
	public static String getFirstLetter(String s) {
    	 return PinyinHelper.getShortPinyin(s);
     }
    
	public static String getPinYinWithoutTone(String s){
		return PinyinHelper.convertToPinyinString(s, "", PinyinFormat.WITHOUT_TONE);
	}
	
	public static String getPinYin(String s){
	   	return PinyinHelper.convertToPinyinString(s, "");
	}
	
	public  static  void main(String[] args){
		System.out.println(getFirstLetter("中华人民共和国"));
		
	}
}
