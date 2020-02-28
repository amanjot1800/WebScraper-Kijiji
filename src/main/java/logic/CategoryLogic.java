package logic;

import common.ValidationException;
import dal.CategoryDAL;
import entity.Category;
import entity.Item;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CategoryLogic extends GenericLogic<Category,CategoryDAL> {


    public static final String TITLE = "title";
    public static final String URL = "url";
    public static final String ID = "id";


    public CategoryLogic() {
        super(new CategoryDAL());
    }


    @Override
    public List<Category> getAll() {
        return get(()-> dao().findAll());
    }

    @Override
    public Category getWithId(int id) {
        return get(()-> dao().findById(id));
    }

    public Category getWithTitle(String title) {
        return get(()-> dao().findByTitle(title));
    }

    public Category getWithUrl(String url) {
        return get(()-> dao().findByUrl(url));
    }

    public List<Category> search(String search){
        return get(()->dao().findContaining(search));
    }

    @Override
    public Category createEntity(Map<String, String[]> parameterMap) {

        Category category = new Category();
        String title = parameterMap.get(TITLE)[0];
        String url = parameterMap.get(URL)[0];

        if (parameterMap.containsKey(ID)) {
            String id = parameterMap.get(ID)[0];
            try {
                category.setId(Integer.parseInt(id));
            } catch (NumberFormatException ex) {
                throw new ValidationException("Id should be number");
            }
        }

        if (title==null || title.isEmpty()){
            throw new ValidationException("Title cannot be null");
        }
        else if (title.length()>255){
            throw new ValidationException("Title can only be 255 characters long");
        }
        else category.setTitle(title);

        if (url==null || url.isEmpty()){
            throw new ValidationException("url cannot be null");
        }
        else if (url.length()>255){
            throw new ValidationException("url cannot be more than 255 characters");
        }
        else category.setUrl(url);

        return category;
    }

    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Title", "URL");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, TITLE, URL);
    }

    @Override
    public List<?> extractDataAsList(Category e) {
        return Arrays.asList(e.getId(), e.getTitle(), e.getUrl());
    }

}
