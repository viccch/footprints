package io.github.viccch.footprints.ui.me;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.github.viccch.footprints.AppCommunicationManager;
import io.github.viccch.footprints.R;
import io.github.viccch.footprints.ui.find.Blog;
import io.github.viccch.footprints.ui.find.BlogAdapter;

public class JourneyFragment extends Fragment {

    View view;
    RecyclerView recyclerView;
    List<Blog> blogList;

    public static final String ARG_ID = "id";

    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_journey, container, false);

        initData();
        initRecyclerView();

        return view;
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            id = getArguments().getString(ARG_ID);
        }
    }


    void initData() {
        blogList = new ArrayList<>();
        try {
            blogList.clear();
            String result = AppCommunicationManager.QueryBlogByID(id);
            blogList = new Gson().fromJson(result, new TypeToken<List<Blog>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }

    void initRecyclerView() {
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        BlogAdapter blogAdapter = new BlogAdapter(blogList, this.getActivity());
        recyclerView.setAdapter(blogAdapter);
    }
}