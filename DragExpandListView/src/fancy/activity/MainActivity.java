package fancy.activity;

import java.util.ArrayList;
import java.util.List;

import fancy.custom.view.DragExpandListView;
import fancy.custom.view.DragExpandListView.DropViewListener;
import fancy.custom.view.ExpandListViewAdapter;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;

public class MainActivity extends Activity  implements DropViewListener{

	//在drop方法中需要用到的group位置
	public static int groupPosition;
	private DragExpandListView expand;
	private ExpandListViewAdapter adapter;
	private List<String> groups;
	private List<List<String>> children;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		expand = (DragExpandListView) findViewById(R.id.expandlistview);
		expand.setVerticalFadingEdgeEnabled(true);
		expand.setGroups(groups);
		adapter = new ExpandListViewAdapter(this, children, groups,expand);
		expand.setAdapter(adapter);
		
		expand.requestFocus();
		expand.setmAdapter(adapter);
		expand.setDropViewListener(this);
		expand.setGroupIndicator(null);
		//这里的返回true，自己处理完，在childview中setAnimation之后会导致group需要点击两次的情况
		expand.setOnGroupClickListener(new OnGroupClickListener() {
			
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// TODO Auto-generated method stub
				System.out.println("--------the group be clicked-----");
				adapter.setGroupboolean(true);
				if(expand.isGroupExpanded(groupPosition)){
					expand.collapseGroup(groupPosition);
				}else{
					expand.expandGroup(groupPosition);
				}
				return true;
			}
		});
	}

	
	private void init() {
		// TODO Auto-generated method stub
		groups = new ArrayList<String>();
		children = new ArrayList<List<String>>();
		for(int i=0;i<5;i++){
			groups.add("group>>"+i);
			List<String> list = new ArrayList<String>();
			for(int j=0;j<5;j++){
				list.add(i+"<<child>>"+j);
			}
			children.add(list);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}


	@Override
	public void drop(int from, int to) {
		// TODO Auto-generated method stub
		adapter.insert(to-groupPosition-1, (String)adapter.getChild(groupPosition, from-groupPosition-1));
		adapter.notifyDataSetChanged();
	}


	@Override
	public void dropTgroup(int from, int towhichgroup) {
		// TODO Auto-generated method stub
		String note = (String)adapter.getChild(groupPosition, from-groupPosition-1);
		adapter.insertTgroup(towhichgroup, note);
		adapter.notifyDataSetChanged();
	}


}
