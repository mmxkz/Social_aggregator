package org.susu.sa.soc.vk;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.perm.kate.api.Auth;

public abstract class VKWebViewClient extends WebViewClient {
    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

        try {
        if (url.startsWith(Auth.redirect_url)) {
                if (!url.contains("error=")) {
                    String[] auth = Auth.parseRedirectUrl(url);
                    onAuthSuccess(auth[0], Long.parseLong(auth[1]));
                } else
                    throw new Exception("Error in url");
            }
        } catch (Exception e) {
            onAuthFailure(e);
        }
    }

    public abstract void onAuthSuccess(String accessToken, long userId);
    public abstract void onAuthFailure(Exception e);
}
