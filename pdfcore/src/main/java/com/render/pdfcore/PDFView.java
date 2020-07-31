package com.render.pdfcore;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.render.pdfcore.engine.IPagesLoader;
import com.render.pdfcore.engine.page.MenuLoader;
import com.render.pdfcore.engine.page.PagesLoader;
import com.render.pdfcore.engine.render.PDFRenderWrapper;
import com.render.pdfcore.engine.view.PDFContentAdapter;
import com.render.pdfcore.engine.view.PDFMenuAdapter;
import com.render.pdfcore.engine.view.PDFRecyclerView;
import com.render.pdfcore.engine.view.PDFToolbar;


public class PDFView extends LinearLayout implements DrawerLayout.DrawerListener,
        PDFToolbar.OnDrawToggleClickListener {
    private static final int MAX_CONTENT_PAGE_CACHE_SIZE = 6;
    private static final int MAX_MENU_PAGE_CACHE_SIZE = 10;
    private String mPath;
    private ProgressBar mPbProgress;
    private DrawerLayout mPDFDrawer;
    private PDFRecyclerView mPDFContent;
    private PDFRecyclerView mPDFMenu;
    private PDFToolbar mPDFToolbar;

    private IPagesLoader mPageLoader;
    private IPagesLoader mMenuLoader;
    private PDFMenuAdapter mPdfMenuAdapter;
    private LinearLayoutManager mPDFContentLayoutManager;

    public PDFView(@NonNull Context context) {
        this(context, null);
    }

    public PDFView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PDFView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOrientation(HORIZONTAL);

        initView();
    }

    private void initView() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.layout_pdf_view,
                this, true);
        mPbProgress = root.findViewById(R.id.pb_progress);

        mPDFToolbar = root.findViewById(R.id.pdf_toolbar);
        mPDFToolbar.setVisibility(GONE);

        mPDFDrawer = root.findViewById(R.id.dl_pdf_menu_drawer);
        mPDFDrawer.setScrimColor(Color.TRANSPARENT);

        mPDFContent = root.findViewById(R.id.rv_pdf_main);
        mPDFContentLayoutManager = new LinearLayoutManager(getContext());
        mPDFContent.setLayoutManager(mPDFContentLayoutManager);
        mPDFMenu = root.findViewById(R.id.rv_pdf_menu);
        mPDFMenu.setLayoutManager(new LinearLayoutManager(getContext()));

        mPDFDrawer.addDrawerListener(this);
//        mPDFToolbar.setOnDrawToggleClickListener(this);
    }

    public void startRender(String path) {
        this.mPath = path;
        mPbProgress.setVisibility(VISIBLE);
        mPageLoader = new PagesLoader(getContext(), path, MAX_CONTENT_PAGE_CACHE_SIZE, null,
                new PDFRenderWrapper.InitCallback() {
                    @Override
                    public void onInitProgress(int progress) {
                        mPbProgress.setProgress(progress);
                    }

                    @Override
                    public void onSuccess() {
                        mPbProgress.setVisibility(GONE);

                        mPDFContent.setAdapter(new PDFContentAdapter(mPageLoader));
                        mPageLoader.start();
                    }

                    @Override
                    public void onFailed(Exception e) {
                        mPbProgress.setVisibility(GONE);

                        Toast.makeText(getContext().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void stopRender() {
        if (mPageLoader != null) {
            mPageLoader.stop();
            mPageLoader = null;
        }
        if (mMenuLoader != null) {
            mMenuLoader.stop();
            mMenuLoader = null;
        }
        if (mPdfMenuAdapter != null) {
            mPdfMenuAdapter = null;
        }
    }

    @Override
    public void onDrawerSlide(@NonNull View view, float v) {

    }

    @Override
    public void onDrawerOpened(@NonNull View view) {
        loadMenu();
    }

    @Override
    public void onDrawerClosed(@NonNull View view) {
        destroyMenu();
    }

    @Override
    public void onDrawerStateChanged(int i) {

    }

    @Override
    public void onToggleClick() {
        openMenu();
    }

    private void openMenu() {
        if (mPDFDrawer.isDrawerOpen(mPDFMenu)) {
            mPDFDrawer.closeDrawer(mPDFMenu, true);
        } else {
            mPDFDrawer.openDrawer(mPDFMenu);
        }
    }

    private void loadMenu() {
        renderMenu(mPath);
    }

    private void renderMenu(String path) {
        if (path == null || path.isEmpty()) {
            return;
        }

        if (mMenuLoader == null) {
            if (mPageLoader instanceof PagesLoader) {
                mMenuLoader = new MenuLoader(mPDFMenu.getWidth(), 200, MAX_MENU_PAGE_CACHE_SIZE,
                        ((PagesLoader) mPageLoader).getRenderHandler().getRenderCore());
            }
        }

        if (mMenuLoader == null) {
            return;
        }
        if (mPdfMenuAdapter == null) {
            mPdfMenuAdapter = new PDFMenuAdapter(mMenuLoader);
        }
        mPdfMenuAdapter.setOnItemClickListener(new PDFMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                selectContentItem(pos);
            }
        });
        mPDFMenu.setAdapter(mPdfMenuAdapter);

        mMenuLoader.start();
    }

    private void destroyMenu() {
        if (mPDFMenu != null) {
            mPDFMenu.removeAllViews();
        }
    }

    private void selectContentItem(int pos) {
        mPDFContentLayoutManager.scrollToPosition(pos);
    }
}
