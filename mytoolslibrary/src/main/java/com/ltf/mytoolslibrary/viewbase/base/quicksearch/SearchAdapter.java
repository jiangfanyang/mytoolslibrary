package com.ltf.mytoolslibrary.viewbase.base.quicksearch;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.ltf.mytoolslibrary.viewbase.utils.show.L;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SearchAdapter<T> extends BaseAdapter implements Filterable {
	private List<T> mObjects;
	private List<dataBean> objectss = new ArrayList<>();
	private List<T> objectt = new ArrayList<>();

	private List<Set<String>> pinyinList;//支持多音字,类似:{{z,c},{j},{z},{q,x}}的集合

	private final Object mLock = new Object();

	private int mResource;

	private int mFieldId = 0;

	private Context mContext;

	private ArrayList<T> mOriginalValues;
	private ArrayFilter mFilter;

	private LayoutInflater mInflater;

	public static final int ALL=-1;//全部
	private int maxMatch=10;//最多显示多少个可能选项
	private boolean isdataBean = false;
	/**
	 * 支持多音字
	 */
	public SearchAdapter(Context context,int textViewResourceId, T[] objects,int maxMatch) {
		this.isdataBean = false;
		init(context, textViewResourceId, 0, Arrays.asList(objects));
		this.pinyinList = getHanziSpellList(objects);
		this.maxMatch=maxMatch;
	}
	
	public SearchAdapter(Context context,int textViewResourceId, List<dataBean> objects,int maxMatch) {
		this.objectss = objects;
		this.isdataBean = true;
		this.objectt.clear();
		for (int i=0;i<objects.size();i++){
			this.objectt.add((T) objects.get(i).getName());
		}
		init(context, textViewResourceId, 0, objectt);
		this.pinyinList = getHanziSpellList(objectt);
		this.maxMatch=maxMatch;
	}
	
	private void init(Context context, int resource, int textViewResourceId,List<T> objects) {
		this.mContext = context;
		this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mResource = resource;
		this.mObjects = objects;
		this.mFieldId = textViewResourceId;
	}

	
	/**
	 * 获得汉字拼音首字母列表
	 */
	private List<Set<String>> getHanziSpellList(T[] hanzi){
		List<Set<String>> listSet=new ArrayList<Set<String>>();
		PinYin4j pinyin=new PinYin4j();
		for(int i=0;i<hanzi.length;i++){
			listSet.add(pinyin.getPinyin(hanzi[i].toString()));
		}
		return listSet;
	}
	/**
	 * 获得汉字拼音首字母列表
	 */
	private List<Set<String>> getHanziSpellList(List<T> hanzi){
        List<Set<String>> listSet=new ArrayList<Set<String>>();
        PinYin4j pinyin=new PinYin4j();
        for(int i=0;i<hanzi.size();i++){
        	listSet.add(pinyin.getPinyin(hanzi.get(i).toString()));
        }
        return listSet;
	}
	
	public int getCount() {
		return mObjects.size();
	}

	public T getItem(int position) {
		return mObjects.get(position);
	}

	public int getPosition(T item) {
		return mObjects.indexOf(item);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent, mResource);
	}

	private View createViewFromResource(final int position, View convertView,
										ViewGroup parent, int resource) {
		final View view;
		TextView text;

		if (convertView == null) {
			view = mInflater.inflate(resource, parent, false);
		} else {
			view = convertView;
		}

		try {
			if (mFieldId == 0) {
				text = (TextView) view;
			} else {
				text = (TextView) view.findViewById(mFieldId);
			}
		} catch (ClassCastException e) {
			Log.e("ArrayAdapter",
					"You must supply a resource ID for a TextView");
			throw new IllegalStateException(
					"ArrayAdapter requires the resource ID to be a TextView", e);
		}

		text.setText(getItem(position).toString());
		view.setId(position);
		view.setTag(view);
		if(isdataBean){
			view.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View views) {
					if(mOnCustomItemClickListener != null){
						Map<Integer,Object> map = fromatData(getItem(views.getId()).toString());
						mOnCustomItemClickListener.onCustomItemClick((View) views.getTag(),(Integer) map.get(0),(dataBean) map.get(1));
					}
				}
			});
		}
		return view;
	}

	private Map<Integer,Object> fromatData(String str){
		Map<Integer,Object> map = new HashMap<>();
		for (int i=0;i<objectss.size();i++) {
			L.e("查找被点击的选项JavaBean--->",str+"---->"+objectss.get(i).getName());
			if((str+"").equals(""+objectss.get(i).getName())){
				map.put(0,i);
				map.put(1,objectss.get(i));
				return map;
			}
		}
		return null;
	}

	private OnCustomItemClickListener mOnCustomItemClickListener;
	public void setOnCustomItemClickListener(OnCustomItemClickListener m){
		this.mOnCustomItemClickListener= m;
	}

	@Override
	public Filter getFilter() {
		if (mFilter == null) {
			mFilter = new ArrayFilter();
		}
		return mFilter;
	}

	public interface OnCustomItemClickListener {
		void onCustomItemClick(View view,int position,dataBean data);
	}

	private class ArrayFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence prefix) {
			FilterResults results = new FilterResults();

			if (mOriginalValues == null) {
				synchronized (mLock) {
					mOriginalValues = new ArrayList<T>(mObjects);//
				}
			}


			if (prefix == null || prefix.length() == 0) {
				synchronized (mLock) {
//					ArrayList<T> list = new ArrayList<T>();//无
					ArrayList<T> list = new ArrayList<T>(mOriginalValues);//List<T>
					results.values = list;
					results.count = list.size();
				}
			} else {
				String prefixString = prefix.toString().toLowerCase();

				final ArrayList<T> hanzi = mOriginalValues;//汉字String
				final int count = hanzi.size();

				final Set<T> newValues = new HashSet<T>(count);//支持多音字,不重复

				for (int i = 0; i < count; i++) {
					final T value = hanzi.get(i);//汉字String
					final String valueText = value.toString().toLowerCase();//汉字String
					final Set<String> pinyinSet=pinyinList.get(i);//支持多音字,类似:{z,c}
					Iterator iterator= pinyinSet.iterator();//支持多音字
		        	while (iterator.hasNext()) {//支持多音字
		        		final String pinyin = iterator.next().toString().toLowerCase();//取出多音字里的一个字母

		        		if (pinyin.indexOf(prefixString)!=-1) {//任意匹配
		        			newValues.add(value);
		        		}
		        		else if (valueText.indexOf(prefixString)!=-1) {//如果是汉字则直接添加
		        			newValues.add(value);
		        		}

		        	}
		        	if(maxMatch>0){//有数量限制
			        	if(newValues.size()>maxMatch-1){//不要太多
			        		break;
			        	}
		        	}

				}
				List<T> list=Set2List(newValues);//转成List
				results.values = list;
				results.count = list.size();
			}
			return results;
		}

		@Override
		protected void publishResults(CharSequence charSequence, FilterResults results) {
			mObjects = (List<T>) results.values;
			if (results.count > 0) {
				notifyDataSetChanged();
			} else {
				notifyDataSetInvalidated();
			}
		}

	}
	
	//List Set 相互转换
	public <T extends Object> Set<T> List2Set(List<T> tList) {   
		Set<T> tSet = new HashSet<T>(tList);   
		//TODO 具体实现看需求转换成不同的Set的子类。   
		return tSet;   
	}
	public <T extends Object> List<T> Set2List(Set<T> oSet) {   
		List<T> tList = new ArrayList<T>(oSet);   
		// TODO 需要在用到的时候另外写构造，根据需要生成List的对应子类。   
		return tList;   
	}
}
