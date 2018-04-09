package com.consultsim.mallsim.View;


import com.consultsim.mallsim.Model.Objects;
import com.consultsim.mallsim.Model.Position;
import com.consultsim.mallsim.Model.StaticObjects.Mall;
import com.consultsim.mallsim.Model.Store;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class FileHandler {

    private ArrayList<Store> arrayOfStores;
    private ArrayList<Objects> arrarOfObjects;

    FileHandler() {
        arrayOfStores = new ArrayList<>();
        arrarOfObjects = new ArrayList<>();
    }

    private static boolean validateAgainstXSD(InputStream xml, InputStream xsd) {
        try {
            SchemaFactory factory =
                    SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(xsd));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(xml));
            System.out.println("Valid XML");
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public ArrayList<Store> getArrayOfStores() {
        return arrayOfStores;
    }

    public ArrayList<Objects> getArrarOfObjects() {
        return arrarOfObjects;
    }

    public void readFile(String fileName) throws FileNotFoundException {
        InputStream xmlFile = new FileInputStream("InputMallSim.xml");
        InputStream xsdFile = new FileInputStream("xsd.xsd");
        validateAgainstXSD(xmlFile, xsdFile);


        try {

            File inputFile = new File(fileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            Node node = doc.getFirstChild();
            NodeList node1 = node.getChildNodes();


            for (int i = 0; i < node1.getLength(); i++) {
                switch (node1.item(i).getNodeName()) {
                    case "store":
                        handleStore(node1.item(i));
                        break;
                    case "object":
                        handleObject(node1.item(i));
                        break;
                    case "generalInfo":
                        handleGeneralInfo(node1.item(i));
                        break;
                    case "entranceDoor":
                        handleEntranceDoor(node1.item(i));
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handleEntranceDoor(Node n) {

    }

    private void handleGeneralInfo(Node n) {

        Element element = (Element) n;
        Mall mall = new Mall();

        String xSize = element.getElementsByTagName("xSize").item(0).getChildNodes().item(0).getNodeValue();
        String ySize = element.getElementsByTagName("ySize").item(0).getChildNodes().item(0).getNodeValue();

        String xPosLeftUpper = element.getElementsByTagName("xPos").item(0).getChildNodes().item(0).getNodeValue();
        String yPosLeftUpper = element.getElementsByTagName("yPos").item(0).getChildNodes().item(0).getNodeValue();
        String xPosDownRight = element.getElementsByTagName("xPos").item(1).getChildNodes().item(0).getNodeValue();
        String yPosDownRight = element.getElementsByTagName("yPos").item(1).getChildNodes().item(0).getNodeValue();


        mall.setxSize(Integer.parseInt(xSize));
        mall.setySize(Integer.parseInt(ySize));

        mall.setDoorDownRight((new Position(Integer.parseInt(xPosDownRight), Integer.parseInt(yPosDownRight))));
        mall.setDoorLeftUpper((new Position(Integer.parseInt(xPosLeftUpper), Integer.parseInt(yPosLeftUpper))));

        System.out.println("Door left upper: " + mall.getDoorLeftUpper().getX() + "::" + mall.getDoorLeftUpper().getY());
        System.out.println("Door down right: " + mall.getDoorDownRight().getX() + "::" + mall.getDoorDownRight().getY());

    }

    private void handleStore(Node n) {
        Element element = (Element) n;

        int id = Integer.parseInt(element.getAttribute("id"));

        String xPosLeftUpper = element.getElementsByTagName("xPos").item(0).getChildNodes().item(0).getNodeValue();
        String yPosLeftUpper = element.getElementsByTagName("yPos").item(0).getChildNodes().item(0).getNodeValue();
        //System.out.println(xPosLeftUpper + ":::" + yPosLeftUpper);

        String xPosDownRight = element.getElementsByTagName("xPos").item(1).getChildNodes().item(0).getNodeValue();
        String yPosDownRight = element.getElementsByTagName("yPos").item(1).getChildNodes().item(0).getNodeValue();

        String xPosDoorLeftUpper = element.getElementsByTagName("xPos").item(2).getChildNodes().item(0).getNodeValue();
        String yPosDoorLeftUpper = element.getElementsByTagName("yPos").item(2).getChildNodes().item(0).getNodeValue();

        String xPosDoorDownRight = element.getElementsByTagName("xPos").item(3).getChildNodes().item(0).getNodeValue();
        String yPosDoorDownRight = element.getElementsByTagName("yPos").item(3).getChildNodes().item(0).getNodeValue();

        String name = element.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue();

        String infoOne = element.getElementsByTagName("info").item(0).getChildNodes().item(0).getNodeValue();
        String infoTwo = element.getElementsByTagName("info").item(1).getChildNodes().item(0).getNodeValue();

        String maxVisitors = element.getElementsByTagName("maxVisitors").item(0).getChildNodes().item(0).getNodeValue();

        String openTime = element.getElementsByTagName("openTime").item(0).getChildNodes().item(0).getNodeValue();
        String closeTime = element.getElementsByTagName("closeTime").item(0).getChildNodes().item(0).getNodeValue();

        String startTime = element.getElementsByTagName("startTime").item(0).getChildNodes().item(0).getNodeValue();
        String endTime = element.getElementsByTagName("endTime").item(0).getChildNodes().item(0).getNodeValue();

        String color = element.getElementsByTagName("color").item(0).getChildNodes().item(0).getNodeValue();

        System.out.println(color);


        Store tempStore = new Store();
        tempStore.setDoorPosition(new int[]{Integer.parseInt(xPosDoorLeftUpper), Integer.parseInt(yPosDoorLeftUpper), Integer.parseInt(xPosDoorDownRight), Integer.parseInt(yPosDoorDownRight)});
        tempStore.setPosition(new int[]{Integer.parseInt(xPosLeftUpper), Integer.parseInt(yPosLeftUpper), Integer.parseInt(xPosDownRight), Integer.parseInt(yPosDownRight)});


        tempStore.setInterestingFor(new String[]{infoOne, infoTwo});
        tempStore.setLabel(name);


        //ERROR - convertion, but there are only Strings in here

        tempStore.setColor(Color.web(color));


        tempStore.setPeopleCounter(Integer.parseInt(maxVisitors));

        tempStore.setId(id);

        if (checkIfAttributesAreValid(tempStore)) {
            arrayOfStores.add(tempStore);
        } else {
            System.out.println("Error: border intersection");
        }


    }

    private void handleObject(Node n) {
        Element element = (Element) n;


        int id = Integer.parseInt(element.getAttribute("id"));

        String xPosLeftUpper = element.getElementsByTagName("xPos").item(0).getChildNodes().item(0).getNodeValue();
        String yPosLeftUpper = element.getElementsByTagName("yPos").item(0).getChildNodes().item(0).getNodeValue();

        String xPosDownRight = element.getElementsByTagName("xPos").item(1).getChildNodes().item(0).getNodeValue();
        String yPosDownRight = element.getElementsByTagName("yPos").item(1).getChildNodes().item(0).getNodeValue();


        String type = element.getElementsByTagName("type").item(0).getChildNodes().item(0).getNodeValue();

        Objects tempObject = new Objects();
        tempObject.setPosition(new int[]{Integer.parseInt(xPosLeftUpper), Integer.parseInt(yPosLeftUpper), Integer.parseInt(xPosDownRight), Integer.parseInt(yPosDownRight)});
        tempObject.setLabel(type);
        tempObject.setId(id);
        arrarOfObjects.add(tempObject);

    }


    private boolean checkIfAttributesAreValid(Store newStore) {

        int newStorePos[] = newStore.getPosition();

        int xulnewStore = newStorePos[0];
        int yulnewStore = newStorePos[1];
        int xdrnewStore = newStorePos[2];
        int ydrnewStore = newStorePos[3];


        Rectangle ne = new Rectangle(xulnewStore, yulnewStore, (xdrnewStore - xulnewStore), (ydrnewStore - yulnewStore));

        for (Store s : arrayOfStores) {

            int oldStorePos[] = s.getPosition();

            int xuloldStore = oldStorePos[0];
            int yuloldStore = oldStorePos[1];
            int xdroldStore = oldStorePos[2];
            int ydroldStore = oldStorePos[3];

            Rectangle old = new Rectangle(xuloldStore, yuloldStore, (xdroldStore - xuloldStore), (ydroldStore - yuloldStore));

            if (overlaps(old, ne)) {
                return false;
            }
        }
        return true;
    }

    private boolean overlaps(Rectangle old, Rectangle ne) {
        return ne.getX() < old.getX() + old.getWidth()
                && ne.getWidth() + ne.getWidth() > old.getX()
                && ne.getY() < old.getY() + old.getHeight()
                && ne.getY() + ne.getHeight() > old.getY();
    }


}
