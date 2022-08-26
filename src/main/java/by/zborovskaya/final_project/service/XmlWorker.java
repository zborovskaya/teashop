package by.zborovskaya.final_project.service;

import by.zborovskaya.final_project.entity.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;

public class XmlWorker {
    private static final String ABSOLUTE_PATH = "F:/products/";

    public static void serializeToXML(Product serializeProduct) throws JsonProcessingException, IOException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure( ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true );
        xmlMapper.registerModule(new JavaTimeModule());
//            File file = new File(path +File.separator+"product"+serializeProduct.getId()+".xml");
        File file = new File(ABSOLUTE_PATH + "product" + serializeProduct.getId() + ".xml");
        xmlMapper.writeValue(file, serializeProduct);
    }

    public static Product deserializeFromXML(String fileName) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new JavaTimeModule());
        String readContent = new String(Files.readAllBytes(Paths.get(fileName)));
        Product deserializedData = xmlMapper.readValue(readContent, Product.class);
        return deserializedData;
    }

    public static void main(String[] args) {
        try {
            Product product = new Product( 2, "test",
                    "test", "test", BigDecimal.valueOf(12), 89,
                    LocalTime.now(), BigDecimal.valueOf(12),true);
////            deserializeFromXML();
            serializeToXML(product);
//            System.out.println(deserializeFromXML("F:/products/product0.xml"));
        } catch (Exception ex) {
            System.out.println("er");
        }
    }
}
