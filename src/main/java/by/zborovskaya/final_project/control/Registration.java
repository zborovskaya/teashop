package by.zborovskaya.final_project.control;

import by.zborovskaya.final_project.entity.User;
import by.zborovskaya.final_project.entity.UserInfo;
import by.zborovskaya.final_project.entity.UserRole;
import by.zborovskaya.final_project.service.ClientService;
import by.zborovskaya.final_project.service.ServiceException;
import by.zborovskaya.final_project.service.ServiceFactory;
import by.zborovskaya.final_project.service.UserService;
import by.zborovskaya.final_project.service.impl.ClientServiceImpl;
import by.zborovskaya.final_project.service.impl.UserServiceImpl;
import by.zborovskaya.final_project.service.validation.exception.ValidatorException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static by.zborovskaya.final_project.control.Parameter.*;

@WebServlet("/registration")
public class Registration extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final UserService userService = ServiceFactory.getInstance().getUserService();
        final ClientService clientService = ServiceFactory.getInstance().getClientService();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        UserRole role = user != null && user.getUserRole() == UserRole.ADMIN ?
                UserRole.ADMIN : UserRole.CLIENT;
        String password = request.getParameter(PASSWORD);
        String repeat_password = request.getParameter("repeat_password");
        if(!password.equals(repeat_password)){
            session.setAttribute("invalid_repeat_password", "Неправильно повторен пароль");
            response.sendRedirect(request.getContextPath() + "/registration");
            return;
        }

        User userRegistr = new User ();
        String login = request.getParameter(LOGIN);
        userRegistr.setLogin(login);
        userRegistr.setActive(true);
        userRegistr.setUserRole(role);
        userRegistr.setPassword(password);
        try{
            boolean isUserRegistrated = userService.registration(userRegistr);

            if(!isUserRegistrated){
                session.setAttribute("invalid_login", "Данный логин существует");
                response.sendRedirect(request.getContextPath() + "/registration");
                return;
            }
            long id= userService.authorization(login,password).getIdUser();
            UserInfo userInfo = new UserInfo(userRegistr,
                    request.getParameter(USER_EMAIL),
                    request.getParameter(USER_FIRST_NAME),
                    request.getParameter(USER_LAST_NAME),
                    Integer.valueOf(request.getParameter(USER_PHONE_NUMBER)),
                    request.getParameter(USER_ADDRESS));
            boolean isClientInfoSave = clientService.addClientData(userInfo,id);
            session.setAttribute("user",userRegistr);
            response.sendRedirect(request.getContextPath() + "/main");
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/registration");
            return;
        }
    }
}
