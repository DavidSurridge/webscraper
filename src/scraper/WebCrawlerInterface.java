package scraper;

import java.util.Set;
import java.util.SortedSet;

public interface WebCrawlerInterface {

  public Set<String> startWebScrape(String url);

  public void convertToXml(Set<String> sitemap);

}
