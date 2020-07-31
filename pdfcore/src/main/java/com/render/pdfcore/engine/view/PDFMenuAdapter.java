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

public class PDFMenuAdapter extends PDFBaseAdapter {
    private OnItemClickListener mOnItemClickListener;

    public PDFMenuAdapter(IPagesLoader loader) {
        super(loader);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    @Override
    protected ViewHolder onCreateItemViewHolder(ViewGroup viewGroup, int type) {
        return new MenuViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_pdf_menu, viewGroup, false));
    }

    class MenuViewHolder extends ViewHolder {
        private final ImageView mIvMenuItem;

        MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvMenuItem = itemView.findViewById(R.id.iv_menu_item);

            if (mOnItemClickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onItemClick(getLayoutPosition());
                    }
                });
            }
        }

        @Override
        public void bandData(Page page) {
            if (page == null) {
                return;
            }
            Bitmap content = page.getContent();
            if (content == null || content.isRecycled()) {
                return;
            }
            mData.add(page);
            mIvMenuItem.setImageBitmap(content);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int pos);
    }
}
