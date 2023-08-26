package io.github.viccch.footprints.ui.me;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.github.viccch.footprints.AppCommunicationManager;
import io.github.viccch.footprints.R;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.ViewHolder> {

    final List<People> peopleList;
    final Context context;

    public PeopleAdapter(List<People> peopleList, Context context) {
        this.peopleList = peopleList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.list_me_people, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        People people = peopleList.get(position);

        load_info(people);

        holder.textView_id.setText(people.id);
        holder.textView_fans.setText(String.valueOf(people.fans));
        holder.textView_subscribe.setText(String.valueOf(people.subscribe));

        Glide.with(context)
                .load(people.head)
                .placeholder(R.drawable.baseline_broken_image_24)
                .transform(new CircleCrop())
                .into(holder.imageView_head);
    }

    void load_info(People people) {

//        people.head = "https://www.w3school.com.cn/i/eg_tulip.jpg";

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

        } catch (Exception e) {

        } finally {

        }
    }

    @Override
    public int getItemCount() {
        return peopleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView_head;
        TextView textView_id;
        TextView textView_subscribe;
        TextView textView_fans;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView_head = itemView.findViewById(R.id.imageView_head);
            textView_id = itemView.findViewById(R.id.textView_id);
            textView_subscribe = itemView.findViewById(R.id.textView_subscribe);
            textView_fans = itemView.findViewById(R.id.textView_fans);

            itemView.setOnClickListener(v -> {
                Intent peopleIntent = new Intent(itemView.getContext(), PeopleActivity.class);
                peopleIntent.putExtra("id", textView_id.getText());
                context.startActivity(peopleIntent);
            });
        }


    }
}
