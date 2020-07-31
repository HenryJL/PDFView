package com.render.pdfcore.engine;

import com.render.pdfcore.engine.page.Page;

public interface IPagePool {
    long getMaxSize();

    void put(int idx, Page page);

    Page get(int idx);

    void clearMemory();
}
