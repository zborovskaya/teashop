package by.zborovskaya.final_project.control;

import by.zborovskaya.final_project.entity.User;
import by.zborovskaya.final_project.service.ServiceFactory;
import by.zborovskaya.final_project.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/signin")
public class GotoSignIn extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/pages/signin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password  = req.getParameter("password");
        UserService userService = ServiceFactory.getInstance().getUserService();
        User user = null;
        HttpSession httpSession =req.getSession();
        try {

            if(httpSession.getAttribute("errorMessage")==null){
                httpSession.removeAttribute("errorMessage");
            }
            user = userService.authorization(login,password);
            if (user == null) {
                httpSession.setAttribute("errorMessage","Не верный логин или пароль");
                resp.sendRedirect(req.getContextPath() + "/signin");
                return;
            }
            if (!user.isActive()){
                httpSession.setAttribute("errorMessage","Данный пользователь был заблокирован администратором");
                resp.sendRedirect(req.getContextPath() + "/signin");
                return;
            }

        }catch (Exception e){
            httpSession.setAttribute("errorMessage","Введите все необходимые данные");
            resp.sendRedirect(req.getContextPath() + "/signin");
            return;
        }
        httpSession.setAttribute("user",user);
        resp.sendRedirect(req.getContextPath() + "/main");
    }
}
