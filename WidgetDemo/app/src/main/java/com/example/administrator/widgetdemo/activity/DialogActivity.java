package com.example.administrator.widgetdemo.activity;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.administrator.widgetdemo.R;
import com.example.administrator.widgetdemo.widget.ExpandableListView.AbstractAdapterItem;
import com.example.administrator.widgetdemo.widget.ExpandableListView.BaseExpandableAdapter;
import com.example.administrator.widgetdemo.widget.ExpandableListView.Company;
import com.example.administrator.widgetdemo.widget.ExpandableListView.CompanyItem;
import com.example.administrator.widgetdemo.widget.ExpandableListView.Department;
import com.example.administrator.widgetdemo.widget.ExpandableListView.DepartmentItem;
import com.example.administrator.widgetdemo.widget.dialog.CustomPopupWindow;
import com.example.administrator.widgetdemo.widget.dialog.PopupWindowUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogActivity extends AppCompatActivity implements View.OnClickListener {

    List menuData1;
    List menuData2;
    List menuData3;
    private TextView productTv, sortTv, activityTv;
    private ListView popListView;
    private LinearLayout product, sort, activity;

    CustomPopupWindow popMenu;
    SimpleAdapter simpleAdapter1;
    SimpleAdapter simpleAdapter2;
    SimpleAdapter simpleAdapter3;
    Button btn;

    int menuIndex;

    RecyclerView recyclerView;
    private List mCompanylist;
    private final int ITEM_TYPE_COMPANY = 1;
    private final int ITEM_TYPE_DEPARTMENT = 2;
    private final int ITEM_TYPE_EMPLOYEE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        initMenuData();
        initPopup();
    }

    private void initPopup() {
        recyclerView = findViewById(R.id.rcv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        btn = findViewById(R.id.btn_ss);

        product = (LinearLayout) findViewById(R.id.supplier_list_product);
        sort = (LinearLayout) findViewById(R.id.supplier_list_sort);
        activity = (LinearLayout) findViewById(R.id.supplier_list_activity);
        product.setOnClickListener(this);
        sort.setOnClickListener(this);
        activity.setOnClickListener(this);

        productTv = (TextView) findViewById(R.id.supplier_list_product_tv);
        sortTv = (TextView) findViewById(R.id.supplier_list_sort_tv);
        activityTv = (TextView) findViewById(R.id.supplier_list_activity_tv);

        View contentView = View.inflate(this, R.layout.popwin_supplier_list,
                null);
        popMenu = new CustomPopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        popMenu.setOutsideTouchable(true);
        popMenu.setBackgroundDrawable(new BitmapDrawable());
        popMenu.setFocusable(true);
        popMenu.setAnimationStyle(R.style.popwin_anim_style);
        popMenu.setOnDismissListener(new PopupWindow.OnDismissListener() {
            public void onDismiss() {
                productTv.setTextColor(Color.parseColor("#5a5959"));
                sortTv.setTextColor(Color.parseColor("#5a5959"));
                activityTv.setTextColor(Color.parseColor("#5a5959"));
            }
        });

        popListView = (ListView) contentView
                .findViewById(R.id.popwin_supplier_list_lv);
        contentView.findViewById(R.id.popwin_supplier_list_bottom)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        popMenu.dismiss();
                    }
                });

        simpleAdapter1 = new SimpleAdapter(this, menuData1,
                R.layout.item_listview_popwin, new String[]{"name"},
                new int[]{R.id.listview_popwind_tv});
        simpleAdapter2 = new SimpleAdapter(this, menuData2,
                R.layout.item_listview_popwin, new String[]{"name"},
                new int[]{R.id.listview_popwind_tv});
        simpleAdapter3 = new SimpleAdapter(this, menuData3,
                R.layout.item_listview_popwin, new String[]{"name"},
                new int[]{R.id.listview_popwind_tv});

        initListViewData();
        BaseExpandableAdapter mBaseExpandableAdapter = new BaseExpandableAdapter(mCompanylist) {
            @NonNull
            @Override
            public AbstractAdapterItem<Object> getItemView(Object type) {
                int itemType = (int) type;
                switch (itemType) {
                    case ITEM_TYPE_COMPANY:
                        return new CompanyItem();
                    case ITEM_TYPE_DEPARTMENT:
                        return new DepartmentItem();
                }
                return null;
            }

            @Override
            public Object getItemViewType(Object t) {
                if (t instanceof Company) {
                    return ITEM_TYPE_COMPANY;
                } else if (t instanceof Department)
                    return ITEM_TYPE_DEPARTMENT;
                return -1;
            }

        };
        recyclerView.setAdapter(mBaseExpandableAdapter);


    }

    private void initListViewData() {
        mCompanylist = new ArrayList<>();

        mCompanylist.add(createCompany("Google", true));

        mCompanylist.add(createCompany("Apple", false));
    }

    private Company createCompany(String companyName, boolean isExpandDefault) {
        Company firstCompany = new Company();
        firstCompany.name = companyName;
        List<Department> departments = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Department department = new Department();
            department.name = "Department:" + i;
            departments.add(department);
        }
        firstCompany.mDepartments = departments;
        firstCompany.mExpanded = isExpandDefault;
        return firstCompany;
    }

    private void initMenuData() {
        menuData1 = new ArrayList<Map<String, String>>();
        String[] menuStr1 = new String[]{"全部", "粮油", "衣服", "图书", "电子产品",
                "酒水饮料", "水果"};
        Map<String, String> map1;
        for (int i = 0, len = menuStr1.length; i < len; ++i) {
            map1 = new HashMap<String, String>();
            map1.put("name", menuStr1[i]);
            menuData1.add(map1);
        }

        menuData2 = new ArrayList<Map<String, String>>();
        String[] menuStr2 = new String[]{"综合排序", "配送费最低"};
        Map<String, String> map2;
        for (int i = 0, len = menuStr2.length; i < len; ++i) {
            map2 = new HashMap<String, String>();
            map2.put("name", menuStr2[i]);
            menuData2.add(map2);
        }

        menuData3 = new ArrayList<Map<String, String>>();
        String[] menuStr3 = new String[]{"优惠活动", "特价活动", "免配送费",
                "可在线支付"};
        Map<String, String> map3;
        for (int i = 0, len = menuStr3.length; i < len; ++i) {
            map3 = new HashMap<String, String>();
            map3.put("name", menuStr3[i]);
            menuData3.add(map3);
        }
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.supplier_list_product:
                productTv.setTextColor(Color.parseColor("#39ac69"));
                popListView.setAdapter(simpleAdapter1);
                popMenu.showAsDropDown(product, 0, 2);
                menuIndex = 0;
                break;
            case R.id.supplier_list_sort:
                sortTv.setTextColor(Color.parseColor("#39ac69"));
                popListView.setAdapter(simpleAdapter2);
                popMenu.showAsDropDown(product, 0, 2);
                menuIndex = 1;
                break;
            case R.id.supplier_list_activity:
                activityTv.setTextColor(Color.parseColor("#39ac69"));
                popListView.setAdapter(simpleAdapter3);
                PopupWindowUtil.showAsDropDownStrong(popMenu, activityTv, 0, 2);
                menuIndex = 2;
                break;

        }

    }
}
