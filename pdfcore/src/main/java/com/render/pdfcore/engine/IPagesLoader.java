package com.render.pdfcore.engine;

import com.render.pdfcore.engine.page.Page;
import com.render.pdfcore.engine.view.PDFBaseAdapter;

public interface IPagesLoader {
    int getPageSize();

    Page loadPage(PDFBaseAdapter.ViewHolder holder, int position);

    void start();

    void stop();
}
