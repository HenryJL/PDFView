package com.render.pdfcore.engine.render;


import com.render.pdfcore.engine.view.PDFBaseAdapter;

public class RenderTask {
    public PDFBaseAdapter.ViewHolder holder;
    public int page;
    public boolean print;
    public float width, height;

    public RenderTask(PDFBaseAdapter.ViewHolder holder, int page,
                      boolean print, float width, float height) {
        this.holder = holder;
        this.page = page;
        this.print = print;
        this.width = width;
        this.height = height;
    }
}
