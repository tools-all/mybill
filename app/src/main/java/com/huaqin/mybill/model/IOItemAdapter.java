package com.huaqin.mybill.model;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.percentlayout.widget.PercentRelativeLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.huaqin.mybill.GlobalVariables;
import com.huaqin.mybill.R;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.util.List;


public class IOItemAdapter extends RecyclerView.Adapter<IOItemAdapter.ViewHolder> {
    private static final String TAG = "IOItemAdapter";
    private final int TYPE_COST = -1;
    private final int TYPE_EARN =  1;

    private List<IOItem> mIOItemList;
    private String mDate;

    public DecimalFormat decimalFormat = new DecimalFormat("0.00");

    static class ViewHolder extends RecyclerView.ViewHolder {
        PercentRelativeLayout earnLayout, costLayout;
        RelativeLayout dateBar;

        ImageView itemImageEarn, itemImageCost;
        TextView itemNameEarn, itemNameCost;
        TextView itemMoneyEarn, itemMoneyCost;
        TextView itemDspEarn, itemDspCost;
        TextView itemDate;

        public ViewHolder(View view) {
            super(view);
            earnLayout = (PercentRelativeLayout) view.findViewById(R.id.earn_left_layout);
            costLayout = (PercentRelativeLayout) view.findViewById(R.id.cost_right_layout);
            dateBar    = (RelativeLayout) view.findViewById(R.id.date_bar);

            itemImageEarn = (ImageView) view.findViewById(R.id.earn_item_img_main);
            itemImageCost = (ImageView) view.findViewById(R.id.cost_item_img_main);
            itemNameEarn  = (TextView ) view.findViewById(R.id.earn_item_name_main);
            itemNameCost  = (TextView ) view.findViewById(R.id.cost_item_name_main);
            itemMoneyEarn = (TextView ) view.findViewById(R.id.earn_item_money_main);
            itemMoneyCost = (TextView ) view.findViewById(R.id.cost_item_money_main);
            itemDspEarn   = (TextView ) view.findViewById(R.id.earn_item_decription);
            itemDspCost   = (TextView ) view.findViewById(R.id.cost_item_decription);
            itemDate      = (TextView ) view.findViewById(R.id.iotem_date);
        }
    }

    public IOItemAdapter(List<IOItem> ioItemList) {
        mIOItemList = ioItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.io_item, parent ,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        IOItem ioItem = mIOItemList.get(position);
        showItemDate(holder, ioItem.getTimeStamp());
        // ?????????????????????
        if (ioItem.getType() == TYPE_COST) {       // -1????????????
            holder.earnLayout.setVisibility(View.GONE);
            holder.costLayout.setVisibility(View.VISIBLE);
            holder.itemImageCost.setImageResource(ioItem.getSrcId());
            holder.itemNameCost.setText(ioItem.getName());
            holder.itemMoneyCost.setText(decimalFormat.format(ioItem.getMoney()));
            handleDescription(ioItem, holder.itemDspCost, holder.itemNameCost, holder.itemMoneyCost);
        //?????????????????????
        } else if (ioItem.getType() == TYPE_EARN) {
            holder.earnLayout.setVisibility(View.VISIBLE);
            holder.costLayout.setVisibility(View.GONE);
            holder.itemImageEarn.setImageResource(ioItem.getSrcId());
            holder.itemNameEarn.setText(ioItem.getName());
            holder.itemMoneyEarn.setText(decimalFormat.format(ioItem.getMoney()));
            handleDescription(ioItem, holder.itemDspEarn, holder.itemNameEarn, holder.itemMoneyEarn);
        }

    }

    @Override
    public int getItemCount() {
        return mIOItemList.size();
    }

    // ??????????????????????????????
    public void showItemDate(ViewHolder holder, String Date) {
        if (GlobalVariables.getmDate().equals(Date)) holder.dateBar.setVisibility(View.GONE);
        else {
            holder.dateBar.setVisibility(View.VISIBLE);
            holder.itemDate.setText(Date);
            GlobalVariables.setmDate(Date);
            Log.d(TAG, "showItemDate: "+Date);
        }
    }

    // ??????????????????????????????????????????????????????????????????????????????????????????
    public String getItemDate(int position) {
        IOItem ioItem = mIOItemList.get(position);
        return ioItem.getTimeStamp();
    }

    public void removeItem(int position) {
        IOItem ioItem = mIOItemList.get(position);
        BookItem bookItem = DataSupport.find(BookItem.class, GlobalVariables.getmBookId());
        int type = ioItem.getType();
        bookItem.setSumAll(bookItem.getSumAll() - ioItem.getMoney()*type);
        // ??????????????????
        if (type < 0) bookItem.setSumMonthlyCost(bookItem.getSumMonthlyCost() - ioItem.getMoney());
        else bookItem.setSumMonthlyEarn(bookItem.getSumMonthlyEarn() - ioItem.getMoney());
        bookItem.save();
        DataSupport.delete(IOItem.class, mIOItemList.get(position).getId());

        mIOItemList.remove(position);
        notifyItemRemoved(position);
    }

    public boolean isThereADescription(IOItem ioItem) {
        return (ioItem.getDescription()!=null && !ioItem.getDescription().equals(""));
    }

    public void handleDescription(IOItem ioItem, TextView Dsp, TextView Name, TextView Money) {
        if (isThereADescription(ioItem)) {
            RelativeLayout.LayoutParams nameParams = (RelativeLayout.LayoutParams)Name.getLayoutParams();
            nameParams.removeRule(RelativeLayout.CENTER_VERTICAL);
            RelativeLayout.LayoutParams moneyParams = (RelativeLayout.LayoutParams)Money.getLayoutParams();
            moneyParams.removeRule(RelativeLayout.CENTER_VERTICAL);
            Dsp.setText(ioItem.getDescription());
            Name.setLayoutParams(nameParams);
            Money.setLayoutParams(moneyParams);
        } else {
            Dsp.setVisibility(View.GONE);
        }
    }
}