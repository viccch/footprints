package io.github.viccch.footprints.ui.me;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import io.github.viccch.footprints.APP;
import io.github.viccch.footprints.AppCommunicationManager;
import io.github.viccch.footprints.R;
import io.github.viccch.footprints.ui.find.Blog;

public class PeopleActivity extends AppCompatActivity {

    String my_id;
    Button btn_subscribe;

    People people;

//    private TextView textView_1;
//    private TextView textView_2;
    private TextView textView_3;
    private TextView textView_4;

    private ImageView imageView_head;
    private TextView textView_id;

    @Override
    public void onResume() {
        super.onResume();

        load_info();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        my_id = APP.getInstance().getUser().getUser_id();

        String id = getIntent().getStringExtra("id");
        people = new People(id);
        setTitle(id + "的主页");

        btn_subscribe = findViewById(R.id.btn_subscribe);
        btn_subscribe.setOnClickListener(v -> {
            String my_id = APP.getInstance().getUser().getUser_id();
            if (btn_subscribe.getText().toString().equals("+关注")) {
                AppCommunicationManager.AddSubscribe(my_id, people.id);
//            btn_subscribe.setVisibility(View.GONE);
                btn_subscribe.setText("取消关注");
            } else {
                AppCommunicationManager.RemoveSubscribe(my_id, people.id);
                btn_subscribe.setText("+关注");
            }
        });

        if (my_id.equals(id)) {
            btn_subscribe.setVisibility(View.GONE);
        }

//        textView_1 = findViewById(R.id.textView_1);
//        textView_2 = findViewById(R.id.textView_2);
        textView_3 = findViewById(R.id.textView_3);
        textView_4 = findViewById(R.id.textView_4);

        textView_id = findViewById(R.id.textView_id);
        textView_id.setText(APP.getInstance().getUser().getUser_id());

        imageView_head = findViewById(R.id.imageView_head);
        Glide.with(this)
                .load(R.drawable.user_head)
                .placeholder(R.drawable.baseline_broken_image_24)
                .transform(new CircleCrop())
                .into(imageView_head);

        textView_id = findViewById(R.id.textView_id);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString(JourneyFragment.ARG_ID, people.id);

        fragmentTransaction.replace(R.id.journeyFragment, JourneyFragment.class, bundle).commit();

        load_info();
    }

    void load_info() {
        String my_id = APP.getInstance().getUser().getUser_id();

        List<Blog> blogList = new ArrayList<>();
        try {
            blogList.clear();
            String result = AppCommunicationManager.QueryBlogByID(APP.getInstance().getUser().getUser_id());
            blogList = new Gson().fromJson(result, new TypeToken<List<Blog>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
            blogList = new ArrayList<>();
        } finally {
//            textView_1.setText(String.valueOf(blogList.size()));
        }

        try {

            String result = AppCommunicationManager.QueryUserSubscribe(people.id);

            List<People.PeopleSubscribe> peopleSubscribeList = new Gson().fromJson(result, new TypeToken<List<People.PeopleSubscribe>>() {
            }.getType());

            for (People.PeopleSubscribe p : peopleSubscribeList) {
                if (p.user_id.equals(people.id)) {
                    people.subscribeList.add(p.user_subscribe);
                } else {
                    people.fansList.add(p.user_id);
                }
            }

            if (people.fansList.contains(my_id)) {
//                btn_subscribe.setVisibility(View.GONE);
                btn_subscribe.setText("取消关注");
            }

            people.fans = people.fansList.size();
            people.subscribe = people.subscribeList.size();

            textView_3.setText(String.valueOf(people.subscribe));
            textView_4.setText(String.valueOf(people.fans));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

}