package scraper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WiproWebCrawler implements WebCrawlerInterface {

    private static Set<String> visited = new HashSet<>();
    private static Set<String> setOfLinks = new HashSet<>();

    @Override
    public Set<String> startWebScrape(String url) {

        runRecursive(url);
        return setOfLinks;

    }

    @Override
    public void convertToXml(Set<String> sitemap) {
        SortedSet<String> finalSitemap = finalValidation(sitemap);

        try {
            JAXBContext contextObj = JAXBContext.newInstance(Pages.class);
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Pages website = new Pages();
            website.setLinks(finalSitemap);
            marshallerObj.marshal(website, new FileOutputStream("sitemap.xml"));
            marshallerObj.marshal(website, System.out);
        } catch (JAXBException | FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void runner(String url) {

        try {

            LinkedList<String> queue = new LinkedList<>();
            queue.add(url);
            while (!queue.isEmpty()) {

                String nextUrl = queue.pollFirst();

                if (!visited.contains(nextUrl)) {
                    visited.add(nextUrl);

                    Document response = Jsoup.connect(nextUrl).get();

                    List<String> urlLinks = parseResponnseToUrlList(response);
                    List<String> srcLink = parseResponnseToResourceList(response);
                    setOfLinks.addAll(urlLinks);
                    setOfLinks.addAll(srcLink);
                    for (String link : urlLinks) {
                        if (internalUrlValidation(link) && !visited.contains(link)) {
                            queue.add(link);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void runRecursive(String url) {
        visited.add(url);

        try {
            Document response = Jsoup.connect(url).get();

            List<String> urlLinks = parseResponnseToUrlList(response);
            List<String> srcLink = parseResponnseToResourceList(response);
            setOfLinks.addAll(urlLinks);
            setOfLinks.addAll(srcLink);

            for (String link : urlLinks) {
                if (internalUrlValidation(link)) {
                    runRecursive(link);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> parseResponnseToUrlList(Document response) {
        List<String> links = response.body().getElementsByAttribute("href").eachAttr("href");
        List<String> linkUrl = response.body().getElementsByClass("linkURL").eachAttr("value");
        if (!linkUrl.isEmpty()) {
            links.addAll(linkUrl);
        }
        return links;
    }

    private static List<String> parseResponnseToResourceList(Document response) {
        List<String> src = response.body().getElementsByAttribute("src").eachAttr("src");

        List<String> resources = response.body().getElementsByAttributeValueContaining("style", "background")
                .eachAttr("style");
        if (!resources.isEmpty()) {
            resources.replaceAll(resource -> resource.replaceAll("(background: url\\((\"|\'|))", ""));
            resources.replaceAll(resource -> resource.replaceAll("((\"|\'|)\\) center center;)", ""));
            src.addAll(resources);
        }
        return src;
    }

    private static boolean internalUrlValidation(String ref) {

        return (!ref.isEmpty() && ref.startsWith("https://wiprodigital.com/") && !visited.contains(ref)
                && !ref.contains("#") && !ref.contains("?"));
    }

    private static SortedSet<String> finalValidation(Set<String> setOfLinks) {
        setOfLinks.removeIf(s -> !s.startsWith("http"));
        SortedSet<String> set = new TreeSet<>();
        set.addAll(setOfLinks);
        return set;
    }
}
