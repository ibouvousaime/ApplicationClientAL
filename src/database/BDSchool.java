package database;
import entities.Utilisateur;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class BDSchool {
    
    private  final String user="newsite";
    private final String password="passer";
    private final String url ="jdbc:mysql://localhost:3306/journal";
    private String driver = "com.mysql.jdbc.Driver";
    private Connection connex;
    public static String tokenDeConnexion;
    
    public String buildXMLRequest(String functionName, ArrayList<String> argumentNames, ArrayList<String> argumentValues) {
        System.out.println("le token enregistre est: " + BDSchool.tokenDeConnexion);
        String headerContent = "";
        headerContent = "<token>" + BDSchool.tokenDeConnexion + "</token>";
        
        String base1 = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:hel=\"http://helloage.app.web.softwarepulse/\">" +
                        "   <soapenv:Header>" + headerContent + "</soapenv:Header>" +
                        "   <soapenv:Body>";
        String base2 = "   </soapenv:Body>" +
                        "</soapenv:Envelope>";
        String functionTagPart1 = "<" + functionName + ">";
        String functionTagPart2 = "</" + functionName + ">";
        String argumentString = "";
        for (int i = 0; i < argumentNames.size(); i++) {
          argumentString += "<" +argumentNames.get(i) + ">";
          argumentString += argumentValues.get(i);
          argumentString += "</" +argumentNames.get(i) + ">";
        }
        String resultat = base1 + functionTagPart1 + argumentString + functionTagPart2 + base2;
        System.out.println(resultat);
        return resultat;
    }
    
    public NodeList sendSoapRequest(String xmlInput, String functionName) {
        String wsURL = "http://127.0.0.1:5000/soap";
        URL url = null;
        URLConnection connection = null;
        HttpURLConnection httpConn = null;
        String responseString = null;
        String outputString="";
        OutputStream out = null;
        InputStreamReader isr = null;
        BufferedReader in = null;
        try
        {
            url = new URL(wsURL);
            connection = url.openConnection();
            httpConn = (HttpURLConnection) connection;
 
            byte[] buffer = new byte[xmlInput.length()];
            buffer = xmlInput.getBytes();
 
            String SOAPAction = "";
            // Set the appropriate HTTP parameters.
             httpConn.setRequestProperty("Content-Length", String
                     .valueOf(buffer.length));
            httpConn.setRequestProperty("Content-Type",
                    "text/xml; charset=utf-8");
             
             
            httpConn.setRequestProperty("SOAPAction", SOAPAction);
            httpConn.setRequestMethod("POST");
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            out = httpConn.getOutputStream();
            out.write(buffer);
            out.close();
             
            // Read the response and write it to standard out.
            isr = new InputStreamReader(httpConn.getInputStream());
            in = new BufferedReader(isr);
             
            while ((responseString = in.readLine()) != null) 
            {
                outputString = outputString + responseString;
            }
           // System.out.println(outputString);
            //System.out.println("");
             
            // Get the response from the web service call
            Document document = lireFixhierXML(outputString);
             
            NodeList nodeLst = document.getElementsByTagName("return");
            String webServiceResponse = nodeLst.item(0).getTextContent();
            //System.out.println("The response from the web service call is : " + webServiceResponse);
            return nodeLst;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }
    
     private Document lireFixhierXML(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
             InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }
 
    public BDSchool(){
        //tokenDeConnexion = "";
    }
    
    
   public boolean seConnecter(String login, String password)
   {
       ArrayList<String> argumentNames = new ArrayList<String>();
       argumentNames.add("login");
       argumentNames.add("password");
       ArrayList<String> argumentValues = new ArrayList<String>();
       argumentValues.add(login);
       argumentValues.add(password);
       NodeList result =  this.sendSoapRequest(this.buildXMLRequest("userLogin",argumentNames , argumentValues),"userLogin");
       String token = result.item(0).getTextContent();
       boolean res = false;
       if (!token.contains("passe")) {
           BDSchool.tokenDeConnexion = token;
           res = true;
       }
       
       else res = false;
       return res;
   } 

    public boolean ajouterUtilisateur(Utilisateur utilisateur){
        try {
             ArrayList<String> utilisateurChamps = utilisateur.getAttributeArrayNoms();
             ArrayList<String> utilisateurValeurs = utilisateur.getAttributeArray();
             NodeList result = this.sendSoapRequest(this.buildXMLRequest("insertUser",utilisateurChamps , utilisateurValeurs),"insertUser");
             System.out.print(result.item(0).getTextContent());
            return true;
            } catch (Exception e) {
                e.printStackTrace(); 
                return false;
            }
    }
    
    
    public boolean modifierUtilisateur(Utilisateur utilisateur){
       try {
             ArrayList<String> utilisateurChamps = utilisateur.getAttributeArrayNoms();
             ArrayList<String> utilisateurValeurs = utilisateur.getAttributeArray();
             NodeList result = this.sendSoapRequest(this.buildXMLRequest("updateUser",utilisateurChamps , utilisateurValeurs),"updateUser");
             System.out.print(result.item(0).getTextContent());
            return true;
            } catch (Exception e) {
                e.printStackTrace(); 
                return false;
            }
    }

    public boolean supprimerUtilisateur(int id){
        try {
            ArrayList<String> argumentNames = new ArrayList<String>();
            argumentNames.add("id");
            ArrayList<String> argumentValues = new ArrayList<String>();
            argumentValues.add(String.valueOf(id));
            NodeList result = this.sendSoapRequest(this.buildXMLRequest("deleteUser",argumentNames , argumentValues),"deleteUser");
            System.out.print(result.item(0).getTextContent());
            return true;
            } catch (Exception e) {
                e.printStackTrace(); 
                return false;
            }
    }
   
    
    public void chargerUtilisateurs(DefaultTableModel dftm, JTable jtUtilisateurs){
        
        try {
            
        NodeList users = this.sendSoapRequest(this.buildXMLRequest("getUsers", new ArrayList<String>(), new ArrayList<String>()),"getUsers");
        NodeList actualUsers = users.item(0).getChildNodes();
        for (int i =0; i< actualUsers.getLength(); i++)
        {
            NodeList userAttributes = actualUsers.item(i).getChildNodes();
            Vector v = new Vector();
            for (int j =0; j< userAttributes.getLength(); j++) {
                if (j == 4) continue;
               System.out.println("token " + tokenDeConnexion);
               v.add(userAttributes.item(j).getTextContent());
            }
            dftm.addRow(v);
        }
        jtUtilisateurs.setModel(dftm);//Chargment des colonnes et ligns sur le jtable
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
      
    
    public static void main(String[] args) {
        
       BDSchool bd= new BDSchool();
       //bd.seConnecter("seck", "passer");
       
    }
}
