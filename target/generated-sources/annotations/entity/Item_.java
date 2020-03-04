package entity;

import entity.Category;
import entity.Image;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-03-02T20:58:00")
@StaticMetamodel(Item.class)
public class Item_ { 

    public static volatile SingularAttribute<Item, Date> date;
    public static volatile SingularAttribute<Item, Image> image;
    public static volatile SingularAttribute<Item, BigDecimal> price;
    public static volatile SingularAttribute<Item, String> description;
    public static volatile SingularAttribute<Item, String> location;
    public static volatile SingularAttribute<Item, Integer> id;
    public static volatile SingularAttribute<Item, String> title;
    public static volatile SingularAttribute<Item, Category> category;
    public static volatile SingularAttribute<Item, String> url;

}