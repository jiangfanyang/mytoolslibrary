package com.ltf.mytoolslibrary.viewbase.base.quicksearch;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PinyinHelper{
	private static PinyinHelper instance;
	private Properties properties = null;
	
    public static String[] getUnformattedHanyuPinyinStringArray(char ch){
        return getInstance().getHanyuPinyinStringArray(ch);
    }

    private PinyinHelper(){
    	initResource();
    }

    public static PinyinHelper getInstance(){
    	if(instance==null){
    		instance = new PinyinHelper();
    	}
        return instance;
    }
    
    private void initResource(){
        try{
//        	final String resourceName = "/assets/unicode_to_hanyu_pinyin.txt";
          final String resourceName = "/assets/unicode_to_simple_pinyin.txt";

          BufferedInputStream bis=new BufferedInputStream(PinyinHelper.class.getResourceAsStream(resourceName));
          properties=new Properties();
          properties.load(bis);

        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private String[] getHanyuPinyinStringArray(char ch){
        String pinyinRecord = getHanyuPinyinRecordFromChar(ch);

        if (null != pinyinRecord){
            int indexOfLeftBracket = pinyinRecord.indexOf(Field.LEFT_BRACKET);
            int indexOfRightBracket = pinyinRecord.lastIndexOf(Field.RIGHT_BRACKET);

            String stripedString = pinyinRecord.substring(indexOfLeftBracket
                    + Field.LEFT_BRACKET.length(), indexOfRightBracket);

            return stripedString.split(Field.COMMA);

        } else
            return null;
        
    }
    
    private String getHanyuPinyinRecordFromChar(char ch){
        int codePointOfChar = ch;
        String codepointHexStr = Integer.toHexString(codePointOfChar).toUpperCase();
        String foundRecord = properties.getProperty(codepointHexStr);
        return foundRecord;
    }

    class Field{
        static final String LEFT_BRACKET = "(";
        static final String RIGHT_BRACKET = ")";
        static final String COMMA = ",";
    }

    // 返回整个中文的首字母
    public String getPinYinHeadChars(String str) {
        PinYin4j t = new PinYin4j();
        return getAlpha(t.makeStringByStringSet(t.getPinyin(str)));//demo格式是所有汉字的首字母大写拼接在一起 列如:可以么  转换后KYM就是demo的值
//        return getAlpha(CharacterParser.getCharacterParser().getSelling(str));//demo格式是所有汉字的首字母大写拼接在一起 列如:可以么  转换后KYM就是demo的值
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *  重庆 chognqing zhongqing zq,cq
     *  长沙 changsha zhangsha cs,zs
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        if(("zq,cq").toUpperCase().equals(str.toUpperCase()+"")){
            str = "chongqing";
        }else if(("cs,zs").toUpperCase().equals(str.toUpperCase()+"")){
            str = "changsha";
        }
        String  sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }
}
