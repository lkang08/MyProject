package com.lk.myproject.horizontalpage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.lk.myproject.R;
import com.lk.myproject.activity.BaseActivity;
import com.lk.myproject.horizontalpage.view.DividerItemDecoration;
import com.lk.myproject.horizontalpage.view.HorizontalPageLayoutManager;
import com.lk.myproject.horizontalpage.view.PagingItemDecoration;
import com.lk.myproject.horizontalpage.view.PagingScrollHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HorizontalPageActivity extends BaseActivity implements PagingScrollHelper.onPageChangeListener,
        View.OnClickListener {
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    TextView tv_title;
    PagingScrollHelper scrollHelper = new PagingScrollHelper();
    RadioGroup rg_layout;
    Button btnUpdate;
    TextView tv_page_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_page);
        init();
        rg_layout = (RadioGroup) findViewById(R.id.rg_layout);
        rg_layout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switchLayout(checkedId);
            }
        });
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_page_total = (TextView) findViewById(R.id.tv_page_total);
        btnUpdate = (Button) findViewById(R.id.btn_update);
        btnUpdate.setOnClickListener(this);
        myAdapter = new MyAdapter();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setAdapter(myAdapter);
        scrollHelper.setUpRecycleView(recyclerView);
        scrollHelper.setOnPageChangeListener(this);
        switchLayout(R.id.rb_horizontal_page);
        recyclerView.setHorizontalScrollBarEnabled(true);
        //获取总页数,采用这种方法才能获得正确的页数。否则会因为RecyclerView.State 缓存问题，页数不正确。
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                tv_page_total.setText("共" + scrollHelper.getPageCount() + "页");
            }
        });

    }

    private RecyclerView.ItemDecoration lastItemDecoration = null;
    private HorizontalPageLayoutManager horizontalPageLayoutManager = null;
    private LinearLayoutManager hLinearLayoutManager = null;
    private LinearLayoutManager vLinearLayoutManager = null;
    private DividerItemDecoration hDividerItemDecoration = null;
    private DividerItemDecoration vDividerItemDecoration = null;
    private PagingItemDecoration pagingItemDecoration = null;

    private void init() {
        hLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        hDividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);

        vDividerItemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        vLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        horizontalPageLayoutManager = new HorizontalPageLayoutManager(2, 5);
        pagingItemDecoration = new PagingItemDecoration(this, horizontalPageLayoutManager);

    }

    private void switchLayout(int checkedId) {
        RecyclerView.LayoutManager layoutManager = null;
        RecyclerView.ItemDecoration itemDecoration = null;
        switch (checkedId) {
            case R.id.rb_horizontal_page:
                layoutManager = horizontalPageLayoutManager;
                itemDecoration = pagingItemDecoration;
                break;
            case R.id.rb_vertical_page:
                layoutManager = vLinearLayoutManager;
                itemDecoration = vDividerItemDecoration;
                break;
            case R.id.rb_vertical_page2:
                layoutManager = hLinearLayoutManager;
                itemDecoration = hDividerItemDecoration;
                break;
        }
        if (layoutManager != null) {
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.removeItemDecoration(lastItemDecoration);
            recyclerView.addItemDecoration(itemDecoration);
            scrollHelper.updateLayoutManger();
            scrollHelper.scrollToPosition(0);
            lastItemDecoration = itemDecoration;
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onPageChange(int index) {

        tv_title.setText("第" + (index + 1) + "页");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update:
                updateData();
                break;
        }
    }

    private void updateData() {
        myAdapter.updateData();
        myAdapter.notifyDataSetChanged();
        //滚动到第一页
        scrollHelper.scrollToPosition(0);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                tv_page_total.setText("共" + scrollHelper.getPageCount() + "页");
            }
        });
    }

    private static List<String> data = new ArrayList<>();
    private static int data_name = 0;

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        private Random random = new Random();

        public MyAdapter() {
            setData();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.layout_item, parent, false);
            return new MyViewHolder(view);
        }

        private void setData() {
            int size = random.nextInt(70);
            for (int i = 1; i <= size; i++) {
                data.add(data_name + "-" + i + "");
            }
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final String title = data.get(position);
            holder.tv_title.setText(title);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "item" + title + " 被点击了", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public void updateData() {
            data_name++;
            data.clear();
            setData();
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }

}
