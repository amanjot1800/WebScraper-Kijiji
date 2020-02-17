package logic;

import dal.ItemDAL;
import entity.Item;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
        return get(()-> dao().findById(id));
    }

    public List<Item> getWithPrice(String price) {
        return get(()-> dao().findByPrice(price));
    }

    public List<Item> getWithTitle(String title) {
        return get(()-> dao().findByTitle(title));
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

    public List<Item> getWithCategory(String categoryId) {
        return get(()-> dao().findByCategory(categoryId));
    }

//    public List<Item> search(String search) {
//        return get(()-> dao().find;
//    }

    @Override
    public Item createEntity(Map<String, String[]> parameterMap) {
        Item item = new Item();
        if (parameterMap.containsKey(ID)){
            item.setId(Integer.parseInt(parameterMap.get(ID)[0]));
        }

        item.setTitle(parameterMap.get(TITLE)[0]);

        SimpleDateFormat smf = new SimpleDateFormat("dd-MM-yyyy");
        smf.parse("0545455");


        return null;
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
        return null;
    }



}
