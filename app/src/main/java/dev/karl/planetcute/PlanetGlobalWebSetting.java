package dev.karl.planetcute;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class PlanetGlobalWebSetting extends WebView {
    public PlanetGlobalWebSetting(Context context) {
        super(context);
        initWebViewSettings();
    }

    public PlanetGlobalWebSetting(Context context, AttributeSet attrs) {
        super(context, attrs);
        initWebViewSettings();
    }

    public PlanetGlobalWebSetting(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initWebViewSettings();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebViewSettings() {
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setSupportMultipleWindows(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        addJavascriptInterface(new PlanetJSScript(getContext()), "jsBridge");

        setWebViewClient(new CustomWebClient());
    }

    private static class CustomWebClient extends WebViewClient {
        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            new Handler().postDelayed(() ->
                    view.evaluateJavascript(
                            "(function() { " +
                                    "   if(document.getElementById('pngPreloaderWrapper')) {" +
                                    "       document.getElementById('pngPreloaderWrapper').removeChild(document.getElementById('pngLogoWrapper')); " +
                                    "   }" +
                                    "})();",
                            null), 600);

            new Handler().postDelayed(() ->
                    view.evaluateJavascript(
                            "(function() { " +
                                    "   var myHome = document.getElementById('lobbyButtonWrapper'); " +
                                    "   if(document.getElementById('lobbyButtonWrapper')) {" +
                                    "       document.getElementById('lobbyButtonWrapper').style.display = 'none';" +
                                    "   }" +
                                    "});",
                            null), 5000);

            // Remove an element by its id
            String removeElementCode = "(function() { " +
                    "   var elementToRemove = document.getElementById('suggest-download-h5_top');" +
                    "   if (elementToRemove) {" +
                    "       elementToRemove.parentNode.removeChild(elementToRemove);" +
                    "   }" +
                    "})();";

            view.evaluateJavascript(removeElementCode, null);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            new Handler().postDelayed(() ->
                    view.evaluateJavascript(
                            "(function() { document.getElementById('suggest-download-h5_top').innerHTML = ''; document.getElementById('headerWrap').style = 'position:fixed; top:0px;';})();",
                            null), 1000);
        }
    }
}
