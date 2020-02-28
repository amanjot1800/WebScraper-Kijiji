package logic;

import common.ValidationException;
import dal.ImageDAL;
import entity.Image;
import javax.persistence.NoResultException;
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

    public List<Image> getWithName(String name) {
            return get(() -> dao().findByName(name));
    }

    public List<Image> search(String search){
        return get(()->dao().findContaining(search));
    }

    @Override
    public Image createEntity(Map<String, String[]> parameterMap) {
        Image image = new Image();

        String name = parameterMap.get(NAME)[0];
        String path = parameterMap.get(PATH)[0];
        String url = parameterMap.get(URL)[0];

        if (parameterMap.containsKey(ID)) {
            String id = parameterMap.get(ID)[0];
            try {
                image.setId(Integer.parseInt(id));
            } catch (NumberFormatException ex) {
                throw new ValidationException("Id should be number");
            }
        }

        if (name==null || name.isEmpty()){
            throw new ValidationException("Name cannot be null");
        }
        else if (name.length()>255){
            throw new ValidationException("Name can only be 255 characters long");
        }
        else image.setName(name);

        if (path==null || path.isEmpty()){
            throw new ValidationException("Path cannot be null");
        }
        else if (path.length()>255){
            throw new ValidationException("Path cannot be more than 255 characters");
        }
        else image.setPath(path);


        if (url==null || url.isEmpty()){
            throw new ValidationException("url cannot be null");
        }
        else if (url.length()>255){
            throw new ValidationException("url cannot be more than 255 characters");
        }
        else image.setUrl(url);


        return image;
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
