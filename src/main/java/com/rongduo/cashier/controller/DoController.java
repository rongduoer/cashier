package com.rongduo.cashier.controller;

import com.rongduo.cashier.model.order.Order;
import com.rongduo.cashier.model.order.OrderDetail;
import com.rongduo.cashier.model.product.Product;
import com.rongduo.cashier.model.product.ProductParam;
import com.rongduo.cashier.model.user.PasswordValidator;
import com.rongduo.cashier.model.user.User;
import com.rongduo.cashier.model.user.UsernameValidator;
import com.rongduo.cashier.service.OrderService;
import com.rongduo.cashier.service.ProductService;
import com.rongduo.cashier.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: rongduo
 * @description: 与前端交互控制类，接收用户提交上来的数据，最后进行重定向
 * @date: 2022-09-02
 */

@Slf4j
@Controller
public class DoController {
    private final UserService userService;
    private final UsernameValidator usernameValidator;
    private final PasswordValidator passwordValidator;
    private final ProductService productService;
    private final OrderService orderService;

    @Autowired
    public DoController(UserService userService, UsernameValidator usernameValidator, PasswordValidator passwordValidator, ProductService productService, OrderService orderService) {
        this.userService = userService;
        this.usernameValidator = usernameValidator;
        this.passwordValidator = passwordValidator;
        this.productService = productService;
        this.orderService = orderService;
    }

    // 我们的这个方法中没有 try-catch，catch 就在我们刚才些的 CashControllerAdvice 中
    @PostMapping("/register.do")
    // 由于参数名称和 form 表单里的 input 的 name 是对应上的，所以省略了 @RequestParam 注解了
    public String register(String username, String password, HttpServletRequest request) {
        String module = "用户注册";
        String redirectUrl = "/register.html";
        // 咱定好一个规则，无论哪个动态资源，先打印功能 + 参数
        log.debug("{}: username = {}, password = {}", module, username, password);

        // 进行参数的合法性校验 validate（验证；使生效；使有效）
        username = usernameValidator.validate(module, redirectUrl, username);    // 如果有错，直接异常穿透 register 方法就出去了
        password = passwordValidator.validate(module, redirectUrl, password);    // 同理

        // 完成注册的工作
        try {
            User user = userService.register(username, password);

            // 直接完成登录操作（把刚注册的用户信息放入 session 中，视为登录了）
            HttpSession session = request.getSession();
            session.setAttribute("currentUser", user);

            // 最终注册成功之后，跳转到首页(/)
            log.debug("{}: 注册成功, user = {}", module, user);
            return "redirect:/";
        } catch (DuplicateKeyException exc) {
            throw new ErrorRedirectException("username 重复", module, redirectUrl, exc);
        }
    }

    @PostMapping("/login.do")
    public String login(String username, String password, HttpServletRequest request) {
        String module = "用户登录";
        String redirectUrl = "/login.html";
        log.debug("{}: username = {}, password = {}", module, username, password);

        username = usernameValidator.validate(module, redirectUrl, username);
        password = passwordValidator.validate(module, redirectUrl, password);

        User user = userService.login(username, password);

        if (user == null) {
            throw new ErrorRedirectException("username or password 错误", module, redirectUrl);
        }

        HttpSession session = request.getSession();
        session.setAttribute("currentUser", user);

        log.debug("{}: 登录成功, user = {}", module, user);
        return "redirect:/";
    }

    @PostMapping("/product/create.do")
    // 由于参数列表比较长，所以我们不采用一个个列出来的方式，而是使用一个对象的方式
    // 通过对象接收请求参数的方式
    public String productCreate(ProductParam productParam, HttpServletRequest request) {
        String module = "用户登录";
        String redirectUrl = "/product/create.html";
        log.debug("{}: 请求参数 = {}", module, productParam);

        User currentUser = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            currentUser = (User) session.getAttribute("currentUser");
        }

        if (currentUser == null) {
            // 说明用户未登录
            log.debug("{}: 用户未登录，无权进行该操作", module);
            return "redirect:/login.html";  // 重定向到登录页，让用户登录
        }

        log.debug("{}: 当前登录用户为: {}", module, currentUser);
        // 验证合法性【整体业务写完再回来写这个，但要求自己输入的时候，一定要输入合法的参数，比如价格如果不是数字，就会 500
//        productParam.validate(module, redirectUrl);
        // 使用 service -> mapper 保存
        Product product = productService.create(currentUser, productParam);

        log.debug("{}: 成功，上架商品为: {}", module, product);
        return "redirect:/product/list.html";   // 商品上架成功后跳转到商品列表页
    }

    @PostMapping("/product/update.do")
    public String productUpdate(ProductParam productParam, HttpServletRequest request) {
        log.debug("商品更新: 请求参数 = {}", productParam);

        User currentUser = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            currentUser = (User) session.getAttribute("currentUser");
        }

        if (currentUser == null) {
            // 说明用户未登录
            log.debug("商品更新: 用户未登录，无权进行该操作");
            return "redirect:/login.html";  // 重定向到登录页，让用户登录
        }

        Product product = productService.update(currentUser, productParam);

        log.debug("商品更新: 成功，product = {}", product);

        return "redirect:/product/list.html";
    }

    @PostMapping("/order/create.do")
    public String orderCreate(@RequestParam("create-param") String createParam, HttpServletRequest request) {
        log.debug("购买商品: createParam = {}", createParam);

        Map<Integer, Integer> toBoughtProductMap = new HashMap<>();
        // TODO: 暂时不考虑参数合法性的问题
        String[] split = createParam.split(",");
        for (String s : split) {
            String[] group = s.split("-");
            String productIdStr = group[0];     // 参数不对，就会抛异常
            String numberStr = group[1];     // 参数不对，就会抛异常

            int productId = Integer.parseInt(productIdStr);     // 参数不对，就会抛异常
            int number = Integer.parseInt(numberStr);           // 参数不对，就会抛异常

            toBoughtProductMap.put(productId, number);
        }

        log.debug("toBoughtProductMap = {}", toBoughtProductMap);

        User currentUser = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            currentUser = (User) session.getAttribute("currentUser");
        }

        if (currentUser == null) {
            // 说明用户未登录
            log.debug("商品更新: 用户未登录，无权进行该操作");
            return "redirect:/login.html";  // 重定向到登录页，让用户登录
        }

        log.debug("当前用户: user = {}", currentUser);
        Order order = orderService.create(currentUser, toBoughtProductMap);
        log.debug("创建订单: {}", order);

        return "redirect:/order/detail/" + order.getUuid();
    }

    @GetMapping("/order/detail/{uuid}")
    public String orderDetail(@PathVariable("uuid") String uuid, HttpServletRequest request, Model model) {
        log.debug("订单详情: uuid = {}", uuid);

        User currentUser = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            currentUser = (User) session.getAttribute("currentUser");
        }

        if (currentUser == null) {
            // 说明用户未登录
            log.debug("商品更新: 用户未登录，无权进行该操作");
            return "redirect:/login.html";  // 重定向到登录页，让用户登录
        }
        log.debug("当前用户: user = {}", currentUser);

        OrderDetail orderDetail = orderService.query(uuid);

        log.debug("order = {}", orderDetail);

        model.addAttribute("order", orderDetail);

        return "order-detail";
    }

    @GetMapping("/order/confirm/{orderId}")
    public String orderConfirm(@PathVariable int orderId, HttpServletRequest request) {
        log.debug("确认订单: orderId = {}", orderId);

        User currentUser = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            currentUser = (User) session.getAttribute("currentUser");
        }

        if (currentUser == null) {
            // 说明用户未登录
            log.debug("商品更新: 用户未登录，无权进行该操作");
            return "redirect:/login.html";  // 重定向到登录页，让用户登录
        }
        log.debug("当前用户: user = {}", currentUser);

        orderService.confirm(orderId);

        return "redirect:/order/list.html";
    }

    @GetMapping("/order/cancel/{orderId}")
    public String orderCancel(@PathVariable int orderId, HttpServletRequest request) {
        log.debug("确认订单: orderId = {}", orderId);

        User currentUser = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            currentUser = (User) session.getAttribute("currentUser");
        }

        if (currentUser == null) {
            // 说明用户未登录
            log.debug("商品更新: 用户未登录，无权进行该操作");
            return "redirect:/login.html";  // 重定向到登录页，让用户登录
        }
        log.debug("当前用户: user = {}", currentUser);

        orderService.cancel(orderId);

        return "redirect:/order/list.html";
    }
}
