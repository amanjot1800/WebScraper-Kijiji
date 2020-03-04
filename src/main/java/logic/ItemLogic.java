package logic;

import common.ValidationException;
import dal.ItemDAL;
import entity.Item;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
        return get(() -> dao().findById(id));
    }

    public List<Item> getWithPrice(BigDecimal price) {
        return get(()-> dao().findByPrice(price));
    }

    public List<Item> getWithTitle(String title) {
            return get(() -> dao().findByTitle(title));
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

        String id = parameterMap.get(ID)[0];
        String description = parameterMap.get(DESCRIPTION)[0];
        String location = parameterMap.get(LOCATION)[0];
        String title = parameterMap.get(TITLE)[0];
        String price = parameterMap.get(PRICE)[0].replaceAll("[^.0-9]","");
        String url = parameterMap.get(URL)[0];

        if (id==null || id.isEmpty()){
            throw new ValidationException("Id cannot be null");
        }
        else if (!id.matches("^[0-9]*$")) {
            throw new ValidationException("Id can only be Integer");
        }
        else item.setId(Integer.parseInt(id));

        if (description==null || description.isEmpty()){
            throw new ValidationException("Description cannot be null");
        }
        else if (description.length()>255){
            item.setDescription(description.substring(0, 255));
        }
        else item.setDescription(description);

        if(location.length()>45){
            throw new ValidationException("Location can only be 45 characters long");
        }
        else item.setLocation(location);

        
        if (!price.isEmpty()) {
            if (!price.matches("^([0-9]{1,13})([.])([0-9]{1,2})$")) {
                throw new ValidationException("Price is not valid");
            } item.setPrice(new BigDecimal(price));
        }

        if (title==null || title.isEmpty()){
            throw new ValidationException("Title cannot be null");
        }
        else if (title.length()>255){
            throw new ValidationException("Title cannot be more than 255 characters");
        }
        else item.setTitle(title);

        SimpleDateFormat smf = new SimpleDateFormat("dd/MM/yyyy");
        Date todaysDate = null;
        try {
            todaysDate = smf.parse(smf.format(new Date()));
            item.setDate(smf.parse(parameterMap.get(DATE)[0]));
        }
        catch (ParseException | NullPointerException ex){
            item.setDate(todaysDate);
        }

        if(url==null || url.isEmpty()){
            throw new ValidationException("url cannot be null");
        }
        else if (url.length()>255){
            throw new ValidationException("url can only be 255 characters long");
        }
        else item.setUrl(parameterMap.get(URL)[0]);

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
