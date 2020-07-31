package com.render.pdfcore.engine.page;

import com.render.pdfcore.engine.IPagesLoader;
import com.render.pdfcore.engine.render.PDFMenuHandler;
import com.render.pdfcore.engine.render.PDFRenderWrapper;
import com.render.pdfcore.engine.view.PDFBaseAdapter;

public class MenuLoader implements IPagesLoader {
    private boolean running;
    private PDFMenuHandler mMenuHandler;
    private int mMenuWidth;
    private int mMenuHeight;

    public MenuLoader(int width, int height, int maxSize, PDFRenderWrapper render) {
        this.mMenuWidth = width;
        this.mMenuHeight = height;
        this.mMenuHandler = new PDFMenuHandler(render, maxSize);
    }

    @Override
    public int getPageSize() {
        return running ? mMenuHandler.getPageCount() : 0;
    }

    @Override
    public Page loadPage(PDFBaseAdapter.ViewHolder holder, int position) {
        return running ? mMenuHandler.tryGetMenuPage(holder, position, mMenuWidth, mMenuHeight) : null;
    }

    @Override
    public void start() {
        running = true;
        if (mMenuHandler != null) {
            mMenuHandler.start();
        }
    }

    @Override
    public void stop() {
        running = false;
        if (mMenuHandler != null) {
            mMenuHandler.stop();
            mMenuHandler = null;
        }
    }
}
