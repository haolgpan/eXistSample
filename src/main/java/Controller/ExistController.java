package Controller;


import javax.xml.xquery.*;
import net.xqj.exist.ExistXQDataSource;

public class ExistController {
    private XQConnection connection;
    private XQExpression xqe;

    public ExistController() {
        try {
            XQDataSource xqs = new ExistXQDataSource();
            xqs.setProperty("serverName", "localhost");
            xqs.setProperty("port", "8080");
            xqs.setProperty("user","admin");
            xqs.setProperty("password","admin");
            connection = xqs.getConnection("admin","admin");
            xqe = connection.createExpression();

        } catch (XQException e) {
            throw new RuntimeException(e);
        }
    }


    public XQResultSequence executeQuery(String query) {
        try {
            XQExpression xqe = connection.createExpression();
            XQResultSequence xqrs = xqe.executeQuery(query);
            return xqrs;
        } catch (XQException e) {
            throw new RuntimeException(e);
        }
    }

    public void printResultSequence(XQResultSequence xqrs) {
        try {
            while (xqrs.next()) {
                String itemString = xqrs.getItemAsString(null);
                itemString = itemString.replaceAll("<[^>]*>", ""); // remove XML tags
                System.out.println(itemString);
            }
        } catch (XQException e) {
            throw new RuntimeException(e);
        }
    }
    public void queryAllSpiceName(){
        XQResultSequence xqrs = executeQuery("for $SPICE in doc('/db/apps/collections/foaf/Spices/spices.xml')/SPICES/SPICE return $SPICE/SPICE_NAME");
        printResultSequence(xqrs);
    }
    public void queryByCountry(String country){
        XQResultSequence xqrs = executeQuery( "for $SPICE in doc('/db/apps/collections/foaf/Spices/spices.xml')/SPICES/SPICE where contains ($SPICE/SPICE_COUNTRY_ORIGIN,'" + country +"')" + " return $SPICE/SPICE_NAME");
        printResultSequence(xqrs);
    }
    public void queryByCuisine(String cuisine){
        XQResultSequence xqrs = executeQuery( "for $SPICE in doc('/db/apps/collections/foaf/Spices/spices.xml')/SPICES/SPICE where contains ($SPICE/SPICE_CUISINE,'" + cuisine +"')" + " return $SPICE/SPICE_NAME");
        printResultSequence(xqrs);
    }
    public void queryByFormat(String format){
        XQResultSequence xqrs = executeQuery( "for $SPICE in doc('/db/apps/collections/foaf/Spices/spices.xml')/SPICES/SPICE where contains ($SPICE/SPICE_PRODUCT_STYLE,'" + format +"')" + " return $SPICE/SPICE_NAME");
        printResultSequence(xqrs);
    }
    public void printFormat(){
        System.out.println(
                "Powder\n" +
                        "De-Stemmed\n" +
                        "Crushed\n" +
                        "Whole\n" +
                        "Liquid\n" +
                        "Seeds/Berries\n" +
                        "Loose Leaf\n" +
                        "Null\n" +
                        "Granulated\n" +
                        "Blend-Crushed\n" +
                        "Coarse Cut\n" +
                        "Chips\n" +
                        "Cross-cut\n" +
                        "Sliced\n" +
                        "Blend\n" +
                        "Flakes\n" +
                        "Shredded\n" +
                        "Desiccated\n" +
                        "Cracked\n" +
                        "Minced\n" +
                        "Crushed-Seeds/Berries\n" +
                        "Whole-Seeds/Berries\n" +
                        "Threads\n" +
                        "Greek-cut\n" +
                        "Diced");
    }
    public void insertSpice(String name, String description, String format, String country){
        try {
            xqe = connection.createExpression();
            String xquery = "update insert \n" +
                    "   <SPICE>" +
                    "<SPICE_DESCRIPTION>"+description+"</SPICE_DESCRIPTION>" +
                    "<SPICE_NAME>"+name+"</SPICE_NAME>" +
                    "<SPICE_COUNTRY_ORIGIN>"+country+"</SPICE_COUNTRY_ORIGIN>" +
                    "<SPICE_PRODUCT_STYLE>"+format+"</SPICE_PRODUCT_STYLE>" +
                    "</SPICE>" + " into doc('/db/apps/collections/foaf/Spices/spices.xml')/SPICES" ;
            System.out.println(xquery);
            xqe.executeCommand(xquery);
        } catch (XQException e) {
            e.printStackTrace();
        }
    }
    public void deleteSpice(String name){
        try {
            xqe = connection.createExpression();
            String xquery = "update delete doc('/db/apps/collections/foaf/Spices/spices.xml')/SPICES/SPICE[SPICE_NAME='"+name+"']";
            System.out.println(xquery);
            xqe.executeCommand(xquery);
        } catch (XQException e) {
            e.printStackTrace();
        }
    }
    public void updateSpice(){

    }
}