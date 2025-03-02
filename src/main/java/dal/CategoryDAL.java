package dal;

import entity.Category;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDAL extends GenericDAL<Category> {

    public CategoryDAL(){
        super(Category.class);
    }

    public List<Category> findAll(){

        return findResults( "Category.findAll", null);
    }

    public Category findById(int id){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return findResult( "Category.findById", map);
    }

    public Category findByUrl(String url){
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        return findResult( "Category.findByUrl", map);
    }

    public Category findByTitle(String title){
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        return findResult( "Category.findByTitle", map);
    }

    public List<Category> findContaining(String search) {
        Map<String, Object> map = new HashMap<>();
        map.put("search",search);
        return findResults("Category.findContaining",map);
    }
}
