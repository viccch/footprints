package io.github.viccch.footprints.ui.find;

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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.github.viccch.footprints.APP;
import io.github.viccch.footprints.R;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {

    private final List<Blog> blogList;
    private final Context context;

    public BlogAdapter(List<Blog> blogList, Context context) {
        if (blogList == null) {
            this.blogList = new ArrayList<>();
        } else {
            this.blogList = blogList;
        }
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.list_blog_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Blog blog = blogList.get(position);
        Glide.with(context).load(APP.getInstance().getServerUrl() + blog.blog_content.head_image_url).placeholder(R.drawable.baseline_broken_image_24).into(holder.itemImageView);
        holder.itemTextView_title.setText(blog.blog_title);
        holder.itemTextView_id.setText(blog.blog_user_id);
        holder.itemTextView_datetime.setText(blog.blog_time);

        holder.row_json = new Gson().toJson(blog);
    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        String row_json;
        ImageView itemImageView;
        TextView itemTextView_id;
        TextView itemTextView_title;
        TextView itemTextView_datetime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImageView = itemView.findViewById(R.id.itemImageView);
            itemTextView_id = itemView.findViewById(R.id.itemTextView_id);
            itemTextView_title = itemView.findViewById(R.id.itemTextView_title);
            itemTextView_datetime = itemView.findViewById(R.id.itemTextView_datetime);

            itemView.setOnClickListener(v -> {
//                Toast.makeText(itemView.getContext(), itemTextView_title.getText(), Toast.LENGTH_SHORT).show();
                Intent blogIntent = new Intent(itemView.getContext(), BlogActivity.class);
                blogIntent.putExtra("data", row_json);
                context.startActivity(blogIntent);
            });
        }
    }
}
