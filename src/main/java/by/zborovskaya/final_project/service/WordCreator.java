package by.zborovskaya.final_project.service;

import by.zborovskaya.final_project.entity.Order;
import by.zborovskaya.final_project.entity.PaymentMethod;
import by.zborovskaya.final_project.entity.UserInfo;
import by.zborovskaya.final_project.service.impl.OrderServiceImpl;
import org.apache.poi.xwpf.usermodel.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public class WordCreator {
    private static final OrderService service = OrderServiceImpl.getInstance();

    static String fileName = "F:/order_details/order.docx";

    public static void create(Order order, UserInfo userInfo) {
            try (XWPFDocument docxModel = new XWPFDocument()) {
                XWPFParagraph p1 = docxModel.createParagraph();
                p1.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun r1 = p1.createRun();
                r1.setFontSize(16);
                r1.setBold(true);
                r1.setFontFamily("New Roman");
                r1.setText("Детали заказа");
                XWPFTable table = docxModel.createTable(8, 2);
                for(XWPFTableRow row:  table.getRows()){
                    row.getTableCells().stream().forEach((cell)->{
                        XWPFParagraph p = cell.addParagraph();
                        p.setAlignment(ParagraphAlignment.CENTER);
                        XWPFRun r = p.createRun();
                        r.setFontSize(14);
                        r.setBold(true);
                        r.setFontFamily("New Roman");
                    });
                }
                List<XWPFTableRow> rows = table.getRows();
                List<XWPFTableCell> cells0 = rows.get(0).getTableCells();
                cells0.get(0).setText("Имя");
                cells0.get(1).setText(userInfo.getFirstName());
                List<XWPFTableCell> cells1 = rows.get(1).getTableCells();
                cells1.get(0).setText("Фамилия");
                cells1.get(1).setText(userInfo.getLastName());
                List<XWPFTableCell> cells2 = rows.get(2).getTableCells();
                cells2.get(0).setText("Email");
                cells2.get(1).setText(userInfo.getEmail());
                List<XWPFTableCell> cells3 = rows.get(3).getTableCells();
                cells3.get(0).setText("Телефон");
                cells3.get(1).setText("+375" + userInfo.getPhone());
                List<XWPFTableCell> cells4 = rows.get(4).getTableCells();
                cells4.get(0).setText("Адрес");
                cells4.get(1).setText(order.getDeliveryAddress());
                List<XWPFTableCell> cells5 = rows.get(5).getTableCells();
                cells5.get(0).setText("Дата заказа");
                cells5.get(1).setText(order.getDeliveryDate().toString());
                List<XWPFTableCell> cells6 = rows.get(6).getTableCells();
                cells6.get(0).setText("Метод оплаты");
                cells6.get(1).setText(order.getPaymentMethod().toString());
                List<XWPFTableCell> cells7 = rows.get(7).getTableCells();
                cells7.get(0).setText("Цена");
                cells7.get(1).setText(order.getTotalCost().toString() + " руб");
                try (FileOutputStream out = new FileOutputStream(fileName)) {
                    docxModel.write(out);
                }catch (IOException ex){}
            }catch (IOException e){}
    }

//    public static void main(String[] args) {
//        UserInfo userInfo = new UserInfo(1, "email", "Анна",
//                "Зборовская", 123455453, "Могилёв");
//        Order order = new Order(2, 3, BigDecimal.valueOf(12), "адрес",
//                LocalDate.now(), PaymentMethod.CACHE);
//        WordCreator.create(order, userInfo);
//
//    }
}
