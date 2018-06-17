package scraper;

import java.util.Set;

public class DoScrape {

  public static void main(String[] args) {
    WiproWebCrawler crawler = new WiproWebCrawler();
    Set<String> sitemap = crawler.startWebScrape("https://wiprodigital.com/");
    crawler.convertToXml(sitemap);
  }
}
