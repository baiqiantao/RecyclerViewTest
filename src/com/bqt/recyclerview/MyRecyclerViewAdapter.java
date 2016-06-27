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

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
	private Context context;
	private List<String> mDatas;
	private MyOnItemClickLitener mOnItemClickLitener;

	public void setOnItemClickLitener(MyOnItemClickLitener mOnItemClickLitener) {
		this.mOnItemClickLitener = mOnItemClickLitener;
	}

	public MyRecyclerViewAdapter(Context context, List<String> datas) {
		this.context = context;
		mDatas = datas;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item, parent, false));
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		holder.tv.setText(mDatas.get(position));
		RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) holder.tv.getLayoutParams();
		lp.setMargins(5, 5, 5, 5);//设置边距
		holder.tv.setLayoutParams(lp);

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

	/**添加并更新数据，同时具有动画效果*/
	public void addData(int position) {
		mDatas.add(position, "Insert One");
		notifyItemInserted(position);//更新数据集，注意不是用adapter.notifyDataSetChanged()，否则没有动画效果
	}

	/**移除并更新数据，同时具有动画效果*/
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