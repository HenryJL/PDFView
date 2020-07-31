package com.render.pdfcore.engine.render;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;

import com.render.pdfcore.engine.IPagePool;
import com.render.pdfcore.engine.cache.LRUPageCachePool;
import com.render.pdfcore.engine.page.Page;
import com.render.pdfcore.engine.view.PDFBaseAdapter;

public class PDFMenuHandler extends Handler {
    private static final int MSG_RENDER_TASK = 1;
    private PDFRenderWrapper mRenderCore;
    private IPagePool mPagePool;

    private boolean running;

    public PDFMenuHandler(PDFRenderWrapper render, int maxSize) {
        this.mRenderCore = render;
        this.mPagePool = new LRUPageCachePool(maxSize);
    }

    public int getPageCount() {
        return mRenderCore.getPageCount();
    }

    public Page tryGetMenuPage(PDFBaseAdapter.ViewHolder holder, int page, int width, int height) {
        if (running) {
            Page menu = mPagePool.get(page);
            if (menu != null) {
                return menu;
            }
            addRenderingTask(holder, page, width, height);
        }
        return null;
    }

    public void start() {
        running = true;
    }

    public void stop() {
        running = false;
        removeMessages(MSG_RENDER_TASK);
        if (mPagePool != null) {
            mPagePool.clearMemory();
            mPagePool = null;
        }
    }

    private void addRenderingTask(PDFBaseAdapter.ViewHolder holder, int page, int width, int height) {
        RenderTask task = new RenderTask(holder, page, true, width, height);
        Message msg = obtainMessage(MSG_RENDER_TASK, task);
        sendMessage(msg);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        RenderTask task = (RenderTask) msg.obj;
        Page page = proceedTask(task);
        if (page != null) {
            if (running) {
                task.holder.bandData(page);
                cacheIntoPool(task.page, page);
            } else {
                Bitmap content = page.getContent();
                if (content != null && !content.isRecycled()) {
                    content.recycle();
                }
            }
        }
    }

    private Page proceedTask(RenderTask task) {
        if (task == null) {
            return null;
        }

        mRenderCore.setDisplayRenderMode();

        int w = Math.round(task.width);
        int h = Math.round(task.height);

        Bitmap content = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        content.eraseColor(Color.parseColor("#ffffff"));
        mRenderCore.renderPage(content, task.page);

        return new Page(task.page, content, task.print);
    }

    private void cacheIntoPool(int idx, Page page) {
        mPagePool.put(idx, page);
    }

}
