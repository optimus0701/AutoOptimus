package com.optimus.auto.task;

import java.io.IOException;

public interface OnDownloadCompleted {
    void onDownloadCompleted(String source) throws IOException;
}
