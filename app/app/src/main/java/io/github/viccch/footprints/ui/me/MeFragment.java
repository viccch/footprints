package io.github.viccch.footprints.ui.me;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
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
import io.github.viccch.footprints.ui.login.LoginActivity;

public class MeFragment extends Fragment {
    private View view;
    private Button btn_logout;

//    private ViewGroup item_1;
//    private ViewGroup item_2;
    private ViewGroup item_3;
    private ViewGroup item_4;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(JourneyFragment.ARG_ID, APP.getInstance().getUser().getUser_id());
        fragmentTransaction.replace(R.id.journeyFragment, JourneyFragment.class, bundle).commit();


        btn_logout = view.findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(v -> {
            Toast.makeText(this.getActivity(), "logout", Toast.LENGTH_SHORT).show();
            logout();
        });


//        textView_1 = view.findViewById(R.id.textView_1);
//        textView_2 = view.findViewById(R.id.textView_2);
        textView_3 = view.findViewById(R.id.textView_3);
        textView_4 = view.findViewById(R.id.textView_4);

//        item_1 = view.findViewById(R.id.item_1);
//        item_1.setOnClickListener(v -> {
//            startActivity(new Intent(this.getActivity(), JourneyFragment.class));
//        });

//        item_2 = view.findViewById(R.id.item_2);
//        item_2.setOnClickListener(v -> {
//            startActivity(new Intent(this.getActivity(), FootprintActivity.class));
//        });

        item_3 = view.findViewById(R.id.item_3);
        item_3.setOnClickListener(v -> {
            startActivity(new Intent(this.getActivity(), SubscribeListActivity.class));
        });

        item_4 = view.findViewById(R.id.item_4);
        item_4.setOnClickListener(v -> {
            startActivity(new Intent(this.getActivity(), FansListActivity.class));
        });

        textView_id = view.findViewById(R.id.textView_id);
        textView_id.setText(APP.getInstance().getUser().getUser_id());

        imageView_head = view.findViewById(R.id.imageView_head);
        Glide.with(view)
                .load(R.drawable.user_head)
                .placeholder(R.drawable.baseline_broken_image_24)
                .transform(new CircleCrop())
                .into(imageView_head);

        textView_id = view.findViewById(R.id.textView_id);

        load_info();

        return view;
    }

    void logout() {

        SharedPreferences sp = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
//        editor.putString("user_id", "");
        editor.putString("user_password", "");
        editor.apply();

        startActivity(new Intent(this.getActivity(), LoginActivity.class));
        this.getActivity().finish();
    }

    void load_info() {

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

        People people = new People(APP.getInstance().getUser().getUser_id());

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