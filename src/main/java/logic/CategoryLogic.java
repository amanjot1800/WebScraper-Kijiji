package logic;

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
        if (parameterMap.containsKey(ID)){
            category.setId(Integer.parseInt(parameterMap.get(ID)[0]));
        }

        category.setTitle(parameterMap.get(TITLE)[0]);
        category.setUrl(parameterMap.get(URL)[0]);

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
