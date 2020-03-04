package logic;

import common.TomcatStartUp;
import common.ValidationException;
import dal.EMFactory;
import entity.Account;
import entity.Category;
import entity.Image;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import javax.persistence.EntityManager;
import junit.framework.TestCase;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ImageLogicTest extends TestCase {

    private ImageLogic logic;
    private Image expectedImage;

    @BeforeAll
    final static void setUpBeforeClass() throws Exception {
        TomcatStartUp.createTomcat();
    }

    @AfterAll
    final static void tearDownAfterClass() throws Exception {
     //   TomcatStartUp.stopAndDestroyTomcat();
    }

    @BeforeEach
    protected final void setUp() throws Exception {

        Image image = new Image();
        image.setName("Sample Image");
        image.setPath("C:\\Path\\to\\the\\sample\\image.jpg");
        image.setUrl("www.sampleImageTest.com");

        EntityManager em = EMFactory.getEMFactory().createEntityManager();

        em.getTransaction().begin();

        expectedImage = em.merge(image);

        em.getTransaction().commit();

        em.close();

        logic = new ImageLogic();
    }

    @AfterEach
    @Override
    protected final void tearDown() throws Exception {
        if (expectedImage != null) {
            logic.delete(expectedImage);
        }
    }

    @Test
    final void testGetAll() {
        List<Image> list = logic.getAll();
        int originalSize = list.size();
        logic.delete(expectedImage);
        list = logic.getAll();
        assertEquals(originalSize - 1, list.size());
    }

    private void assertImageEquals(Image expected, Image actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getUrl(), actual.getUrl());
        assertEquals(expected.getPath(), actual.getPath());
    }

    @Test
    final void testGetWithId() {
        Image returnedImage = logic.getWithId(expectedImage.getId());
        assertImageEquals(expectedImage, returnedImage);
    }

    @Test
    final void testGetWithPath() {
        Image returnedImage = logic.getWithPath(expectedImage.getPath());
        assertImageEquals(expectedImage, returnedImage);
    }

    @Test
    final void testGetWithUrl() {
        int foundFull = 0;
        List<Image> returnedList = logic.getWithUrl(expectedImage.getUrl());
        for (Image image : returnedList) {
            assertEquals(image.getUrl(), expectedImage.getUrl());

            if (image.getId().equals(expectedImage.getId())) {
                assertImageEquals(expectedImage, image);
                foundFull++;
            }
        }
        assertEquals(1, foundFull);
    }

    @Test
    final void testGetWithName() {
        int foundFull = 0;
        List<Image> returnedList = logic.getWithName(expectedImage.getName());
        for (Image image : returnedList) {
            assertEquals(image.getName(), expectedImage.getName());

            if (image.getId().equals(expectedImage.getId())) {
                assertImageEquals(expectedImage, image);
                foundFull++;
            }
        }
        assertEquals(1, foundFull);
    }


    @Test
    final void testSearch() {
        int foundFull = 0;
        String search = expectedImage.getName();
        List<Image> returnedList = logic.search(search);
        for (Image image : returnedList) {

            assertTrue(image.getName().contains(search));

            if (image.getId().equals(expectedImage.getId())) {
                assertImageEquals(expectedImage, image);
                foundFull++;
            }
        }
        assertEquals(1, foundFull);
    }

    @Test
    final void testCreateEntityAndAdd() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(ImageLogic.NAME, new String[]{"ImageLogicTest - Name"});
        sampleMap.put(ImageLogic.PATH, new String[]{"ImageLogicTest - Path"});
        sampleMap.put(ImageLogic.URL, new String[]{"ImageLogicTest - Url"});

        Image returnedImage = logic.createEntity(sampleMap);
        logic.add(returnedImage);

        returnedImage = logic.getWithPath(returnedImage.getPath());

        assertEquals(sampleMap.get(ImageLogic.NAME)[0], returnedImage.getName());
        assertEquals(sampleMap.get(ImageLogic.PATH)[0], returnedImage.getPath());
        assertEquals(sampleMap.get(ImageLogic.URL)[0], returnedImage.getUrl());

        logic.delete(returnedImage);
    }


    @Test
    final void testCreateEntity() {
        Map<String, String[]> sampleMap = new HashMap<>();
        sampleMap.put(ImageLogic.ID, new String[]{Integer.toString(expectedImage.getId())});
        sampleMap.put(ImageLogic.NAME, new String[]{expectedImage.getName()});
        sampleMap.put(ImageLogic.PATH, new String[]{expectedImage.getPath()});
        sampleMap.put(ImageLogic.URL, new String[]{expectedImage.getUrl()});

        Image returnedImage = logic.createEntity(sampleMap);

        assertImageEquals(expectedImage, returnedImage);
    }

    @Test
    final void testCreateEntityNullAndEmptyValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            map.put(ImageLogic.ID, new String[]{Integer.toString(expectedImage.getId())});
            map.put(ImageLogic.NAME, new String[]{expectedImage.getName()});
            map.put(ImageLogic.PATH, new String[]{expectedImage.getPath()});
            map.put(ImageLogic.URL, new String[]{expectedImage.getUrl()});
        };

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.ID, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.ID, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.NAME, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.NAME, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.PATH, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.PATH, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.URL, null);
        assertThrows(NullPointerException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.URL, new String[]{});
        assertThrows(IndexOutOfBoundsException.class, () -> logic.createEntity(sampleMap));
    }

    @Test
    final void testCreateEntityBadLengthValues() {
        Map<String, String[]> sampleMap = new HashMap<>();
        Consumer<Map<String, String[]>> fillMap = (Map<String, String[]> map) -> {
            map.clear();
            map.put(ImageLogic.ID, new String[]{Integer.toString(expectedImage.getId())});
            map.put(ImageLogic.NAME, new String[]{expectedImage.getName()});
            map.put(ImageLogic.PATH, new String[]{expectedImage.getPath()});
            map.put(ImageLogic.URL, new String[]{expectedImage.getUrl()});
        };

        IntFunction<String> generateString = (int length) -> {
            //https://www.baeldung.com/java-random-string#java8-alphabetic
            return new Random().ints('a', 'z' + 1).limit(length)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                    .toString();
        };

        //idealy every test should be in its own method
        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.ID, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.ID, new String[]{"12b"});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.NAME, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.NAME, new String[]{generateString.apply(256)});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.PATH, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.PATH, new String[]{generateString.apply(256)});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));

        fillMap.accept(sampleMap);
        sampleMap.replace(ImageLogic.URL, new String[]{""});
        assertThrows(ValidationException.class, () -> logic.createEntity(sampleMap));
        sampleMap.replace(ImageLogic.URL, new String[]{generateString.apply(256)});
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
        sampleMap.put(ImageLogic.ID, new String[]{Integer.toString(1)});
        sampleMap.put(ImageLogic.NAME, new String[]{generateString.apply(1)});
        sampleMap.put(ImageLogic.PATH, new String[]{generateString.apply(1)});
        sampleMap.put(ImageLogic.URL, new String[]{generateString.apply(1)});


        Image returnedImage = logic.createEntity(sampleMap);
        assertEquals(Integer.parseInt(sampleMap.get(ImageLogic.ID)[0]), (int) returnedImage.getId());
        assertEquals(sampleMap.get(ImageLogic.NAME)[0], returnedImage.getName());
        assertEquals(sampleMap.get(ImageLogic.PATH)[0], returnedImage.getPath());
        assertEquals(sampleMap.get(ImageLogic.URL)[0], returnedImage.getUrl());

        sampleMap = new HashMap<>();
        sampleMap.put(ImageLogic.ID, new String[]{Integer.toString(1)});
        sampleMap.put(ImageLogic.NAME, new String[]{generateString.apply(255)});
        sampleMap.put(ImageLogic.PATH, new String[]{generateString.apply(255)});
        sampleMap.put(ImageLogic.URL, new String[]{generateString.apply(255)});

        returnedImage = logic.createEntity(sampleMap);
        assertEquals(Integer.parseInt(sampleMap.get(ImageLogic.ID)[0]), (int) returnedImage.getId());
        assertEquals(sampleMap.get(ImageLogic.NAME)[0], returnedImage.getName());
        assertEquals(sampleMap.get(ImageLogic.PATH)[0], returnedImage.getPath());
        assertEquals(sampleMap.get(ImageLogic.URL)[0], returnedImage.getUrl());
    }


    @Test
    final void testGetColumnNames() {
        List<String> list = Arrays.asList("ID", "Name", "Path", "Url");
        assertEquals(list, logic.getColumnNames());
    }

    @Test
    final void testGetColumnCodes() {
        List<String> list = Arrays.asList(ImageLogic.ID, ImageLogic.NAME, ImageLogic.PATH, ImageLogic.URL);
        assertEquals(list, logic.getColumnCodes());
    }

    @Test
    final void testExtractDataAsList() {
        Image e = logic.getAll().get(0);
        assertEquals(Arrays.asList(e.getId(), e.getName(), e.getPath(), e.getUrl()), logic.extractDataAsList(e));
    }
    
}