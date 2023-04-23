package Controller;


import javax.xml.xquery.*;
import net.xqj.exist.ExistXQDataSource;

public class ExistController {
    private final XQConnection connection;
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
                """
                        Powder
                        De-Stemmed
                        Crushed
                        Whole
                        Liquid
                        Seeds/Berries
                        Loose Leaf
                        Null
                        Granulated
                        Blend-Crushed
                        Coarse Cut
                        Chips
                        Cross-cut
                        Sliced
                        Blend
                        Flakes
                        Shredded
                        Desiccated
                        Cracked
                        Minced
                        Crushed-Seeds/Berries
                        Whole-Seeds/Berries
                        Threads
                        Greek-cut
                        Diced""");
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
            xqe.executeCommand(xquery);
        } catch (XQException e) {
            e.printStackTrace();
        }
    }
    public void deleteSpiceByName(String name){
        try {
            xqe = connection.createExpression();
            String xquery = "update delete doc('/db/apps/collections/foaf/Spices/spices.xml')" +
                    "/SPICES/SPICE[SPICE_NAME='"+name+"']";
            xqe.executeCommand(xquery);
        } catch (XQException e) {
            e.printStackTrace();
        }
    }
    public void updateSpiceByName(String name, String newName) {
        try {
            xqe = connection.createExpression();
            String xquery = "update value doc('/db/apps/collections/foaf/Spices/spices.xml')" +
                    "/SPICES/SPICE[SPICE_NAME='"+name+"']/SPICE_NAME with '"+newName+"'";
            xqe.executeCommand(xquery);
        } catch (XQException e) {
            e.printStackTrace();
        }
    }
    public void updateSpiceAllField(String name, String field, String newValue) {
        try {
            xqe = connection.createExpression();
            String xquery = "update value doc('/db/apps/collections/foaf/Spices/spices.xml')" +
                    "/SPICES/SPICE[SPICE_NAME='"+name+"']/"+ field + " with '"+newValue+"'";
            System.out.println(xquery);
            xqe.executeCommand(xquery);
        } catch (XQException e) {
            e.printStackTrace();
        }
    }
    public void deleteSpiceByCountry(String country) {
        try {
            xqe = connection.createExpression();
            String xquery = "update delete doc('/db/apps/collections/foaf/Spices/spices.xml')/SPICES/SPICE[SPICE_COUNTRY_ORIGIN='" + country + "']";
            System.out.println(xquery);
            xqe.executeCommand(xquery);
        } catch (XQException e) {
            e.printStackTrace();
        }
    }

    public void deleteSpiceByProductStyle(String productStyle) {
        try {
            xqe = connection.createExpression();
            String xquery = "update delete doc('/db/apps/collections/foaf/Spices/spices.xml')/SPICES/SPICE[SPICE_PRODUCT_STYLE='" + productStyle + "']";
            System.out.println(xquery);
            xqe.executeCommand(xquery);
        } catch (XQException e) {
            e.printStackTrace();
        }
    }

//    public class XmlUploadExample {
//        public static void main(String[] args) {
//            String driver = "org.exist.xmldb.DatabaseImpl";
//            String uri = "xmldb:exist://localhost:8080/exist/xmlrpc";
//            String collectionPath = "/db/apps/collections/foaf/Spices";
//            String fileName = "spices.xml";
//
//            try {
//                // initialize database driver
//                Class<?> cl = Class.forName(driver);
//                Database database = (Database) cl.newInstance();
//                DatabaseManager.registerDatabase(database);
//
//                // get collection
//                Collection collection = DatabaseManager.getCollection(uri + collectionPath);
//                if (collection == null) {
//                    System.out.println("Collection does not exist!");
//                    return;
//                }
//
//                // create XML resource from file
//                File file = new File(fileName);
//                if (!file.exists()) {
//                    System.out.println("File does not exist!");
//                    return;
//                }
//                FileInputStream inputStream = new FileInputStream(file);
//                XMLResource resource = (XMLResource) collection.createResource(file.getName(), "XMLResource");
//                resource.setContent(inputStream);
//
//                // store resource in collection
//                collection.storeResource(resource);
//                System.out.println("File uploaded successfully!");
//
//                // close collection
//                collection.close();
//            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | XMLDBException |
//                     FileNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }


}