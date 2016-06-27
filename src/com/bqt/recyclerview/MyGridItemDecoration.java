package com.bqt.recyclerview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.State;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**zhy写的分割线，Item如果为最后一列则右边无间隔线，如果为最后一行则底部无分割线*/
public class MyGridItemDecoration extends RecyclerView.ItemDecoration {
	private Drawable mDivider;

	public MyGridItemDecoration(Context context) {
		//通过读取系统主题中的 Android.R.attr.listDivider属性，将其作为Item间的分割线， <item name="android:listDivider">@drawable/divider_bg</item>  
		TypedArray typedArray = context.obtainStyledAttributes(new int[] { android.R.attr.listDivider });
		mDivider = typedArray.getDrawable(0);
		typedArray.recycle();//回收TypedArray，以便后面重用。This TypedArray should be recycled after use with recycle()
	}

	@Override
	/**判断如果是最后一行，则不需要绘制底部；如果是最后一列，则不需要绘制右边，整个判断也考虑到了StaggeredGridLayoutManager的横向和纵向*/
	public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
		int spanCount = getSpanCount(parent);
		int itemCount = parent.getAdapter().getItemCount();
		//使用outRect设置绘制的范围。一般如果仅仅是希望有空隙，还是去设置item的margin方便
		if (isLastRaw(parent, itemPosition, spanCount, itemCount)) {// 如果是最后一行，则不需要绘制底部
			outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
		} else if (isLastColum(parent, itemPosition, spanCount, itemCount)) {// 如果是最后一列，则不需要绘制右边
			outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
		} else {
			outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
		}
	}

	@Override
	public void onDraw(Canvas c, RecyclerView parent, State state) {
		drawHorizontal(c, parent);
		drawVertical(c, parent);
	}

	//******************************************************************************************
	public void drawHorizontal(Canvas c, RecyclerView parent) {
		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);
			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			final int left = child.getLeft() - params.leftMargin;
			final int right = child.getRight() + params.rightMargin + mDivider.getIntrinsicWidth();
			final int top = child.getBottom() + params.bottomMargin;
			final int bottom = top + mDivider.getIntrinsicHeight();
			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
		}
	}

	public void drawVertical(Canvas c, RecyclerView parent) {
		final int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			final View child = parent.getChildAt(i);

			final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
			final int top = child.getTop() - params.topMargin;
			final int bottom = child.getBottom() + params.bottomMargin;
			final int left = child.getRight() + params.rightMargin;
			final int right = left + mDivider.getIntrinsicWidth();

			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
		}
	}

	//******************************************************************************************
	/**获取RecyclerView有多少列*/
	private int getSpanCount(RecyclerView parent) {
		int spanCount = -1; // 列数
		LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager) spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
		else if (layoutManager instanceof StaggeredGridLayoutManager) spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
		return spanCount;
	}

	private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
		LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager) {
			childCount = childCount - childCount % spanCount;
			if (pos >= childCount) // 如果是最后一行，则不需要绘制底部
			return true;
		} else if (layoutManager instanceof StaggeredGridLayoutManager) {
			int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
			// StaggeredGridLayoutManager 且纵向滚动
			if (orientation == StaggeredGridLayoutManager.VERTICAL) {
				childCount = childCount - childCount % spanCount;
				if (pos >= childCount) return true;// 如果是最后一行，则不需要绘制底部
			} else { // StaggeredGridLayoutManager 且横向滚动
				if ((pos + 1) % spanCount == 0) return true;// 如果是最后一行，则不需要绘制底部
			}
		}
		return false;
	}

	private boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount) {
		LayoutManager layoutManager = parent.getLayoutManager();
		if (layoutManager instanceof GridLayoutManager) {
			if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
				return true;
			}
		} else if (layoutManager instanceof StaggeredGridLayoutManager) {
			int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
			if (orientation == StaggeredGridLayoutManager.VERTICAL) {
				if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
					return true;
				}
			} else {
				childCount = childCount - childCount % spanCount;
				if (pos >= childCount) // 如果是最后一列，则不需要绘制右边
				return true;
			}
		}
		return false;
	}
}