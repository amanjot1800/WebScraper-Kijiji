package scraper.kijiji;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ItemBuilder {
    
    private static final String URL_BASE = "https://www.kijiji.ca";
    private static final String ATTRIBUTE_ID = "data-listing-id";
    private static final String ATTRIBUTE_IMAGE = "image";
    private static final String ATTRIBUTE_IMAGE_SRC = "data-src";
    private static final String ATTRIBUTE_IMAGE_NAME = "alt";
    private static final String ATTRIBUTE_PRICE = "price";
    private static final String ATTRIBUTE_TITLE = "title";
    private static final String ATTRIBUTE_LOCATION = "location";
    private static final String ATTRIBUTE_DATE = "date-posted";
    private static final String ATTRIBUTE_DESCRIPTION = "description";

    private Element element;
    private KijijiItem item;


    ItemBuilder(){
        item = new KijijiItem();
    }

    public ItemBuilder setElement(Element elements){
        this.element = elements;
        return this;
    }

    public KijijiItem build(){
        item.setId(element.attr(ATTRIBUTE_ID).trim());

        Elements elements = element.getElementsByClass(ATTRIBUTE_IMAGE);
        String image = elements.get(0).child(0).attr(ATTRIBUTE_IMAGE_SRC).trim();
        if (image.isEmpty()) {
            image = elements.get(0).child(0).attr("src").trim();
            if (image.isEmpty()) {
                image = elements.get(0).child(0).child(1).attr(ATTRIBUTE_IMAGE_SRC).trim();
            }
        }
        item.setImageUrl(image);

        String imageName = elements.get(0).child(0).attr(ATTRIBUTE_IMAGE_NAME).trim();
        if (imageName.isEmpty()) {
            imageName = elements.get(0).child(0).child(1).attr(ATTRIBUTE_IMAGE_NAME).trim();

        }
        item.setImageName(imageName);

       elements = element.getElementsByClass(ATTRIBUTE_PRICE);
        if (!elements.isEmpty()){
            item.setPrice(elements.get(0).text().trim());
        }

        item.setUrl(URL_BASE + element.getElementsByClass(ATTRIBUTE_TITLE).get(0).child(0).attr("href").trim());
        item.setTitle(element.getElementsByClass(ATTRIBUTE_TITLE).get(0).child(0).text().trim());

        elements = element.getElementsByClass(ATTRIBUTE_DATE);
        if (!elements.isEmpty()){
            item.setDate(elements.get(0).text().trim());
        }

        elements = element.getElementsByClass(ATTRIBUTE_LOCATION);
        if (!elements.isEmpty()){
            item.setLocation(elements.get(0).childNode(0).outerHtml().trim());
        }

        item.setDescription(element.getElementsByClass(ATTRIBUTE_DESCRIPTION).get(0).text().trim());

        return item;
    }
    
}
