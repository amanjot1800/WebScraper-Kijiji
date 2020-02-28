package view;

import common.ValidationException;
import entity.Category;
import entity.Image;
import entity.Item;
import logic.CategoryLogic;
import logic.ImageLogic;
import logic.ItemLogic;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "CreateItem", urlPatterns = {"/CreateItem"})
public class CreateItem extends HttpServlet {

    private String errorMessage = null;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Create Feed</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div style=\"text-align: center;\">");
            out.println("<div style=\"display: inline-block; text-align: left;\">");
            out.println("<form method=\"post\">");

            out.println("ID:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", ItemLogic.ID);
            out.println("<br>");

            out.println("Item Title:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", ItemLogic.TITLE);
            out.println("<br>");

            out.println("Description:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", ItemLogic.DESCRIPTION);
            out.println("<br>");

            out.println("Price:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", ItemLogic.PRICE);
            out.println("<br>");

            out.println("Date:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", ItemLogic.DATE);
            out.println("<br>");

            out.println("Location:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", ItemLogic.LOCATION);
            out.println("<br>");

            out.println("Image ID:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", ItemLogic.IMAGE_ID);
            out.println("<br>");

            out.println("Category ID:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", ItemLogic.CATEGORY_ID);
            out.println("<br>");

            out.println("URL:<br>");
            out.printf("<input type=\"text\" name=\"%s\" value=\"\"><br>", ItemLogic.URL);
            out.println("<br>");

            out.println("<input type=\"submit\" name=\"view\" value=\"Add and View\">");
            out.println("<input type=\"submit\" name=\"add\" value=\"Add\">");
            out.println("</form>");
            if(errorMessage!=null&&!errorMessage.isEmpty()){
                out.println("<p color=red>");
                out.println("<font color=red size=4px>");
                out.println(errorMessage);
                out.println("</font>");
                out.println("</p>");
            }
            out.println("<pre>");
            out.println("Submitted keys and values:");
            out.println(toStringMap(request.getParameterMap()));
            out.println("</pre>");
            out.println("</div>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String toStringMap(Map<String, String[]> values) {
        StringBuilder builder = new StringBuilder();
        values.forEach((k, v) -> builder.append("Key=").append(k)
                .append(", ")
                .append("Value/s=").append(Arrays.toString(v))
                .append(System.lineSeparator()));
        return builder.toString();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("GET");
        processRequest(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        log("POST");
        ItemLogic iLogic = new ItemLogic();
        ImageLogic imageLogic = new ImageLogic();
        CategoryLogic categoryLogic = new CategoryLogic();

        String image_id = request.getParameter(ItemLogic.IMAGE_ID);
        String category_id = request.getParameter(ItemLogic.CATEGORY_ID);


        if(iLogic.getWithUrl(request.getParameter(ItemLogic.URL))==null){

            try {


                Item itm = iLogic.createEntity(request.getParameterMap());

                if (iLogic.getWithId(Integer.parseInt(request.getParameter(ItemLogic.ID)))!=null){
                    throw new ValidationException("Item with ID " + request.getParameter(ItemLogic.ID)+ " already exists");
                }

                if (image_id.isEmpty()){
                    throw new ValidationException("Image ID cannot be empty");
                }

                Image image = imageLogic.getWithId(Integer.parseInt(image_id));
                if (image==null){
                   throw new ValidationException("Image ID does not exist");
                }

                if (category_id.isEmpty()){
                    throw new ValidationException("Category ID cannot be null");
                }

                Category category = categoryLogic.getWithId(Integer.parseInt(category_id));
                if (category==null){
                    throw new ValidationException("Category ID does not exist");
                }

                itm.setImage(image);
                itm.setCategory(category);
                iLogic.add(itm);
                errorMessage = "Item added successfully";

            }
           catch (ValidationException ex){
               errorMessage = ex.getMessage();
           }
        }else{
            errorMessage = "Item with same url: \"" + request.getParameter(ItemLogic.URL) + "\" already exists";
        }

        if(request.getParameter("add")!=null){
            processRequest(request, response);

        }
        else if(request.getParameter("view")!=null) {
            response.sendRedirect("ItemTable");
        }
    }


    @Override
    public String getServletInfo() {
        return "Create a Item Entity";
    }

    private static final boolean DEBUG = true;

    public void log( String msg) {
        if(DEBUG){
            String message = String.format( "[%s] %s", getClass().getSimpleName(), msg);
            getServletContext().log( message);
        }
    }

    public void log( String msg, Throwable t) {
        String message = String.format( "[%s] %s", getClass().getSimpleName(), msg);
        getServletContext().log( message, t);
    }
}
