package io.github.viccch.footprints;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import java.util.List;

import io.github.viccch.footprints.ui.record.RecordFragment;
import io.github.viccch.footprints.ui.find.FindFragment;
import io.github.viccch.footprints.ui.me.MeFragment;
import io.github.viccch.footprints.utils.CheckPermissionsActivity;

public class MainActivity extends CheckPermissionsActivity {

    RadioGroup radioGroup;
    FindFragment findFragment;
    RecordFragment recordFragment;
    MeFragment meFragment;

    public static void removeAllFragments(FragmentActivity context) {
        FragmentManager fragmentManager = context.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        List<Fragment> fragmentList = fragmentManager.getFragments();
        if (fragmentList == null || fragmentList.isEmpty()) {
            Log.e("test", context + " no old Fragment, fragmentList = " + fragmentList);
            return;
        }
        for (Fragment f : fragmentList) {
            transaction.remove(f);
            Log.e("test", context + " delete old Fragment : " + f);
        }
        transaction.commitNow();
        //transaction.commit();
    }

    void hideFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (findFragment != null) {
            transaction.hide(findFragment);
        }
//        if (recordFragment != null) {
//            transaction.hide(recordFragment);
//        }
        if (recordFragment != null) {
            transaction.hide(recordFragment);
        }
        if (meFragment != null) {
            transaction.hide(meFragment);
        }
        transaction.commit();
    }

    void selectFragment(int i) {
        hideFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (i) {
            case 0:
                if (findFragment == null) {
                    findFragment = new FindFragment();
                    transaction.add(R.id.frag_container, findFragment);
                } else {
                    transaction.show(findFragment);
                }
                break;
            case 1:
//                if (recordFragment == null) {
//                    recordFragment = new RecordFragment();
//                    transaction.add(R.id.frag_container, recordFragment);
//                } else {
//                    transaction.show(recordFragment);
//                }
                if (recordFragment == null) {
                    recordFragment = new RecordFragment();
                    transaction.add(R.id.frag_container, recordFragment);
                } else {
                    transaction.show(recordFragment);
                }
                break;
            case 2:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    transaction.add(R.id.frag_container, meFragment);
                } else {
                    transaction.show(meFragment);
                }
                break;
            default:
                break;
        }
        transaction.commit();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        removeAllFragments(this);

        //初始化控件
        radioGroup = findViewById(R.id.rg_tab);
        //对单选按钮进行监听，选中、未选中
        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rb_find) {
                selectFragment(0);
            } else if (i == R.id.rb_record) {
                selectFragment(1);
            } else if (i == R.id.rb_me) {
                selectFragment(2);
            }
        });

        selectFragment(0);

        //设置回退键
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}