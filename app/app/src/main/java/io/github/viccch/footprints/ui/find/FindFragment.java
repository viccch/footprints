package io.github.viccch.footprints.ui.find;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.github.viccch.footprints.AppCommunicationManager;
import io.github.viccch.footprints.R;
import io.github.viccch.footprints.utils.SoftKeyboardUtils;

public class FindFragment extends Fragment {

    private View view;
    private List<Blog> blogList;
    private RecyclerView recyclerView;

    private ImageButton btn_fragment_home_flush;

    private ImageView imageView_search;
    private ImageView imageView_close;
    private EditText editText;

//    @Override
//    public void onPause() {
//        super.onPause();
//    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        btn_fragment_home_flush.callOnClick();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_find, container, false);

        initData();
        initRecyclerView();


        btn_fragment_home_flush = view.findViewById(R.id.btn_fragment_home_flush);
        btn_fragment_home_flush.setOnClickListener(v -> {
            initData();
            initRecyclerView();
            Toast.makeText(view.getContext(), "正在刷新", Toast.LENGTH_SHORT).show();
        });

        imageView_search = view.findViewById(R.id.imageView_search);
        imageView_search.setOnClickListener(v -> {
            search();
        });

        imageView_close = view.findViewById(R.id.imageView_close);
        imageView_close.setVisibility(View.GONE);
        imageView_close.setOnClickListener(v -> {
            editText.setText(null);
        });

        editText = view.findViewById(R.id.editText);
        editText.setOnClickListener(v -> {
//            SoftKeyboardUtil.showSoftInput(editText);
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    imageView_close.setVisibility(View.GONE);
                } else {
                    imageView_close.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
                //没有设置imeOptions， 默认actionId == KeyEvent.KEYCODE_ENTER
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE) {//imeOptions设置的动作
                    SoftKeyboardUtils.hideSoftKeyboard(v, true);
                    imageView_search.callOnClick();
                    return true;
                }
                return false;
            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initData() {
        blogList = new ArrayList<>();
        try {
            blogList.clear();
            String result = AppCommunicationManager.QueryBlogAll();
            blogList = new Gson().fromJson(result, new TypeToken<List<Blog>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    private void initRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        BlogAdapter blogAdapter = new BlogAdapter(blogList, getActivity());
        recyclerView.setAdapter(blogAdapter);
    }

    private void search() {

        Toast.makeText(view.getContext(), "正在搜索", Toast.LENGTH_SHORT).show();

        initData();

        blogList.removeIf(e -> !e.blog_title.contains(editText.getText()) && !e.blog_user_id.contains(editText.getText()));
        initRecyclerView();
    }


}
