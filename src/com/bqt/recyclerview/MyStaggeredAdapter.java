package com.bqt.recyclerview;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

/**和MyAdapter唯一的区别就是：在代码中动态设置了TextView的高度(宽度)*/
public class MyStaggeredAdapter extends RecyclerView.Adapter<MyStaggeredAdapter.MyViewHolder> {
	private Context context;
	private List<String> mDatas;
	private List<Integer> mHeights;//高度

	private MyOnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(MyOnItemClickLitener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	public MyStaggeredAdapter(Context context, List<String> datas, List<Integer> heights) {
		this.context = context;
		this.mDatas = datas;
		this.mHeights = heights;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false));
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) holder.tv.getLayoutParams();
		lp.setMargins(5, 5, 5, 5);
		//横向时，item的宽度需要设置；纵向时，item的高度需要设置
		lp.height = mHeights.get(position);//******************************************************************************************唯一的区别在这里！
		lp.width = mHeights.get(position);//*******************************************************************************************唯一的区别在这里！
		holder.tv.setLayoutParams(lp);
		holder.tv.setText(mDatas.get(position));

		// 如果设置了回调，则设置点击事件
		if (mOnItemClickLitener != null) {
			holder.itemView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int pos = holder.getLayoutPosition();
					mOnItemClickLitener.onItemClick(holder.itemView, pos);
				}
			});
			holder.itemView.setOnLongClickListener(new OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					int pos = holder.getLayoutPosition();
					mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
					removeData(pos);
					return false;
				}
			});
		}
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	public void addData(int position) {
		mDatas.add(position, "Insert One");
		mHeights.add((int) (100 + Math.random() * 300));
		notifyItemInserted(position);
	}

	public void removeData(int position) {
		mDatas.remove(position);
		notifyItemRemoved(position);
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		TextView tv;

		public MyViewHolder(View view) {
			super(view);
			tv = (TextView) view.findViewById(R.id.id_num);
		}
	}
}