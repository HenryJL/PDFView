package com.render.pdfcore.engine.page;

import android.content.Context;

import com.render.pdfcore.engine.IPagesLoader;
import com.render.pdfcore.engine.RenderParams;
import com.render.pdfcore.engine.render.PDFRenderHandler;
import com.render.pdfcore.engine.render.PDFRenderWrapper;
import com.render.pdfcore.engine.view.PDFBaseAdapter;


public class PagesLoader implements IPagesLoader {
    private PDFRenderHandler mRenderHandler;
    private RenderParams mRenderParams;

    public PagesLoader(Context ctx, String path, int maxPagePoolSize, final RenderParams params,
                       final PDFRenderWrapper.InitCallback callback) {
        this.mRenderHandler = new PDFRenderHandler(ctx, path, maxPagePoolSize, new PDFRenderWrapper.InitCallback() {
            @Override
            public void onInitProgress(int progress) {
                if (callback != null) {
                    callback.onInitProgress(progress);
                }
            }

            @Override
            public void onSuccess() {
                PagesLoader.this.mRenderParams = params == null ? mRenderHandler.tryGetRenderParams() : params;

                if (callback != null) {
                    callback.onSuccess();
                }
            }

            @Override
            public void onFailed(Exception e) {
                if (callback != null) {
                    callback.onFailed(e);
                }
            }
        });

    }

    public PDFRenderHandler getRenderHandler() {
        return mRenderHandler;
    }

    @Override
    public int getPageSize() {
        return mRenderHandler.getPageCount();
    }

    @Override
    public Page loadPage(PDFBaseAdapter.ViewHolder holder, int position) {
        return mRenderHandler.tryGetPage(holder, position,
                mRenderParams.getPageWidth(), mRenderParams.getPageHeight());
    }

    @Override
    public void start() {
        mRenderHandler.start();
    }

    @Override
    public void stop() {
        mRenderHandler.stop();
    }
}
