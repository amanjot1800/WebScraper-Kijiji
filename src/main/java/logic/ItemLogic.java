package logic;

import dal.ItemDAL;
import entity.Item;
import org.apache.maven.surefire.shade.org.apache.commons.lang3.ObjectUtils;

import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class ItemLogic extends GenericLogic<Item, ItemDAL> {

    public static final String DESCRIPTION = "description";
    public static final String CATEGORY_ID = "categoryId";
    public static final String IMAGE_ID = "imageId";
    public static final String LOCATION = "location";
    public static final String PRICE = "price";
    public static final String TITLE = "title";
    public static final String DATE = "date";
    public static final String URL = "url";
    public static final String ID = "id";


    public ItemLogic() {
        super(new ItemDAL());
    }

    @Override
    public List<Item> getAll() {
        return get(()-> dao().findAll());
    }


    @Override
    public Item getWithId(int id) {
        return get(() -> dao().findById(id));
    }

    public List<Item> getWithPrice(BigDecimal price) {
        return get(()-> dao().findByPrice(price));
    }

    public List<Item> getWithTitle(String title) {
        try {
            return get(() -> dao().findByTitle(title));
        }
        catch (NoResultException nre){

        }
        return null;
    }

    public List<Item> getWithDate(String date) {
        return get(()-> dao().findByDate(date));
    }

    public List<Item> getWithLocation(String location) {
        return get(()-> dao().findByLocation(location));
    }

    public List<Item> getWithDescription(String description) {
        return get(()-> dao().findByDescription(description));
    }

    public Item getWithUrl(String url) {
        return get(()-> dao().findByUrl(url));
    }

    public List<Item> getWithCategory(int categoryId) {
        return get(()-> dao().findByCategory(categoryId));
    }

    public List<Item> search(String search) {
        return get(() -> dao().findContaining(search));
    }

    @Override
    public Item createEntity(Map<String, String[]> parameterMap) {
        Item item = new Item();

        if (!parameterMap.get(ID)[0].isEmpty()){
            item.setId(Integer.parseInt(parameterMap.get(ID)[0]));
        }

        item.setDescription(parameterMap.get(DESCRIPTION)[0]);
        item.setLocation(parameterMap.get(LOCATION)[0]);

        String price = parameterMap.get(PRICE)[0].replaceAll("[^.0-9]","");
        if (!price.equals("")){
            item.setPrice(new BigDecimal(price));
        }
        item.setTitle(parameterMap.get(TITLE)[0]);

        SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy");
        Date todaysDate = null;
        try {
            todaysDate = smf.parse(smf.format(new Date()));
            item.setDate(smf.parse(parameterMap.get(DATE)[0]));
        }
        catch (ParseException | NullPointerException ex){
            item.setDate(todaysDate);
        }

        item.setUrl(parameterMap.get(URL)[0]);
        item.setId(Integer.parseInt(parameterMap.get(ID)[0]));

        return item;
    }

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Image Id", "Category Id", "Price", "Title","Date", "Location", "Description", "Url");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, IMAGE_ID, CATEGORY_ID, PRICE, TITLE, DATE, LOCATION, DESCRIPTION, URL);
    }

    @Override
    public List<?> extractDataAsList(Item item) {
        return Arrays.asList(item.getId(), item.getImage().getId(), item.getCategory().getId(), item.getPrice(),
                item.getTitle(), item.getDate(), item.getLocation(), item.getDescription(), item.getUrl());
    }



}
