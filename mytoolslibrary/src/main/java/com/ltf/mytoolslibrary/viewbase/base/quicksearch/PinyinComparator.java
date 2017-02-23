package com.ltf.mytoolslibrary.viewbase.base.quicksearch;

import java.util.Comparator;

/**
 * 
 * @author 李堂飞
 *compare（a,b）方法:根据第一个参数小于、等于或大于第二个参数分别返回负整数、零或正整数。
 */
public class PinyinComparator implements Comparator<dataBean> {

	public int compare(dataBean o1, dataBean o2) {
		if (o1.getCode().equals("@")
				|| o2.getCode().equals("#")) {
			return -1;
		} else if (o1.getCode().equals("#")
				|| o2.getCode().equals("@")) {
			return 1;
		} else {
			return o1.getCode().compareTo(o2.getCode());
		}
	}


}
