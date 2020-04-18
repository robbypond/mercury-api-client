package com.robbypond.mercury.util;

import com.googlecode.jatl.Html;
import com.robbypond.mercury.data.MercuryArticle;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

public class HtmlGenerator {

    private static final List<String> badImages = Collections.singletonList("http://analytics.apnewsregistry.com/analytics/v2/image.svc/AP/RWS/hosted.ap.org/MAI/V2227-2015-12-09T1807Z/E/prod/AT/A");

    public String generateHtml(MercuryArticle article) {
        StringWriter stringWriter = new StringWriter();
        new ArticleHtml(stringWriter).generateHtml(article);
        return stringWriter.getBuffer().toString();
    }

    private static class ArticleHtml extends Html {

        public ArticleHtml(Writer writer) {
            super(writer);
        }

        public void generateHtml(MercuryArticle article) {
            html().xmlns("http://www.w3.org/1999/xhtml");
            head();
            meta().charset("UTF-8");
            meta().httpEquiv("X-UA-Compatible").content("IE=edge");
            meta().name("viewport").content("width=device-width, initial-scale=1");
            title().text(article.getTitle()).end();
            link().rel("stylesheet").href("https://cdnjs.cloudflare.com/ajax/libs/materialize/0.99.0/css/materialize.min.css").end();
            end();
            body();
            div().classAttr("container");
            h5().id("title").text(article.getTitle()).end();
            hr();
            br();
            writeText(article);
            endAll();
            done();
        }

        private void writeText(MercuryArticle article) {
            div().id("content").raw(processImageTags(article.getContent())).end();
        }

        private String processImageTags(String text) {
            Document doc = Jsoup.parse(text);
            Elements images = doc.select("img");
            for(Element el : images) {
                String src = el.attr("src");
                if(badImages.contains(src)) {
                    el.remove();
                    continue;
                }
                el.addClass("responsive-img");
            }
            return doc.body().html();
        }
    }
}

