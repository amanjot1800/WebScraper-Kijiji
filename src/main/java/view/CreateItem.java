package view;

import entity.Account;
import entity.Category;
import entity.Item;
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
        String item = request.getParameter(ItemLogic.TITLE);
        if(iLogic.getWithTitle(item) == null){
            Item itm = iLogic.createEntity(request.getParameterMap());
            iLogic.add(itm);

        }else{

            errorMessage = "Item: \"" + item + "\" already exists";
        }
        if(request.getParameter("add")!=null){

            processRequest(request, response);

        }else if(request.getParameter("view")!=null) {

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
