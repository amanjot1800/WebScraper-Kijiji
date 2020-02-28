    package logic;

import common.TomcatStartUp;
import dal.EMFactory;
import entity.Account;
import entity.Category;
import entity.Image;
import entity.Item;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import javax.persistence.EntityManager;
import junit.framework.TestCase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

    public class ItemLogicTest extends TestCase {

    private ItemLogic logic;
    private static ImageLogic imageLogic;
    private static CategoryLogic categoryLogic;
    private static Category category;
    private Item expectedItem;
    private static Image image;
    private static EntityManager em;

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat();
        image = new Image();
        image.setName("ItemLogicTest - Image");
        image.setPath("ItemLogicTest - Path");
        image.setUrl("ItemLogicTest - URL");

        category = new Category();
        category.setTitle("ItemLogicTest - Title");
        category.setUrl("ItemLogicTest - URL");
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
       
      
        imageLogic.delete(image);
        categoryLogic.delete(category);
        
        em.close();
        
        TomcatStartUp.stopAndDestroyTomcat();
       
       
    }

    @BeforeEach
    protected final void setUp() throws Exception {
        imageLogic = new ImageLogic();
        categoryLogic = new CategoryLogic();

        em = EMFactory.getEMFactory().createEntityManager();
        em.getTransaction().begin();

        category = em.merge(category);
        image = em.merge(image);

        em.getTransaction().commit();

        Item item = new Item();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        item.setId(999999);
        item.setUrl("url");
        item.setDate(formatter.parse(formatter.format(new Date())));
        item.setDescription("description");
        item.setCategory(category);
        item.setImage(image);
        item.setLocation("location");
        item.setPrice(new BigDecimal("20.00"));
        item.setTitle("title");

     
        em.getTransaction().begin();
        expectedItem = em.merge(item);
        em.getTransaction().commit();

        logic = new ItemLogic();
    }

    @AfterEach
    @Override
    protected final void tearDown() throws Exception {

        if (expectedItem != null) {
            logic.delete(expectedItem);
        }
    }

    private void assertItemEquals(Item expected, Item actual) {

        //assert all field to guarantee they are the same
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getCategory(), actual.getCategory());
        assertEquals(expected.getUrl(), actual.getUrl());
        assertEquals(expected.getImage(), actual.getImage());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getLocation(), actual.getLocation());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getPrice().compareTo(actual.getPrice()),0);
    }

    @Test
    final void testGetAll() {
        //get all the accounts from the DB
        List<Item> list = logic.getAll();
        //store the size of list/ this way we know how many accounts exits in DB
        int originalSize = list.size();

        //delete the new account, so DB is reverted back to it original form
        logic.delete(expectedItem);

        //get all accounts again
        list = logic.getAll();
        //the new size of accounts must be same as original size
        assertEquals(originalSize - 1, list.size());
    }

    @Test
    final void testGetWithUrl() {
        Item returnedItem = logic.getWithUrl(expectedItem.getUrl());
        System.out.println(returnedItem.getDate()+"\t"+expectedItem.getDate());
        assertItemEquals(returnedItem, expectedItem);
    }

    @Test
    final void testGetWithId() {
        Item returnedItem = logic.getWithId(expectedItem.getId());
        assertItemEquals(returnedItem, expectedItem);
    }

    @Test
    final void testGetWithCategory() {
        List<Item> returnedList = logic.getWithCategory(expectedItem.getCategory().getId());
        for (Item itm : returnedList) {
            assertEquals(itm.getCategory(), expectedItem.getCategory());
        }
    }

    @Test
    final void testGetWithDescription() {
        int foundFull = 0;
        List<Item> returnedList = logic.getWithDescription(expectedItem.getDescription());
        for (Item itm : returnedList) {
            assertEquals(itm.getDescription(), expectedItem.getDescription());
            if (itm.getId().equals(expectedItem.getId())) {
                assertItemEquals(expectedItem, itm);
                foundFull++;
            }
        }
        assertEquals(1, foundFull);
    }

    @Test
    final void testGetWithPrice() {
        int foundFull = 0;
        
        List<Item> returnedList = logic.getWithPrice(expectedItem.getPrice());
        for (Item item : returnedList) {
            
            assertEquals(item.getPrice().compareTo(expectedItem.getPrice()),0);

            if (item.getId().equals(expectedItem.getId())) {
                assertItemEquals(expectedItem, item);
                foundFull++;
            }
        }
        assertEquals(1, foundFull);
    }


    @Test
    final void testGetWithTitle() {
        int foundFull = 0;
        List<Item> returnedList = logic.getWithTitle(expectedItem.getTitle());
        for (Item itm : returnedList) {
            assertEquals(itm.getTitle(), expectedItem.getTitle());
            if (itm.getId().equals(expectedItem.getId())) {
                assertItemEquals(expectedItem, itm);
                foundFull++;
            }
        }
        assertEquals(1, foundFull);
    }

    @Test
    final void testGetWithLocation() {
        int foundFull = 0;
        List<Item> returnedList = logic.getWithLocation(expectedItem.getLocation());
        for (Item itm : returnedList) {
            assertEquals(itm.getLocation(), expectedItem.getLocation());
            if (itm.getId().equals(expectedItem.getId())) {
                assertItemEquals(expectedItem, itm);
                foundFull++;
            }
        }
        assertEquals(1, foundFull);
    }

    @Test
    final void testGetWithDate() {
        List<Item> returnedList = logic.getWithDate(expectedItem.getDate().toString());
        for (Item itm : returnedList) {
            assertEquals(itm.getDate(), expectedItem.getDate());
        }
    }


    @Test
    final void testSearch() {
        String search = expectedItem.getTitle();
        List<Item> returnedList = logic.search(search);
        for (Item itm : returnedList) {
            assertTrue(itm.getTitle().contains(search)||itm.getDate().toString().contains(search)||itm.getPrice().toString().contains(search)
                    ||itm.getLocation().contains(search)||itm.getDescription().contains(search));
        }
    }

    @Test
    final void testCreateEntityAndAdd() {
        Map<String, String[]> sampleMap = new HashMap<>();

        sampleMap.put(ItemLogic.ID, new String[]{"1111"});
        sampleMap.put(ItemLogic.PRICE, new String[]{"19.00"});
        sampleMap.put(ItemLogic.TITLE, new String[]{"Test Item"});
        sampleMap.put(ItemLogic.DATE, new String[]{"2020-02-28"});
        sampleMap.put(ItemLogic.LOCATION, new String[]{"Java Island"});
        sampleMap.put(ItemLogic.DESCRIPTION, new String[]{"This is a test Item"});
        sampleMap.put(ItemLogic.URL, new String[]{"www.sampleTestItem.com"});

        Item returnedItem = logic.createEntity(sampleMap);
        returnedItem.setImage(image);
        returnedItem.setCategory(category);
        logic.add(returnedItem);

        returnedItem = logic.getWithUrl(returnedItem.getUrl());

        assertEquals(sampleMap.get(ItemLogic.ID)[0], returnedItem.getId().toString());
        assertEquals(image.getId(), returnedItem.getImage().getId());
        assertEquals(category.getId(), returnedItem.getCategory().getId());
        assertEquals(sampleMap.get(ItemLogic.PRICE)[0], returnedItem.getPrice().toString());
        assertEquals(sampleMap.get(ItemLogic.TITLE)[0], returnedItem.getTitle());
        assertEquals(sampleMap.get(ItemLogic.DATE)[0], returnedItem.getDate().toString());
        assertEquals(sampleMap.get(ItemLogic.LOCATION)[0], returnedItem.getLocation());
        assertEquals(sampleMap.get(ItemLogic.DESCRIPTION)[0], returnedItem.getDescription());
        assertEquals(sampleMap.get(ItemLogic.URL)[0], returnedItem.getUrl());

        logic.delete(returnedItem);

    }

    @Test
    final void testCreateEntity() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(ItemLogic.ID, new String[]{Integer.toString(expectedItem.getId())});
        sampleMap.put(ItemLogic.PRICE, new String[]{expectedItem.getPrice().toString()});
        sampleMap.put(ItemLogic.TITLE, new String[]{expectedItem.getTitle()});
        sampleMap.put(ItemLogic.DATE, new String[]{expectedItem.getDate().toString()});
        sampleMap.put(ItemLogic.LOCATION, new String[]{expectedItem.getLocation()});
        sampleMap.put(ItemLogic.DESCRIPTION, new String[]{expectedItem.getDescription()});
        sampleMap.put(ItemLogic.URL, new String[]{expectedItem.getUrl()});

        Item returnedItem = logic.createEntity(sampleMap);
        returnedItem.setCategory(expectedItem.getCategory());
        returnedItem.setImage(expectedItem.getImage());

        assertItemEquals(expectedItem, returnedItem);
    }


    @Test
    final void testCreateEntityNullAndEmptyValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            map.put(ItemLogic.ID, new String[]{Integer.toString(expectedItem.getId())});
            map.put(ItemLogic.PRICE, new String[]{expectedItem.getPrice().toString()});
            map.put(ItemLogic.TITLE, new String[]{expectedItem.getTitle()});
            map.put(ItemLogic.DATE, new String[]{expectedItem.getDate().toString()});
            map.put(ItemLogic.LOCATION, new String[]{expectedItem.getLocation()});
            map.put(ItemLogic.DESCRIPTION, new String[]{expectedItem.getDescription()});
            map.put(ItemLogic.URL, new String[]{expectedItem.getUrl()});
        };

        fillMap.accept(sampleMap);
        sampleMap.replace(ItemLogic.ID, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ItemLogic.ID, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ItemLogic.PRICE, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ItemLogic.PRICE, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ItemLogic.TITLE, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ItemLogic.TITLE, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ItemLogic.DATE, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ItemLogic.DATE, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ItemLogic.LOCATION, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ItemLogic.LOCATION, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ItemLogic.DESCRIPTION, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ItemLogic.DESCRIPTION, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ItemLogic.URL, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ItemLogic.URL, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));
    }







    @Test
    final void testGetColumnNames() {
        List<String> list = Arrays.asList("ID", "Image Id", "Category Id", "Price", "Title","Date", "Location", "Description", "Url");
        assertEquals(list, logic.getColumnNames());
    }

    @Test
    final void testGetColumnCodes() {
        List<String> list = Arrays.asList(ItemLogic.ID, ItemLogic.IMAGE_ID,
                ItemLogic.CATEGORY_ID, ItemLogic.PRICE, ItemLogic.TITLE, ItemLogic.DATE, ItemLogic.LOCATION, ItemLogic.DESCRIPTION, ItemLogic.URL);
        assertEquals(list, logic.getColumnCodes());
    }

    @Test
    final void testExtractDataAsList() {
        Item item = logic.getAll().get(0);
        assertEquals(Arrays.asList(item.getId(), item.getImage().getId(), item.getCategory().getId(), item.getPrice(),
                item.getTitle(), item.getDate(), item.getLocation(), item.getDescription(), item.getUrl()), logic.extractDataAsList(item));
    }
}