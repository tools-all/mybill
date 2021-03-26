package com.huaqin.mybill.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.huaqin.mybill.R;
import com.huaqin.mybill.model.IOItem;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private List<View> viewList;

    public ViewPagerAdapter(List<View> viewList) {
        this.viewList = viewList;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView(viewList.get(position));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    /**
     * Created by yuukidach on 17-3-19.
     */

    public static class GridRecyclerAdapter extends RecyclerView.Adapter<GridRecyclerAdapter.ViewHolder> implements View.OnClickListener {
        private static final String TAG = "GridRecyclerAdapter";

        private List<IOItem> mDatas;
        private int curIndex;
        private int pageSize;

        public static interface OnItemClickListener {
            public void onItemClick(View view, int position);
        }

        private OnItemClickListener mOnItemClickListener = null;

        static class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView itemImage;
            private TextView itemTitle;

            public ViewHolder(View view) {
                super(view);
                itemImage = (ImageView) view.findViewById(R.id.item_grid_icon);
                itemTitle = (TextView) view.findViewById(R.id.item_grid_title);
            }
        }

        public GridRecyclerAdapter(List<IOItem> Datas, int curIndex, int pageSize) {
            this.mDatas = Datas;
            this.curIndex = curIndex;
            this.pageSize = pageSize;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d(TAG, "onCreateViewHolder: ");
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chose_io_item, parent, false);
            // 重新设计子项高度
            int height = parent.getHeight();
            view.getLayoutParams().height = height / 4 + 20;
            // 将创建的View注册点击事件
            view.setOnClickListener(this);
            return (new ViewHolder(view));
        }


        @Override
        public long getItemId(int position) {
            return position + curIndex * pageSize;
        }

        @Override
        public int getItemCount() {
            return mDatas.size() > (curIndex + 1) * pageSize ? pageSize : (mDatas.size() - curIndex * pageSize);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: I am in here:" + position);
            int realPositon = position + curIndex * pageSize;
            IOItem ioItem = mDatas.get(realPositon);
            holder.itemImage.setImageResource(ioItem.getSrcId());
            holder.itemTitle.setText(ioItem.getName());
            // 将数据保存在itemView的Tag中，以便点击时进行获取
            holder.itemView.setTag(realPositon);
        }

        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, (int) view.getTag());
            }
        }

        public void setOnItemClickListener(OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }
    }
}
