package fancy.custom.view;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import fancy.activity.MainActivity;
import fancy.activity.R;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExpandListViewAdapter extends BaseExpandableListAdapter {

	private Context c;
	private List<List<String>> child = new ArrayList<List<String>>();
	private List<String> group = new ArrayList<String>();
	private DragExpandListView expand;
	private boolean groupboolean = false,childboolean = false,childclickboolean=false;
	private int lastposition = 0;
	
	public ExpandListViewAdapter(Context c,List<List<String>> child,
			List<String> group,DragExpandListView expand){
		this.c = c;
		this.child = child;
		this.group = group;
		this.expand = expand;
	}
	public void setGroupboolean(boolean b){
		this.groupboolean = b;
	}
	public void insert(int position, String items){
		child.get(MainActivity.groupPosition).remove(items);
		child.get(MainActivity.groupPosition).add(position, items);
	}
	
	public void insertTgroup(int whichgroup,String items){
		child.get(MainActivity.groupPosition).remove(items);
		child.get(whichgroup).add(items);
	}
	public void removeChild(int whichgroup,String items){
		child.get(whichgroup).remove(items);
	}
	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return group.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return child.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return group.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return child.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	/*android view setVisibility():
		有三个参数：Parameters:visibility One of VISIBLE, INVISIBLE, or GONE，想对应的三个常量值：0、4、8
		VISIBLE:0  意思是可见的
		INVISIBILITY:4 意思是不可见的，但还占着原来的空间
		GONE:8  意思是不可见的，不占用原来的布局空间*/
	@Override
	public View getGroupView(final int groupPosition, final boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		TextView counttv ;
		TextView group_btn ;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.group, null);
		}
//		view.setBackgroundColor(PrefVO.themeColorValue);
		TextView tv = (TextView) view.findViewById(R.id.group_name);
		/**
		 * 此处将原本group_btn换成了textview类型，button在groupview中会导致
		 * 这个groupview不可以collapseGroup()
		 */
		group_btn = (TextView) view.findViewById(R.id.group_count_button);
		
		counttv = (TextView) view.findViewById(R.id.group_count_tv);
		tv.setText(group.get(groupPosition));
		if(child.get(groupPosition).size()!=0){
		if(isExpanded){
			counttv.setVisibility(View.GONE);
			group_btn.setVisibility(View.VISIBLE);
			group_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(c, "expanded", Toast.LENGTH_LONG).show();
				}
			});
		}else{
			
				counttv.setVisibility(View.VISIBLE);
				counttv.setText("("+child.get(groupPosition).size()+")");
				group_btn.setVisibility(View.GONE);
			
		}
		}else{
			counttv.setVisibility(View.GONE);
			group_btn.setVisibility(View.VISIBLE);
			group_btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(c, "expanded", Toast.LENGTH_LONG).show();
				}
			});
		}
		expand.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					break;
				case MotionEvent.ACTION_MOVE:
					groupboolean = true;
					childboolean = true;
					childclickboolean = false;
					break;
				case MotionEvent.ACTION_UP:
					break;
				default:
					break;
				}
				return false;
			}
		});
		if(!groupboolean){
		Animation anim = AnimationUtils.loadAnimation(c, R.anim.push_left_to_right);
		Long offset = 100l+groupPosition*100l;
		anim.setStartOffset(offset);
		view.setAnimation(anim);
		}
		view.requestFocus();
		return view;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getChildView(final int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		if(child.get(groupPosition).size()==0){
			//当此文件夹下没有内容时，显示一个空的view，即layout.itemno
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.itemno, null);	
			}
		}else{
		String note = child.get(groupPosition).get(childPosition);
//		String noteTitle = note.getNoteTitle();
//		Date noteDate = note.getNoteDate();
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item, null);	
		}
		TextView imNoteIcon = (TextView)view.findViewById(R.id.itemimage);
		TextView tvNoteTitle = (TextView)view.findViewById(R.id.itemtitle);
		TextView tvNoteDate = (TextView)view.findViewById(R.id.itemdate);
		view.setDrawingCacheEnabled(false);
//		view.setBackgroundColor(R.color.white);
		//imNoteIcon.setText((childPosition+1)+"");
//		imNoteIcon.setBackgroundResource(note.getNoteby());
		tvNoteTitle.setText(note);
//		tvNoteDate.setText(ConvertDate.datetoString(noteDate));
		imNoteIcon.clearFocus();
		tvNoteDate.clearFocus();
		tvNoteTitle.clearFocus();
		}
		if(!groupboolean||!childboolean){
			view.clearAnimation();
			view.clearFocus();
		Animation anim = AnimationUtils.loadAnimation(c, R.anim.push_left_to_right);
		Long offset = 500l+groupPosition*50l+childPosition*100l;
		anim.setStartOffset(offset);
		view.setAnimation(anim);
		//groupboolean = true;
		}else if(childclickboolean){
			view.clearAnimation();
			view.clearFocus();
			view.requestFocus();
			Animation anim = AnimationUtils.loadAnimation(c, R.anim.push_left_to_right);
			Long offset = 50l+childPosition*500l;
			anim.setStartOffset(offset);
			view.setAnimation(anim);
		}
		/*if(childPosition==getChildrenCount(groupPosition)-groupPosition-1){
			childboolean = true;
			childclickboolean = false;
		}*/
		return view;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void onGroupCollapsed(int groupPosition) {
		// TODO Auto-generated method stub
//		childclickboolean = false;
	}
	@Override
	public void onGroupExpanded(int groupPosition) {
		// TODO Auto-generated method stub
		childclickboolean = true;
//		PrefVO.setGroupPosition(groupPosition);
		expand.setGroupPosition(groupPosition);
		MainActivity.groupPosition = groupPosition;
		for(int i=0;i<getGroupCount();i++)
		{
		if(groupPosition!=i)
		{
		expand.collapseGroup(i);
		}
		}
	}
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	
}
