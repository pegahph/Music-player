package com.example.musicplayer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.FolderViewHolder> {

    private ArrayList<Folder> folders;

    public FolderAdapter(ArrayList<Folder> folders) {
        this.folders = folders;
    }


    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View folderView = inflater.inflate(R.layout.artist, parent, false);
        return new FolderViewHolder(folderView);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return folders.size();
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder {

        private TextView folderName;
        private ImageView folderArt;
        private View thisView;

        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);

            folderName = (TextView) itemView.findViewById(R.id.artist_name);
            folderArt = (ImageView) itemView.findViewById(R.id.albums_art);
            thisView = itemView;
        }

        public void bindView(int position) {
            Folder currentFolder = folders.get(position);
            folderName.setText(currentFolder.getFolderName());
//            Drawable folderArtImage = currentFolder.getAlbumArtBitmapDrawable();
//            if (folderArtImage != null)
//                folderArt.setImageDrawable(folderArtImage);
//            else
//                folderArt.setImageResource(R.drawable.icon2);
            thisView.setTag(currentFolder);
        }
    }
}
