package com.ltf.mytoolslibrary.viewbase.utils;

import android.content.Context;

import com.ltf.mytoolslibrary.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 李堂飞  验证手机号： mobiles:需要验证的手机号码
 *         要严格的验证手机号码，必须先要清楚现在已经开放了哪些数字开头的号码段，目前国内号码段分配如下：
 *         移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
 *         联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
 *         身份证号码验证: 1、号码的结构
 *         公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码
 *         ，三位数字顺序码和一位数字校验码。
 *         2、地址码(前六位数)表示编码对象常住户口所在县(市、旗、区)的行政区划代码，按GB/T2260的规定执行。
 *         3、出生日期码（第七位至十四位）表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。
 *         4、顺序码（第十五位至十七位）表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，
 *         顺序码的奇数分配给男性，偶数分配给女性。 5、校验码（第十八位数） (1)十七位数字本体码加权求和公式 S = Sum(Ai * Wi),
 *         i = 0, ... , 16 ，先对前17位数字的权求和 Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi:
 *         7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 (2)计算模 Y = mod(S, 11)
 *         （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
 */
public class AnyIdCardCheckUtils {

	private static AnyIdCardCheckUtils instance;

	private Context context;
	/** 可以防止实例化多个对象 只要实例化一次则不会重新实例化 **/
	public static AnyIdCardCheckUtils getInstance(Context context) {
		if (instance == null) {
			instance = new AnyIdCardCheckUtils(context);
		}
		return instance;
	}

	private AnyIdCardCheckUtils(Context s) {
		this.context = s.getApplicationContext();//防止内存泄漏,不允许context持有activity的this对象
	}

	/**
	 * 检查手机号码的合法性 
	 * @param mobiles
	 * @return
	 */
	public boolean isMobileNO(String mobiles) {
		Pattern p = Pattern
		// .compile("^((13[0-9])|(15[^4,//D])|(18[0,5-9]))//d{8}$");
				.compile("^(1[3,4,5,7,8][0-9])\\d{8}$");
		Matcher m = p.matcher(mobiles);
		if (anyIdCardCheckOnchlickListener != null) {
			if (m.matches()) {
				anyIdCardCheckOnchlickListener.OnChange(1, "");
			} else if(mobiles.length() != 11){
				anyIdCardCheckOnchlickListener.OnChange(10, context.getResources().getString(R.string.tell_lengthNo));
			} else {
				anyIdCardCheckOnchlickListener.OnChange(10, context.getResources().getString(R.string.login_userName));
			}
		}
		return m.matches();
	}

	/**
	 * 验证字符串是否为座机号码
	 * @param num
	 * @return
	 */
	public boolean isPhone(String num){
		String regx = "([0-9]{3,4}-)?[0-9]{7,8}";
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(num);
		if(m.matches()){
			System.out.println(true);
			return true;
		}else{
			System.out.println(false);
			return false;
		}
	}


	/**
	 * 坚查网址是否输入正确 
	 */
	public boolean isUrl(String url) {
//		String str = "^(http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$";
//		Pattern p = Pattern
//				.compile("^((http|www|ftp|https)?:\\/\\/)?([A-Za-z0-9-~])?$");//支持微信链接
////				.compile("^((http|www|ftp|https)?:\\/\\/)?(www\\.)?[\\w-]+\\.\\w{2,4}(\\/)?$");//支持微信链接
////				.compile(str);
////				.compile("^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\\\/])+$");
//		if(IsNullUtils.isNulls(url)){
////			if (anyIdCardCheckOnchlickListener != null) {
////				anyIdCardCheckOnchlickListener.OnChange(40, context.getResources().getString(R.string.url));
////			}
//			return false;
//		}
//		Matcher m = p.matcher(url);
//		if (anyIdCardCheckOnchlickListener != null) {
////			if (m.matches()) {
////				anyIdCardCheckOnchlickListener.OnChange(4, "");
////			} else {
////				anyIdCardCheckOnchlickListener.OnChange(40, context.getResources().getString(R.string.url));
////			}
//		}
		if(url.startsWith("www") || url.startsWith("http://") || url.startsWith("https://")){
			return true;
		}
		return false;
//		return m.matches();
	}

	/** 匹配密码是否合法(判断输入的字符是否是包含特殊字符 允许6-18字节)// 只允许字母和数字 // String regEx ="[[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";  **/
	public boolean isPassWord(String strName) {
		String strPattern = "^[a-zA-Z][a-zA-Z0-9_@#]{5,17}";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strName);
		if(!m.matches()){
			String strPattern1 = "^[0-9][a-zA-Z0-9_@#]{5,17}";
			Pattern p1 = Pattern.compile(strPattern1);
			Matcher m1 = p1.matcher(strName);
			return m1.matches();
		}
		return m.matches();
	}

	/** 匹配输入是否合法(判断输入的字符是否是字母和数字)// 只允许字母和数字 // String regEx ="[^a-zA-Z0-9]";  **/
	public boolean isNumberOrZiMu(String strName) {
		String strPattern = "[^a-zA-Z0-9]";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strName);
		return m.matches();
	}

	/**
	 * 坚查银行卡是否输入正确 
	 */
	public boolean isBankCard(String bankCard) {
		Pattern p = Pattern
				.compile("^([0-9]{16}|[0-9]{19})$");
		Matcher m = p.matcher(bankCard);
		if (anyIdCardCheckOnchlickListener != null) {
			if (m.matches()) {
				anyIdCardCheckOnchlickListener.OnChange(5, "");
			} else {
				anyIdCardCheckOnchlickListener.OnChange(50, context.getResources().getString(R.string.Register_checkId));
			}
		}
		return m.matches();
	}

	/** 验证邮箱： **/
	public boolean isEmail(String strEmail) {
		String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strEmail);
		if (anyIdCardCheckOnchlickListener != null) {
			if (m.matches()) {
				anyIdCardCheckOnchlickListener.OnChange(2, "");
			}else {
				anyIdCardCheckOnchlickListener.OnChange(20, context.getResources().getString(R.string.mailbox));
			}
		}
		return m.matches();
	}

	/** 匹配帐号是否合法(字母开头，允许5-16字节，允许字母数字下划线)： **/
	public boolean isUserName(String strName) {
		String strPattern = "^[a-zA-Z][a-zA-Z0-9_]{4,15}";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strName);
		return m.matches();
	}

	/* 检查EditText中输入的是否符合规则： */
	public boolean checkString(String s) {// 未验证
		return s.matches("\\w*[.](Java|cpp|class)");
	}

	/**
	 * 功能：身份证的有效验证
	 * @param IDStr 身份证号码
	 * 目前（2015-07-17）中国居民身份证均已升级至二代身份证  现不考虑15位身份证情况
	 * 身份证号验证
	 * 有效：返回""
	 * 无效：返回String验证失败信息
	 */
	public boolean IDCardValidate(String IDStr) {
		String errorInfo = "";// 记录错误信息
		String[] ValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4",
				"3", "2" };
		String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7",
				"9", "10", "5", "8", "4", "2" };
		String Ai = "";
		// ================ 号码的长度 18位 ================
		if (IDStr.length() != 18) {
			errorInfo = context.getResources().getString(R.string.idCarLength);
			if (anyIdCardCheckOnchlickListener != null) {
				anyIdCardCheckOnchlickListener.OnChange(30, errorInfo);
			}
			System.out.println(errorInfo+IDStr.length());
			return false;
		}
		// =======================(end)========================

		// ================ 数字 除最后一位以外都为数字 ================
		if (IDStr.length() == 18) {
			Ai = IDStr.substring(0, 17);
		}
//		else if (IDStr.length() == 15) {
//			Ai = IDStr.substring(0, 6) + "19" + IDStr.substring(6, 15);
//		}
		if (isNumeric(Ai) == false) {
			errorInfo = context.getResources().getString(R.string.isNumeric);
			if (anyIdCardCheckOnchlickListener != null) {
				anyIdCardCheckOnchlickListener.OnChange(30, errorInfo);
			}
			System.out.println(errorInfo);
			return false;
		}
		// =======================(end)========================

		// ================ 出生年月是否有效 ================
		String strYear = Ai.substring(6, 10);// 年份
		String strMonth = Ai.substring(10, 12);// 月份
		String strDay = Ai.substring(12, 14);// 日份
		
		if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
			errorInfo = context.getResources().getString(R.string.strMonth);
			if (anyIdCardCheckOnchlickListener != null) {
				anyIdCardCheckOnchlickListener.OnChange(30, errorInfo);
			}
			System.out.println(errorInfo);
			return false;
		}
		if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
			errorInfo = context.getResources().getString(R.string.strDay);
			if (anyIdCardCheckOnchlickListener != null) {
				anyIdCardCheckOnchlickListener.OnChange(30, errorInfo);
			}
			System.out.println(errorInfo);
			return false;
		}
		
		if (isDataFormat(strYear + "-" + strMonth + "-" + strDay) == false) {
			errorInfo = context.getResources().getString(R.string.isDataFormat);
			if (anyIdCardCheckOnchlickListener != null) {
				anyIdCardCheckOnchlickListener.OnChange(30, errorInfo);
			}
			System.out.println(errorInfo);
			return false;
		}
		GregorianCalendar gc = new GregorianCalendar();
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
					|| (gc.getTime().getTime() - s.parse(
							strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
				errorInfo = context.getResources().getString(R.string.isDataFormat_Isvalid);
				if (anyIdCardCheckOnchlickListener != null) {
					anyIdCardCheckOnchlickListener.OnChange(30, errorInfo);
				}
				System.out.println(errorInfo);
				return false;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		// =====================(end)=====================

		// ================ 地区码时候有效 ================
		Map<String, String> h = GetAreaCode();
		if (h.get(Ai.substring(0, 2)) == null) {
			errorInfo = context.getResources().getString(R.string.AreaCode);
			if (anyIdCardCheckOnchlickListener != null) {
				anyIdCardCheckOnchlickListener.OnChange(30, errorInfo);
			}
			System.out.println(errorInfo);
			return false;
		}
		// ==============================================

		// ================ 判断最后一位的值 ================
		int TotalmulAiWi = 0;
		for (int i = 0; i < 17; i++) {
			TotalmulAiWi = TotalmulAiWi
					+ Integer.parseInt(String.valueOf(Ai.charAt(i)))
					* Integer.parseInt(Wi[i]);
		}
		int modValue = TotalmulAiWi % 11;
		String strVerifyCode = ValCodeArr[modValue];
		Ai = Ai + strVerifyCode;

		if (IDStr.length() == 18) {
			if (Ai.equals(IDStr) == false) {
				errorInfo = context.getResources().getString(R.string.IDStr);
				if (anyIdCardCheckOnchlickListener != null) {
					anyIdCardCheckOnchlickListener.OnChange(30, errorInfo);
				}
				System.out.println(errorInfo);
				return false;
			}
		}
		// =====================(end)=====================
		if (anyIdCardCheckOnchlickListener != null) {
			anyIdCardCheckOnchlickListener.OnChange(3, "");
		}
		System.out.println(errorInfo);
		return true;
	}

	/**
	 * 功能：设置地区编码
	 * 
	 * @return Hashtable 对象
	 */
	private Map<String, String> GetAreaCode() {
		Map<String, String> hashtable = new HashMap<String, String>();
		hashtable.put("11", "北京");
		hashtable.put("12", "天津");
		hashtable.put("13", "河北");
		hashtable.put("14", "山西");
		hashtable.put("15", "内蒙古");
		hashtable.put("21", "辽宁");
		hashtable.put("22", "吉林");
		hashtable.put("23", "黑龙江");
		hashtable.put("31", "上海");
		hashtable.put("32", "江苏");
		hashtable.put("33", "浙江");
		hashtable.put("34", "安徽");
		hashtable.put("35", "福建");
		hashtable.put("36", "江西");
		hashtable.put("37", "山东");
		hashtable.put("41", "河南");
		hashtable.put("42", "湖北");
		hashtable.put("43", "湖南");
		hashtable.put("44", "广东");
		hashtable.put("45", "广西");
		hashtable.put("46", "海南");
		hashtable.put("50", "重庆");
		hashtable.put("51", "四川");
		hashtable.put("52", "贵州");
		hashtable.put("53", "云南");
		hashtable.put("54", "西藏");
		hashtable.put("61", "陕西");
		hashtable.put("62", "甘肃");
		hashtable.put("63", "青海");
		hashtable.put("64", "宁夏");
		hashtable.put("65", "新疆");
		hashtable.put("71", "台湾");
		hashtable.put("81", "香港");
		hashtable.put("82", "澳门");
		hashtable.put("91", "国外");
		return hashtable;
	}

	/**
	 * 功能：判断字符串是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 功能：判断字符串是否金额
	 * 
	 * @param str
	 * @return
	 */
	public boolean isNumeric_JinE(String str) {
		Pattern pattern = Pattern.compile("\\d+\\.?\\d{0,2}");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}
			
	/**
	 * 验证日期字符串是否是YYYY-MM-DD格式
	 * 
	 * @param str
	 * @return
	 */
	public boolean isDataFormat(String str) {
		boolean flag = false;
		// String
		// regxStr="[1-9][0-9]{3}-[0-1][0-2]-((0[1-9])|([12][0-9])|(3[01]))";
		String regxStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|"
				+ "(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]"
				+ "?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468]"
				+ "[1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])"
				+ "|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|"
				+ "(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
		Pattern pattern1 = Pattern.compile(regxStr);
		Matcher isNo = pattern1.matcher(str);
		if (isNo.matches()) {
			flag = true;
		}
		return flag;
	}

	private AnyIdCardCheckOnchlickListener anyIdCardCheckOnchlickListener;

	public void setAnyIdCardCheckOnchlickListener(
			AnyIdCardCheckOnchlickListener anyIdCardCheckOnchlickListenerd) {
		anyIdCardCheckOnchlickListener = anyIdCardCheckOnchlickListenerd;
	};

	public interface AnyIdCardCheckOnchlickListener {
		void OnChange(int falg, String info);
	}
}
