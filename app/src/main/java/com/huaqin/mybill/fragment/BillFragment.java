package com.huaqin.mybill.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huaqin.mybill.GlobalVariables;
import com.huaqin.mybill.R;
import com.huaqin.mybill.activity.AddItemActivity;
import com.huaqin.mybill.model.BookItem;
import com.huaqin.mybill.model.IOItem;
import com.huaqin.mybill.model.IOItemAdapter;

import org.litepal.crud.DataSupport;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import at.markushi.ui.CircleButton;


public class BillFragment extends Fragment {
    private String TAG = "BillFragment";
    private View v;
    private static BillFragment billFragment;
    private Button          showBtn;
    private CircleButton    addBtn;
    private RecyclerView    ioItemRecyclerView;
    private ImageView       headerImg;
    private TextView        monthlyCost, monthlyEarn;
//    private ImageButton addBookButton;
    public DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private SimpleDateFormat formatSum  = new SimpleDateFormat("yyyy年MM月", Locale.CHINA);
    private List<BookItem> bookItemList = new ArrayList<>();
    private List<IOItem> ioItemList = new ArrayList<>();
    private IOItemAdapter   ioAdapter;
    // 为ioitem recyclerView设置滑动动作
    private ItemTouchHelper.Callback ioCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            // 获得滑动位置
            final int position = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.RIGHT) {
                // 弹窗确认
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("你确定要删除么？");

                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ioAdapter.removeItem(position);
                        // 刷新界面
                        onResume();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LinearLayout sonView = (LinearLayout) viewHolder.itemView;
                        TextView grandsonTextView = (TextView) sonView.findViewById(R.id.iotem_date);
                        // 判断是否应该显示时间
                        if (sonView.findViewById(R.id.date_bar).getVisibility() == View.VISIBLE)
                            GlobalVariables.setmDate("");
                        else GlobalVariables.setmDate(ioAdapter.getItemDate(position));
                        ioAdapter.notifyItemChanged(position);
                    }
                }).show();  // 显示弹窗
            }
        }
    };
    String sumDate = formatSum.format(new Date());

    public static BillFragment newInstance() {
        if (billFragment == null) {
            billFragment = new BillFragment();
        }
        return billFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        initBookItemList(getActivity());
        initIoItemList(getActivity());

        showBtn.setText("显示余额");

        BookItem tmp = DataSupport.find(BookItem.class, bookItemList.get(GlobalVariables.getmBookPos()).getId());
        monthlyCost.setText(decimalFormat.format(tmp.getSumMonthlyCost()));
        monthlyEarn.setText(decimalFormat.format(tmp.getSumMonthlyEarn()));

    }
    public void initBookItemList(final Context context) {
        bookItemList = DataSupport.findAll(BookItem.class);

        if (bookItemList.isEmpty()) {
            BookItem bookItem = new BookItem();

            bookItem.saveBook(bookItem, 1, "默认账本");
            bookItem.setSumAll(0.0);
            bookItem.setSumMonthlyCost(0.0);
            bookItem.setSumMonthlyEarn(0.0);
            bookItem.setDate(sumDate);
            bookItem.save();

            bookItemList = DataSupport.findAll(BookItem.class);
        }

//        setBookItemRecyclerView(context);
    }

    // 初始化收支项目显示
    public void initIoItemList(final Context context) {

        ioItemList =  DataSupport.where("bookId = ?", String.valueOf(GlobalVariables.getmBookId())).find(IOItem.class);
        setIoItemRecyclerView(context);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.bill_fragment, container, false);
        showBtn = (Button) v.findViewById(R.id.show_money_button);
        addBtn = (CircleButton)  v.findViewById(R.id.add_button);
//        addBookButton = (ImageButton) v.findViewById(R.id.add_book_button);

        ioItemRecyclerView = (RecyclerView)  v.findViewById(R.id.in_and_out_items);
        headerImg = (ImageView)  v.findViewById(R.id.header_img);
        monthlyCost = (TextView)  v.findViewById(R.id.monthly_cost_money);
        monthlyEarn = (TextView)  v.findViewById(R.id.monthly_earn_money);

        // 设置按钮监听
        showBtn.setOnClickListener(new ButtonListener());
        addBtn.setOnClickListener(new ButtonListener());
//        addBookButton.setOnClickListener(new ButtonListener());


        return v;
    }
    private ItemTouchHelper ioTouchHelper = new ItemTouchHelper(ioCallback);

    public void setIoItemRecyclerView(Context context) {
        // 用于存储recyclerView的日期
        GlobalVariables.setmDate("");

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setStackFromEnd(true);    // 列表从底部开始展示，反转后从上方开始展示
        layoutManager.setReverseLayout(true);   // 列表反转

        ioItemRecyclerView.setLayoutManager(layoutManager);
        ioAdapter = new IOItemAdapter(ioItemList);
        ioItemRecyclerView.setAdapter(ioAdapter);
        ioTouchHelper.attachToRecyclerView(ioItemRecyclerView);
    }




//
//    class MyAdapter extends FragmentPagerAdapter {
//
//        public MyAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return fragments.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return fragments.size();
//        }
//
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mTitleList.get(position);
//        }
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
//        }
//    }





// 各个按钮的活动
private class ButtonListener implements View.OnClickListener {
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 按住加号按钮以后，切换到AddItemActivity
            case R.id.add_button:
                Intent intent = new Intent(getActivity(), AddItemActivity.class);
                startActivity(intent);
                break;
            case R.id.show_money_button:
                if (showBtn.getText() == "显示余额") {
                    BookItem tmp = DataSupport.find(BookItem.class, GlobalVariables.getmBookId());

                    String sumString = decimalFormat.format( tmp.getSumAll() );
                    showBtn.setText(sumString);
                } else showBtn.setText("显示余额");
                break;


            default:
                break;
        }
    }
}


}
