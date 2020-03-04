package view;

import common.FileUtility;
import entity.Image;
import entity.Item;
import logic.CategoryLogic;
import logic.ImageLogic;
import logic.ItemLogic;
import scraper.kijiji.Kijiji;
import scraper.kijiji.KijijiItem;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Consumer;

@WebServlet(name = "Kijiji", urlPatterns = {"/Kijiji"})
public class KijijiView extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"style/KijijiStyle.css\">");
            out.println("<title>KijijiView</title>");
            out.println("</head>");
            out.println("<body>");

            ItemLogic logic = new ItemLogic();
            List<Item> entities = logic.getAll();

            for (Item item: entities) {

                out.println("<div class=\"center-column\">");
                out.println("<div class=\"item\">");
                out.println("<div class=\"image\">");
                out.println("<img src=\"image\\" + item.getImage().getPath() +"\" style=\"max-width: 250px; max-height: 200px;\" />");
                out.println("</div>");
                out.println("<div class=\"details\">");
                out.println("<div class=\"title\">");
                out.printf("<a href=\"%s\" target=\"_blank\">%s</a>", item.getUrl(), item.getTitle());
                out.println("</div>");
                out.println("<div class=\"price\">");
                BigDecimal price = item.getPrice();
                if (price==null){
                    out.println("Price: unavailable");
                }else {
                    out.println("Price: $" + price);
                }
                out.println("</div>");
                out.println("<div class=\"date\">");
                out.println("Date posted: " + item.getDate());
                out.println("</div>");
                out.println("<div class=\"location\">");
                out.println("Location: " + item.getLocation());
                out.println("</div>");
                out.println("<div class=\"description\">");
                out.println(item.getDescription());
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");
                out.println("</div>");
            }

            out.println("</body>");
            out.println("</html>");
        }
    }

    private String toStringMap(Map<String, String[]> m) {
        StringBuilder builder = new StringBuilder();
        for (String k : m.keySet()) {
            builder.append("Key=").append(k)
                    .append(", ")
                    .append("Value/s=").append(Arrays.toString(m.get(k)))
                    .append(System.lineSeparator());
        }
        return builder.toString();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Kijiji kijiji = new Kijiji();

        Map<String, String[]> itemMap = new HashMap<>();
        Map<String, String[]> imageMap = new HashMap<>();

        CategoryLogic categoryLogic = new CategoryLogic();
        ItemLogic itemLogic = new ItemLogic();
        ImageLogic imageLogic = new ImageLogic();

        Consumer<KijijiItem> saveItems = (KijijiItem item) -> {

            FileUtility.downloadAndSaveFile(item.getImageUrl(), System.getProperty("user.home") + "\\KijijiImages", item.getId() + ".jpg");

            String imagePath = System.getProperty("user.home") + "\\KijijiImages\\";

            imageMap.put(ImageLogic.NAME, new String[] {item.getImageName() });
            imageMap.put(ImageLogic.PATH, new String[] {imagePath + item.getId() + ".jpg"});
            imageMap.put(ImageLogic.URL, new String[] {item.getImageUrl()});
            Image image = imageLogic.createEntity(imageMap);

            if (imageLogic.getWithPath(imagePath + item.getId() + ".jpg")==null) {
                imageLogic.add(image);
            }

            itemMap.put(ItemLogic.DESCRIPTION, new String[]{item.getDescription()});
            itemMap.put(ItemLogic.LOCATION, new String[]{item.getLocation()});
            itemMap.put(ItemLogic.PRICE, new String[]{item.getPrice()});
            itemMap.put(ItemLogic.TITLE, new String[]{item.getTitle()});
            itemMap.put(ItemLogic.DATE, new String[]{item.getDate()});
            itemMap.put(ItemLogic.URL, new String[] {item.getUrl()});
            itemMap.put(ItemLogic.ID, new String[]{item.getId()});

            if (itemLogic.getWithId(Integer.parseInt(item.getId()))==null){
                Item newItem = itemLogic.createEntity(itemMap);
                newItem.setImage(image);
                newItem.setCategory(categoryLogic.getWithUrl(categoryLogic.getAll().get(0).getUrl()));
                itemLogic.add(newItem);
            }
        };

        kijiji.downloadPage(categoryLogic.getAll().get(0).getUrl());
        kijiji.findAllItems();
        kijiji.processItems(saveItems);

        log("GET");
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("POST");
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Image View Normal";
    }

    private static final boolean DEBUG = true;

    public void log(String msg) {
        if (DEBUG) {
            String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log(message);
        }
    }

    public void log(String msg, Throwable t) {
        String message = String.format("[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log(message, t);
    }

}