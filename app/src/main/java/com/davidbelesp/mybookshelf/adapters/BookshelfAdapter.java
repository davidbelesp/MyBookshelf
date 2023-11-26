package com.davidbelesp.mybookshelf.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.davidbelesp.mybookshelf.R;
import com.davidbelesp.mybookshelf.controllers.ConfigManager;
import com.davidbelesp.mybookshelf.models.Book;
import com.davidbelesp.mybookshelf.models.BookStatus;
import com.davidbelesp.mybookshelf.models.BookType;
import com.davidbelesp.mybookshelf.utils.ImageUtils;

import java.io.IOException;
import java.util.List;

import jp.wasabeef.blurry.Blurry;

public class BookshelfAdapter extends RecyclerView.Adapter<BookshelfAdapter.ViewHolder> implements View.OnClickListener {

    private List<Book> books;
    private LayoutInflater mInflater;
    private Context ctx;
    private OnClickListener onClickListener;

    public BookshelfAdapter(Context ctx, List<Book> books) {
        this.ctx = ctx;
        this.books = books;
        this.mInflater = LayoutInflater.from(ctx);
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }

    @NonNull
    @Override
    public BookshelfAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(
                R.layout.recycler_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Book actualBook = books.get(position);

        holder.bookTitleText.setText(actualBook.getTitle());
        holder.bookChapterCount.setText(Integer.toString(actualBook.getChapters()));
        holder.bookVolumeCount.setText(Integer.toString(actualBook.getVolumes()));
        holder.bookStatusText.setText(BookStatus.valueOf(actualBook.getStatus()).label);
        holder.bookTypeText.setText(BookType.valueOf(actualBook.getType()).label);
        if(actualBook.getScore()!=0) holder.bookScoreText.setText(String.valueOf(actualBook.getScore()));
        else holder.bookScoreText.setText("-");
        //SETTING THE CORNER OF THE VIEW TO THE BOOK STATUS

        switch (actualBook.getStatusObject().label){

            case "Reading":
                holder.bookStatusCorner.setImageResource(R.drawable.shape_reading);
                break;
            case "On Hold":
                holder.bookStatusCorner.setImageResource(R.drawable.shape_on_hold);
                break;
            case "Completed":
                holder.bookStatusCorner.setImageResource(R.drawable.shape_completed);
                break;
            case "Dropped":
                holder.bookStatusCorner.setImageResource(R.drawable.shape_dropped);
                break;
            default:
                holder.bookStatusCorner.setImageResource(R.drawable.shape_plan_to_read);
                break;

        }

        //SETTING IMAGE OR ALTERNATIVE IF NULL OR NSFW
        if(actualBook.getImage()==null){
            holder.bookImageView.setImageResource(R.drawable.cover_null);
        }
        else if (ConfigManager.loadConfig(ctx).getCensor()==true && actualBook.getNsfw()==true){
            try {
                Blurry.with(ctx)
                        .radius(50)
                        .sampling(1)
                        .from(MediaStore.Images.Media.getBitmap(ctx.getContentResolver(),ImageUtils.loadImage(ctx,actualBook.getImage())))
                        .into(holder.bookImageView);
            } catch (IOException e) {holder.bookImageView.setImageResource(R.drawable.cover_null);}
        } else {
            try {holder.bookImageView.setImageURI(ImageUtils.loadImage(ctx, actualBook.getImage()));}
            catch (IOException e) {throw new RuntimeException(e);}
        }

        holder.itemView.setOnClickListener(view -> {
            if(onClickListener != null){
                onClickListener.onClick(position, actualBook);
            }
        });

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onClick(View v) {

    }

    public interface OnClickListener {
        void onClick(int position, Book book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitleText;
        TextView bookChapterCount;
        TextView bookVolumeCount;
        TextView bookTypeText;
        TextView bookStatusText;
        TextView bookScoreText;
        ImageView bookImageView;
        ImageView bookStatusCorner;

        ViewHolder(View itemView){
            super(itemView);
            bookTitleText = itemView.findViewById(R.id.bookTitle);
            bookChapterCount = itemView.findViewById(R.id.bookChapters);
            bookVolumeCount = itemView.findViewById(R.id.bookVolumes);
            bookTypeText = itemView.findViewById(R.id.bookType);
            bookStatusText = itemView.findViewById(R.id.bookStatus);
            bookScoreText = itemView.findViewById(R.id.bookScore);
            bookImageView = itemView.findViewById(R.id.bookImage);
            bookStatusCorner = itemView.findViewById(R.id.bookStatusCorner);
        }

    }

    public void setFilteredList(List<Book> filteredBookList){
        this.books = filteredBookList;
        notifyDataSetChanged();
    }


}
