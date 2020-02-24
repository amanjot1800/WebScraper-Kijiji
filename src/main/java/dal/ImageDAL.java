package dal;

import entity.Image;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageDAL extends GenericDAL<Image> {

    public ImageDAL(){
        super(Image.class);
    }

    public List<Image> findAll(){

        return findResults( "Image.findAll", null);
    }

    public Image findById(int id){
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        return findResult( "Image.findById", map);
    }

    public List<Image> findByUrl(String url){
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        return findResults( "Image.findByUrl", map);
    }

    public Image findByPath(String path){
        Map<String, Object> map = new HashMap<>();
        map.put("path", path);
        return findResult( "Image.findByPath", map);
    }

    public Image findByName(String name){
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        return findResult( "Image.findByName", map);
    }

    public List<Image> findContaining(String search) {
        Map<String, Object> map = new HashMap<>();
        map.put("search",search);
        return findResults("Image.findContaining",map);
    }


}
