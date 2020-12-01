package world.ucode.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import world.ucode.models.*;
import world.ucode.services.BidService;
import world.ucode.models.Lot;
import world.ucode.models.Role;
import world.ucode.models.Search;
import world.ucode.models.User;
import world.ucode.services.LotService;
import world.ucode.utils.CreateJSON;
import world.ucode.utils.ImageHandler;
import world.ucode.utils.SendMail;
import world.ucode.utils.Token;
import world.ucode.services.UserService;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collections;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Controller
@ControllerAdvice
public class ModelController {
    ModelAndView mav = new ModelAndView();
    SendMail sendMail = new SendMail();
    LotService lotService = new LotService();
//    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
    @Autowired
    UserService userService;
    BidService bidService = new BidService();
    CreateJSON createJSON = new CreateJSON();
//    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//    UserService userService = context.getBean("userService", UserService.class);

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "/index";
    }

    /**
     * ----------------------- helpful functional
     */
    private ModelAndView pageModelAndView(String login, String page) {
        ModelAndView mav = new ModelAndView();
        System.out.println(login);
        try {
            ObjectMapper mapper = new ObjectMapper();
            User user = userService.findUserByLogin(login);
            String json = mapper.writeValueAsString(user);
            mav.addObject("user", json);
            mav.setViewName(page);
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Bad JSON");
            mav.setViewName("/errors/error");
            return mav;
        }
    }
    private ModelAndView pageModelAndView(int lotId, String page) {
        ModelAndView mav = new ModelAndView();
        System.out.println(lotId);
        try {
            ObjectMapper mapper = new ObjectMapper();
//            Lot lot = userService.findLotById(lotId);
//            String json = mapper.writeValueAsString(lot);
//            mav.addObject("lot", json);
            mav.setViewName(page);
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Bad JSON");
            mav.setViewName("/errors/error");
            return mav;
        }
    }

    // -----------------------
    @RequestMapping(value = "/authorization", method = RequestMethod.GET)
    public String signin(ModelMap model) throws UnknownHostException {
//        model.addAttribute("form", new User());
        return "/authorization";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView main(ModelMap model) {
        if (!model.containsAttribute("search")) {
            model.addAttribute("search", new Search());
        }
        ModelAndView mav = new ModelAndView();
        try {
            ObjectMapper mapper = new ObjectMapper();
//            Lot lot = userService.findLotById(Integer.parseInt(lotId));
//            String json = mapper.writeValueAsString(lot);

            List<Lot> lots = lotService.findAllLots();
            JSONArray json = createJSON.mainShowLotsJSON(lots);

            mav.addObject("lots", json);
            mav.setViewName("/main");

            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Bad JSON");
            mav.setViewName("/errors/error");
            return mav;
        }
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView profile(ModelMap model, HttpServletRequest request) {
        String login = request.getUserPrincipal().getName();
        ModelAndView mav = new ModelAndView();
        try {
            if (login != null && !login.equals("")) {
                ObjectMapper mapper = new ObjectMapper();
                User user = userService.findUserByLogin(login);
                String json = mapper.writeValueAsString(user);
                mav.addObject("user", json);

                List<Lot> lots = lotService.findAllLotsByUser(login);
                JSONArray jsonArr = createJSON.mainShowLotsJSON(lots);
                mav.addObject("lots", jsonArr);
            }
            mav.setViewName("/profile");
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Bad JSON");
            mav.setViewName("/errors/error");
            return mav;
        }
    }

    /**
     * requires unique user login, which profile needs to be showed.
     * */
    @RequestMapping(value = "/viewProfile", method = RequestMethod.GET)
    public ModelAndView viewProfile(@RequestParam String login) {
        return pageModelAndView(login, "/viewProfile");
    }

    /**
     * requires unique seller login (feedbacks about what seller).
     * */
    @RequestMapping(value = "/feedbacks", method = RequestMethod.GET)
    public ModelAndView feedbacks(@RequestParam String login) {
        return pageModelAndView(login, "/feedbacks");
    }

    /**
     * requires unique lot id (to what lot was added feedback).
     * */
    @RequestMapping(value = "/addFeedback", method = RequestMethod.GET)
    public ModelAndView addFeedback(@RequestParam String lotId) {
        return pageModelAndView(Integer.parseInt(lotId), "/addFeedback");
    }
    /**
     * requires unique lot id (that auction show).
     * */
    @RequestMapping(value = "/auction", method = RequestMethod.GET)
    public ModelAndView auction(@RequestParam String lotId) {
        ModelAndView mav = new ModelAndView();
        try {
            Lot lot = userService.findLotById(Integer.parseInt(lotId));
            User user = lot.getSeller();
            JSONObject json = createJSON.auctionJSON(user, lot);
            mav.addObject("lot", json);
            mav.setViewName("/auction");
            return mav;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Bad JSON");
            mav.setViewName("/errors/error");
            return mav;
        }
    }

    /**
     * requires unique seller login (what seller added lot).
     * */
    @RequestMapping(value = "/addLot", method = RequestMethod.GET)
    public ModelAndView addLot(@RequestParam String login) {
        return pageModelAndView(login, "/addLot");
    }

    public static Timestamp addDays(Timestamp date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);// w ww.  j ava  2  s  .co m
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return new Timestamp(cal.getTime().getTime());

    }

    @RequestMapping(value = "/addLot", method = RequestMethod.POST)
    public ModelAndView addLot(Lot lot, @RequestParam("photo") MultipartFile file, HttpServletRequest request) throws IOException {
        User seller = userService.findUser(request.getUserPrincipal().getName());
        lot.setSeller(seller);
        lot.setImage(file.getBytes());  // Data truncation: Data too long for column 'image' at row 1
        ImageHandler.savePicture(file);  // проверка
        Timestamp curTime = new Timestamp(System.currentTimeMillis());
        curTime.setTime(curTime.getTime() + (2 * 60 * 60 * 1000));
        lot.setStartTime(curTime);
        lot.setFinishTime(addDays(curTime, lot.getDuration()));
        lot.setActive(true);
        seller.addLot(lot);
//        lot.setSeller(seller);
        lotService.saveLot(lot);
        mav.setViewName("redirect:/profile");
        return mav;
    }

    @RequestMapping(value = "/newBit", method = RequestMethod.POST)
    public ModelAndView newBid(Bid bid, HttpServletRequest request) throws JsonProcessingException {
        System.out.println(bid.getPrice());
        User bidder = userService.findUser(request.getUserPrincipal().getName());
        bid.setLot(lotService.findLot(1));
        bid.setActive(true);
        bidder.addBid(bid);
//        bid.setBidder(bidder);
        bidService.saveBid(bid);
        mav.setViewName("redirect:/main");
        return mav;
    }

    @RequestMapping(value = "confirmation{token}", method = RequestMethod.GET)
    public ModelAndView confirmation(@RequestParam("token") String token){
        ModelAndView modelAndView = new ModelAndView();
        User user = userService.validateToken(token);
        user.setVerification("verificated");
        userService.updateUser(user);
        modelAndView.setViewName("redirect:/");
        return modelAndView;
    }

    @RequestMapping(value = "/authorization", method = RequestMethod.POST)
    public ModelAndView signin_post(User user, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        ObjectMapper mapper = new ObjectMapper();
//        try {
            if (user.getType().equals("signin")) {
                User newUser = userService.validateUser(user);
                String json = mapper.writeValueAsString(newUser);
//                List<Lot> lotss = newUser.getLots();
//                for (Lot lot:lotss) {
//                    System.out.println(lot.getTitle());
//                }
//                List<Bid> bids = userService.findUser("4").getBids();
//                for (Bid bid:bids) {
//                    System.out.println(bid.getPrice());
//                }
                mav.addObject("user", json);
                List<Lot> lots = lotService.findAllLotsByUser(newUser.getLogin());
                JSONArray jsonArr = createJSON.mainShowLotsJSON(lots);
                mav.addObject("lots", jsonArr);
                mav.setViewName("/profile");
                response.addCookie(new Cookie("login", user.getLogin()));
            } else {
                Token token = new Token();
//                user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
                user.setToken(token.getJWTToken(user.getLogin()));
                sendMail.sendMail(user);
                userService.saveUser(user);
                String json = mapper.writeValueAsString(user);
                mav.addObject("user",json);
                mav.setViewName("/main");
                response.addCookie(new Cookie("login", user.getLogin()));
            }
            return mav;
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("NON authorized or incorrect mail");
//            mav.setViewName("/authorization");
//            return mav;
//        }
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView signup_post(User user, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView();
        ObjectMapper mapper = new ObjectMapper();
        if (user.getUserRole().equals("seller"))
            user.setRoles(Collections.singleton(Role.SELLER));
        else
            user.setRoles(Collections.singleton(Role.BIDDER));
        Token token = new Token();
                user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setToken(token.getJWTToken(user.getLogin()));
//        sendMail.sendMail(user);
        response.addCookie(new Cookie("login", user.getLogin()));
        userService.saveUser(user);
        String json = mapper.writeValueAsString(user);
        mav.addObject("user",json);
        mav.setViewName("/authorization");
        return mav;
    }
    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String signup(ModelMap model, HttpServletRequest request) throws UnknownHostException {
//        model.addAttribute("hallo", request.getUserPrincipal().getName());
//        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
//        model.addAttribute("form", new User());
        return "/registration";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search_post(@ModelAttribute("search") Search search, Model model) {
        System.out.println(search.getTitle());
        System.out.println(search.getStartPrice());
        System.out.println(search.getDuration());
        System.out.println(search.getStartTime());
        System.out.println(search.getDescription());

        return "redirect:/main";
    }


//    @RequestMapping(value="/upload", method=RequestMethod.GET)
//    public @ResponseBody String provideUploadInfo() {
//        return "Вы можете загружать файл с использованием того же URL.";
//    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("name") String name,
                                   @RequestParam("file") MultipartFile file){
        ImageHandler.savePicture(file);
        return "/main";
    }

//    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
//    public String uploadFileHandler(@RequestBody JSONObject jsonString)
////    public @ResponseBody String uploadFileHandler(@RequestParam("imageForm") MultipartFile multipartFile)
//            throws IOException, ServletException {
////        System.out.println("Uploaded: " + multipartFile.getSize() + " bytes");
////        System.out.println("JSON: " + text.toString());
//        System.out.println("text: " + jsonString.get("text"));
////        String img = (String) jsonString.get("image");
//        InputStream in = new ByteArrayInputStream(ImageHandler.serialize(jsonString.get("image")));
//        // get & process file
//        BufferedImage image = ImageIO.read(in);
//        System.out.println("image height: " + image.getHeight());
////        ImageIO.write(image, type, resp.getOutputStream());
//        return "/main";
//    }
    // -----------------------
    @RequestMapping(value = "/errors/404", method = RequestMethod.GET)
    public String error404() {
        return "/errors/404";
    }

    @RequestMapping(value = "/errors/error", method = RequestMethod.GET)
    public String exceptions() {
        return "/errors/error";
    }

}