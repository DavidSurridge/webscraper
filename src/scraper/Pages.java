package scraper;

import java.util.Set;
import java.util.SortedSet;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Website")
public class Pages {

  SortedSet<String> links;

  @XmlElement(name = "URL")
  public Set<String> getLinks() {
    return links;
  }

  public void setLinks(SortedSet<String> links) {
    this.links = links;
  }

}
