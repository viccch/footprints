package io.github.viccch.footprints.ui.me;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.github.viccch.footprints.APP;
import io.github.viccch.footprints.AppCommunicationManager;
import io.github.viccch.footprints.R;

public class FansListActivity extends AppCompatActivity {

    List<People> fansList;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans_list);

        setTitle("我的粉丝");

        initData();
        inidRecyclerView();
    }

    void inidRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        PeopleAdapter peopleAdapter = new PeopleAdapter(fansList, this);
        recyclerView.setAdapter(peopleAdapter);
    }

    void initData() {
        fansList = new ArrayList<>();

        try {
            fansList.clear();

            String user_id = APP.getInstance().getUser().getUser_id();

            String result = AppCommunicationManager.QueryUserSubscribe(user_id);

            List<People.PeopleSubscribe> peopleSubscribes = new Gson().fromJson(result, new TypeToken<List<People.PeopleSubscribe>>() {
            }.getType());

            for (People.PeopleSubscribe p : peopleSubscribes) {
                if (p.user_subscribe.equals(user_id)) {
                    fansList.add(new People(p.user_id));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}