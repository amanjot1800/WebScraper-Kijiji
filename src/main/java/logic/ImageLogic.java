package logic;

import dal.ImageDAL;
import entity.Image;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ImageLogic extends GenericLogic<Image, ImageDAL> {


    public static final String PATH = "path";
    public static final String NAME = "name";
    public static final String URL = "url";
    public static final String ID = "id";


    public ImageLogic() {
        super(new ImageDAL());
    }


    @Override
    public List<Image> getAll() {
        return get(()-> dao().findAll());
    }

    @Override
    public Image getWithId(int id) {
        return get(()-> dao().findById(id));
    }

    public List<Image> getWithUrl(String url) {
        return get(()-> dao().findByUrl(url));
    }

    public Image getWithPath(String path) {
        return get(()-> dao().findByPath(path));
    }

    public Image getWithName(String name) {
        return get(()-> dao().findByName(name));
    }


//        public List<Image> search(String search) {
//        return get(()-> dao().find;
//    }


    @Override
    public Image createEntity(Map<String, String[]> requestData) {
        return null;
    }


    @Override
    public List<String> getColumnNames() {
        return Arrays.asList("ID", "Name", "Path", "Url");
    }

    @Override
    public List<String> getColumnCodes() {
        return Arrays.asList(ID, NAME, PATH, URL);
    }

    @Override
    public List<?> extractDataAsList(Image e) {
        return Arrays.asList(e.getId(), e.getName(), e.getPath(), e.getUrl());
    }



}
