/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import common.TomcatStartUp;
import common.ValidationException;
import dal.EMFactory;
import entity.Category;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryLogicTest {

    private CategoryLogic logic;
    private Category expectedCategory;

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat();
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
        TomcatStartUp.stopAndDestroyTomcat();
    }

    @BeforeEach
    final void setUp() throws Exception {

        Category category = new Category();
        category.setTitle("Sample Category");
        category.setUrl("www.sampleCategoryTest.com");

        EntityManager em = EMFactory.getEMFactory().createEntityManager();

        em.getTransaction().begin();

        expectedCategory = em.merge(category);

        em.getTransaction().commit();

        em.close();

        logic = new CategoryLogic();
    }

    @AfterEach
    final void tearDown() throws Exception {
        if (expectedCategory != null) {
            logic.delete(expectedCategory);
        }
    }

    @Test
    final void testGetAll() {

        List<Category> list = logic.getAll();

        int originalSize = list.size();

        assertNotNull(expectedCategory);

        logic.delete(expectedCategory);

        list = logic.getAll();

        assertEquals(originalSize - 1, list.size());
    }

    private void assertCategoryEquals(Category expected, Category actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTitle(), actual.getTitle());
        assertEquals(expected.getUrl(), actual.getUrl());

    }

    @Test
    final void testGetWithId() {
        Category returnedCategory = logic.getWithId(expectedCategory.getId());

        assertCategoryEquals(expectedCategory, returnedCategory);
    }

    @Test
    final void testGetCategoryWithTitle() {
        Category returnedCategory = logic.getWithTitle(expectedCategory.getTitle());
        assertCategoryEquals(expectedCategory, returnedCategory);
    }

    @Test
    final void testGetCategoryWithUrl() {
        Category returnedCategory = logic.getWithUrl(expectedCategory.getUrl());
        assertCategoryEquals(expectedCategory, returnedCategory);
    }

    @Test
    final void testSearch() {
        int foundFull = 0;

        String searchString = expectedCategory.getTitle().substring(3);

        List<Category> returnedCategories = logic.search(searchString);

        for (Category category : returnedCategories) {

            assertTrue(category.getTitle().contains(searchString));

            if (category.getId().equals(expectedCategory.getId())) {
                assertCategoryEquals(expectedCategory, category);
                foundFull++;
            }
        }
        assertEquals(1, foundFull, "if zero means not found, if more than one means duplicate");
    }

    @Test
    final void testCreateEntityAndAdd() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(CategoryLogic.TITLE, new String[]{"Test Create Category"});
        sampleMap.put(CategoryLogic.URL, new String[]{"www.testCreateCategory.com"});

        Category returnedCategory = logic.createEntity(sampleMap);
        logic.add(returnedCategory);

        returnedCategory = logic.getWithTitle(returnedCategory.getTitle());

        assertEquals(sampleMap.get(CategoryLogic.TITLE)[0], returnedCategory.getTitle());
        assertEquals(sampleMap.get(CategoryLogic.URL)[0], returnedCategory.getUrl());

        logic.delete(returnedCategory);
    }

    @Test
    final void testCreateEntity() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(CategoryLogic.ID, new String[]{Integer.toString(expectedCategory.getId())});
        sampleMap.put(CategoryLogic.TITLE, new String[]{expectedCategory.getTitle()});
        sampleMap.put(CategoryLogic.URL, new String[]{expectedCategory.getUrl()});

        Category returnedCategory = logic.createEntity(sampleMap);

        assertCategoryEquals(expectedCategory, returnedCategory);
    }


    @Test
    final void testCreateEntityNullAndEmptyValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            map.put(CategoryLogic.ID, new String[]{Integer.toString(expectedCategory.getId())});
            map.put(CategoryLogic.TITLE, new String[]{expectedCategory.getTitle()});
            map.put(CategoryLogic.URL, new String[]{expectedCategory.getUrl()});
        };

        fillMap.accept(sampleMap);
        sampleMap.replace(CategoryLogic.ID, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(CategoryLogic.ID, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(CategoryLogic.TITLE, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(CategoryLogic.TITLE, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(CategoryLogic.URL, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(CategoryLogic.URL, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));
    }


    @Test
    final void testCreateEntityBadLengthValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            map.put(CategoryLogic.ID, new String[]{Integer.toString(expectedCategory.getId())});
            map.put(CategoryLogic.TITLE, new String[]{expectedCategory.getTitle()});
            map.put(CategoryLogic.URL, new String[]{expectedCategory.getUrl()});

        };

        IntFunction<String> generateString = (int length) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints('a', 'z' + 1).limit(length)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        };

        //idealy every test should be in its own method
        fillMap.accept(sampleMap);
        sampleMap.replace(CategoryLogic.ID, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(CategoryLogic.ID, new String[]{"12b"});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(CategoryLogic.TITLE, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(CategoryLogic.TITLE, new String[]{generateString.apply(256)});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(CategoryLogic.URL, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(CategoryLogic.URL, new String[]{generateString.apply(256)});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));

    }


    @Test
    final void testCreateEntityEdgeValues() {
        IntFunction<String> generateString = (int length) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints('a', 'z' + 1).limit(length)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        };

        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(CategoryLogic.ID, new String[]{Integer.toString(1)});
        sampleMap.put(CategoryLogic.TITLE, new String[]{generateString.apply(1)});
        sampleMap.put(CategoryLogic.URL, new String[]{generateString.apply(1)});

        Category returnedCategory = logic.createEntity(sampleMap);
        assertEquals(Integer.parseInt(sampleMap.get(CategoryLogic.ID)[0]), returnedCategory.getId());
        assertEquals(sampleMap.get(CategoryLogic.TITLE)[0], returnedCategory.getTitle());
        assertEquals(sampleMap.get(CategoryLogic.URL)[0], returnedCategory.getUrl());

        sampleMap = new HashMap<>();
        sampleMap.put(CategoryLogic.ID, new String[]{Integer.toString(1)});
        sampleMap.put(CategoryLogic.TITLE, new String[]{generateString.apply(255)});
        sampleMap.put(CategoryLogic.URL, new String[]{generateString.apply(255)});

        returnedCategory = logic.createEntity(sampleMap);
        assertEquals(Integer.parseInt(sampleMap.get(CategoryLogic.ID)[0]), returnedCategory.getId());
        assertEquals(sampleMap.get(CategoryLogic.TITLE)[0], returnedCategory.getTitle());
        assertEquals(sampleMap.get(CategoryLogic.URL)[0], returnedCategory.getUrl());

    }

    @Test
    final void testGetColumnNames() {
        List<String> list = logic.getColumnNames();
        assertEquals(Arrays.asList("ID", "Title", "URL"), list);
    }

    @Test
    final void testGetColumnCodes() {
        List<String> list = logic.getColumnCodes();
        assertEquals(Arrays.asList(CategoryLogic.ID, CategoryLogic.TITLE, CategoryLogic.URL), list);
    }

    @Test
    final void testExtractDataAsList() {
        List<?> list = logic.extractDataAsList(expectedCategory);
        assertEquals(expectedCategory.getId(), list.get(0));
        assertEquals(expectedCategory.getTitle(), list.get(1));
        assertEquals(expectedCategory.getUrl(), list.get(2));
    }

}