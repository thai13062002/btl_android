package com.son.comicreader.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.github.chrisbanes.photoview.PhotoView;
import com.son.comicreader.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

public class MyViewPagerAdapter_test extends RecyclerView.Adapter<MyViewPagerAdapter_test.MyViewHolder> {
    Context context;
    List<String> imageLinks;
    LayoutInflater inflater;

    public MyViewPagerAdapter_test(Context context, List<String> imageLinks) {
        this.context = context;
        this.imageLinks = imageLinks;
        inflater = LayoutInflater.from(context);
    }


//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        View image_layout = inflater.inflate(R.layout.view_pager_item,container,false);
//
//        PhotoView page_image = (PhotoView) image_layout.findViewById(R.id.page_image);
//        Picasso.get().load(imageLinks.get(position)).into((page_image));
//        Log.e("photoview", String.valueOf(page_image));
//        Log.e("layout", String.valueOf(image_layout));
//
//        container.addView(image_layout);
//        return image_layout;
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.view_pager_item, parent, false);
        return new MyViewPagerAdapter_test.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewPagerAdapter_test.MyViewHolder holder, int position) {
        Picasso.get().load(imageLinks.get(position)).into((holder.page_image));
    }

    @Override
    public int getItemCount() {
        return imageLinks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        PhotoView page_image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            page_image = (PhotoView) itemView.findViewById(R.id.page_image);

//            itemView.setOnClickListener(this);
        }

    }
}
