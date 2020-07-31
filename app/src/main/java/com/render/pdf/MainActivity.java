package com.render.pdf;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.render.pdfcore.PDFView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private PDFView mPdfView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPdfView = findViewById(R.id.pdf_view);

        startRender("test.pdf");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRender();
    }

    public void startRender(String path) {
        mPdfView.startRender(path);
    }

    public void stopRender() {
        if (mPdfView != null) {
            mPdfView.stopRender();
            mPdfView = null;
        }
    }
}
