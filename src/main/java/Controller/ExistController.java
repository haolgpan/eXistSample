package Controller;


import javax.xml.xquery.*;
import net.xqj.exist.ExistXQDataSource;

/**
 * Crea una nova connexió a la base de dades Exist amb les credencials d'usuari i contrasenya proporcionades
 * a través de les propietats del sistema.
 */
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

    /**
     * Executa una consulta XQuery donada i retorna una seqüència de resultats.
     * @param query Consulta XQuery a executar.
     * @return Seqüència de resultats obtinguda de l'execució de la consulta XQuery.
     */
    public XQResultSequence executeQuery(String query) {
        try {
            XQExpression xqe = connection.createExpression();
            XQResultSequence xqrs = xqe.executeQuery(query);
            return xqrs;
        } catch (XQException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Imprimeix la seqüència de resultats donada per paràmetre per la sortida estàndard.
     * @param xqrs Seqüència de resultats a imprimir.
     */
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
    /**
     * Executa una consulta XQuery per obtenir els noms de totes les espècies de la base de dades.
     */
    public void queryAllSpiceName(){
        XQResultSequence xqrs = executeQuery("for $SPICE in doc('/db/apps/collections/foaf/Spices/spices.xml')/SPICES/SPICE return $SPICE/SPICE_NAME");
        printResultSequence(xqrs);
    }
    /**
     * Executa una consulta XQuery per obtenir els noms de les espècies d'un país determinat.
     * @param country País a buscar.
     */
    public void queryByCountry(String country){
        XQResultSequence xqrs = executeQuery( "for $SPICE in doc('/db/apps/collections/foaf/Spices/spices.xml')/SPICES/SPICE where contains ($SPICE/SPICE_COUNTRY_ORIGIN,'" + country +"')" + " return $SPICE/SPICE_NAME");
        printResultSequence(xqrs);
    }
    /**
     * Executa una consulta XQuery per obtenir els noms de les espècies d'una cuina determinada.
     * @param cuisine Cuina a buscar.
     */
    public void queryByCuisine(String cuisine){
        XQResultSequence xqrs = executeQuery( "for $SPICE in doc('/db/apps/collections/foaf/Spices/spices.xml')/SPICES/SPICE where contains ($SPICE/SPICE_CUISINE,'" + cuisine +"')" + " return $SPICE/SPICE_NAME");
        printResultSequence(xqrs);
    }
    public void queryByFormat(String format){
        XQResultSequence xqrs = executeQuery( "for $SPICE in doc('/db/apps/collections/foaf/Spices/spices.xml')/SPICES/SPICE where contains ($SPICE/SPICE_PRODUCT_STYLE,'" + format +"')" + " return $SPICE/SPICE_NAME");
        printResultSequence(xqrs);
    }
    /**
     * Imprimeix una llista de formats de productes d'espècies.
     */
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
    /**
     * Insereix una nova espècia amb les dades especificades al document XML.
     * @param name Nom de l'espècia.
     * @param description Descripció de l'espècia.
     * @param format Format del producte d'espècia.
     * @param country País d'origen de l'espècia.
     */
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
    /**
     * Elimina una espècia pel seu nom del document XML.
     * @param name Nom de l'espècia a eliminar.
     */
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
    /**
     * Actualitza el nom d'una espècia pel seu nom actual pel nou nom especificat.
     * @param name Nom actual de l'espècia a actualitzar.
     * @param newName Nou nom per a l'espècia.
     */
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
    /**
     * Actualitza un camp determinat per un nom de espècia concret amb un nou valor.
     *
     * @param name Nom de l'espècia a actualitzar.
     * @param field Nom del camp que es vol actualitzar.
     * @param newValue Nou valor que s'assignarà al camp.
     */
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
    /**
     * Elimina totes les espècies que tinguin un determinat país d'origen.
     *
     * @param country País d'origen a buscar per eliminar les espècies relacionades.
     */
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
    /**
     * Elimina totes les espècies que tinguin un determinat estil de producte.
     *
     * @param productStyle Estil de producte a buscar per eliminar les espècies relacionades.
     */
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

}