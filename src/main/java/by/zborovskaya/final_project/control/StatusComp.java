package by.zborovskaya.final_project.control;

import by.zborovskaya.final_project.entity.Order;

import java.util.Comparator;

public class StatusComp implements Comparator<Order> {
    @Override
    public int compare(Order o1, Order o2) {
        String status1 = o1.getStatus();
        String status2 = o1.getStatus();
        return status1.equals("waiting") ? 1 : (status2.equals("waiting") ? -1 : 0);
    }
}
