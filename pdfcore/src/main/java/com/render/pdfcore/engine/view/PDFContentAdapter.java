package com.render.pdfcore.engine.view;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.render.pdfcore.R;
import com.render.pdfcore.engine.IPagesLoader;
import com.render.pdfcore.engine.page.Page;

public class PDFContentAdapter extends PDFBaseAdapter {
    public PDFContentAdapter(IPagesLoader loader) {
        super(loader);
    }

    @Override
    protected ViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int type) {
        return new ContentViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_pdf_render, viewGroup, false));
    }

    class ContentViewHolder extends ViewHolder {
        private final ImageView mPhotoView;

        ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            mPhotoView = itemView.findViewById(R.id.photo_view);
        }

        @Override
        public void bandData(Page page) {
            if (page == null) {
                return;
            }
            Bitmap content = page.getContent();
            if (content == null || content.isRecycled()) {
//                mPhotoView.setImageResource(R.drawable.pdf_menu_item_bg);
                return;
            }
            if (!mData.contains(page)) {
                mData.add(page);
            }
            mPhotoView.setImageBitmap(content);
        }
    }
}
