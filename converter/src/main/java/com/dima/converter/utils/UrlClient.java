package com.dima.converter.utils;

import java.io.IOException;
import java.io.InputStream;

public interface UrlClient {
    InputStream getResponse(String url) throws IOException;
}
