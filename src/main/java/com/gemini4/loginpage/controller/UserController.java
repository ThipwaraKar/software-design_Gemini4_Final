package com.gemini4.loginpage.controller;

import com.gemini4.loginpage.model.Role;

import com.gemini4.loginpage.model.User;
import com.gemini4.loginpage.service.UserService;
import edu.gemini.app.ocs.OCS;
import edu.gemini.app.ocs.model.AstronomicalData;
import edu.gemini.app.ocs.model.DataProcRequirement;
import edu.gemini.app.ocs.model.SciencePlan;
import edu.gemini.app.ocs.model.StarSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value={"/", "/login" , "/login/user" }, method = {RequestMethod.GET})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }




    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "There is already a user registered with the email provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }


    @RequestMapping(value="/homePage", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        String role_result = "";
        for(Role role: user.getRoles()) {
            role_result += role.getRole();
            role_result += " ";
        }

        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")"+" "+user.getRoles());
        modelAndView.addObject("greeting","Welcome "+user.getName()+ " "+user.getLastName());
        modelAndView.addObject("role","Login as "+role_result);
        //  modelAndView.addObject("adminMessage","This Page is available to Users with "+user.getRoles()+ "Role");
        modelAndView.setViewName("homePage");
        return modelAndView;
    }



    /*@RequestMapping(value="/admin/adminHome", method = RequestMethod.GET)
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("adminMessage","This Page is available to Users with "+user.getRoles()+ "Role");
        modelAndView.setViewName("admin/adminHome");
        return modelAndView;
    }*/

    @RequestMapping(value="/user/userHome", method = RequestMethod.GET)
    public ModelAndView user(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByEmail(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("userMessage","This Page is available to Users with User Role");
        modelAndView.setViewName("user/userHome");
        return modelAndView;
    }
//
//    @RequestMapping(value="/submitSciPlan", method = RequestMethod.POST)
//    public ModelAndView submitSciencePlan(String creator,
//                                          int funding,
//                                          String objective, String location, String starsSystemInput, String startDate, String endDate,
//                                          String fileType, String quality, String colorType, double contrast, double brightness,
//    double saturation, double highlights, double exposure, double shadows, double whites, double blacks, double luminance, double hue, String submitter
//    ){
//        ModelAndView modelAndView = new ModelAndView();
//        //SciencePlan2 sp2 = new SciencePlan2();
//        SciencePlan sp = new SciencePlan();
//        //  DataProcRequirement dp = new DataProcRequirement(type, quality, );
////        StarSystem ss = new StarSystem();
//        OCS ocs = new OCS();
//
////        SciencePlan.TELESCOPELOC telescopeloc = SciencePlan.TELESCOPELOC.valueOf(location);
//
//        System.out.println(creator);
//        System.out.println(funding);
//        System.out.println(starsSystemInput);
//
//
//        DataProcRequirement dp = new DataProcRequirement();
//        sp.setCreator(creator);
//        sp.setSubmitter(submitter);
//        sp.setFundingInUSD(funding);
//        sp.setObjectives(objective);
//        System.out.println(startDate);
//        String[] startDateArr = startDate.split("T")[0].split("-");
//        String[] startTimeArr = startDate.split("T")[1].split(":");
//
//        Calendar startDateObj = Calendar.getInstance();
//        startDateObj.set(
//                Integer.parseInt(startDateArr[0]),
//                Integer.parseInt(startDateArr[1]),
//                Integer.parseInt(startDateArr[2]),
//                Integer.parseInt(startTimeArr[0]),
//                Integer.parseInt(startTimeArr[1]));
//
//        System.out.println(endDate);
//        String[] endDateArr = endDate.split("T")[0].split("-");
//        String[] endTimeArr = endDate.split("T")[1].split(":");
//
//        Calendar endDateObj = Calendar.getInstance();
//        endDateObj.set(
//                Integer.parseInt(endDateArr[0]),
//                Integer.parseInt(endDateArr[1]),
//                Integer.parseInt(endDateArr[2]),
//                Integer.parseInt(endTimeArr[0]),
//                Integer.parseInt(endTimeArr[1]));
//        sp.setStartDate(startDateObj.getTime());
//        sp.setEndDate(endDateObj.getTime());
//        StarSystem.CONSTELLATIONS star_temp = StarSystem.CONSTELLATIONS.Andromeda;
//        for(StarSystem.CONSTELLATIONS star : StarSystem.CONSTELLATIONS.values()) {
//            if(star.engName.equalsIgnoreCase(starsSystemInput)) {
//                star_temp = star;
//            }
//        }
//        sp.setStarSystem(star_temp);
//        sp.setDataProcRequirements(dp);
//        sp.setPlanNo(1);
//        sp.setSubmitter(submitter);
//        if(location.equalsIgnoreCase("chile")) {
//            sp.setTelescopeLocation(SciencePlan.TELESCOPELOC.CHILE);
//        } else if(location.equalsIgnoreCase("hawaii")) {
//            sp.setTelescopeLocation(SciencePlan.TELESCOPELOC.HAWAII);
//        } else {
//            sp.setTelescopeLocation(SciencePlan.TELESCOPELOC.CHILE);
//        }
//
//        dp.setFileType(fileType);
//        dp.setFileQuality(quality);
//        dp.setColorType(colorType);
//        dp.setContrast(contrast);
//        dp.setBrightness(brightness);
//        dp.setBrightness(saturation);
//        dp.setHighlights(highlights);
//        dp.setExposure(exposure);
//        dp.setShadows(shadows);
//        dp.setWhites(whites);
//        dp.setBlacks(blacks);
//        dp.setLuminance(luminance);
//        dp.setHue(hue);
//        /*sp.setCollaborator(collaborator);
//        sp.setFunding(funding);
//        sp.setObjective(objective);
//        sp.setStarSystem(starSystem);
//        sp.setStart(start);
//        sp.setEnd(end);
//        sp.setLocation(location);
//        sp.setBwcolor(bwcolor);
//        sp.setContrast(contrast);*/
//
//        sp.setStatus(SciencePlan.STATUS.SUBMITTED);
//        ocs.submitSciencePlan(sp);
//
//        System.out.println(">> "+ sp.getStarSystem());
//        System.out.println(ocs.getAllSciencePlans());
//        modelAndView.addObject("sp", sp);
//        modelAndView.setViewName("/Astronomer/createSciPlan");
//        return modelAndView;
//    }
//
//    @RequestMapping(value="/Astronomer/createSciPlan", method = RequestMethod.GET)
//    public ModelAndView createSciPlan(){
//        StarSystem.CONSTELLATIONS[] allStars = StarSystem.CONSTELLATIONS.values();
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("allStars", allStars);
//        modelAndView.setViewName("/Astronomer/createSciPlan");
//        return modelAndView;
//    }
//


//    validate sciplan
//    @RequestMapping(value="/SciObserver/validateSciPlan/{SciPlanNo}", method = RequestMethod.GET)
//    public ModelAndView validateSciPlanID(@PathVariable int SciPlanNo){
//      //  int id = 1;
//        OCS test = new OCS();
//        SciencePlan sp = test.getSciencePlanByNo(SciPlanNo);
//
////        ArrayList<SciencePlan> sciencePlans = test.getAllSciencePlans();
////        for (SciencePlan sp1 : sciencePlans) {
////            System.out.println(sp1);
////        }
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("sp", sp);
//        modelAndView.setViewName("/SciObserver/validateSciPlan");
//        return modelAndView;
//    }
//
//    @RequestMapping(value="/SciObserver/validateSciPlan", method = RequestMethod.GET)
//    public ModelAndView validateSciPlan(){
//        OCS ocs = new OCS();
//        ArrayList<SciencePlan> sciencePlansList = new ArrayList<SciencePlan>();
//        for (SciencePlan sp: ocs.getAllSciencePlans()) {
//            if(sp.getStatus().equals(SciencePlan.STATUS.SUBMITTED)) {
//                sciencePlansList.add(sp);
//            }
//        }
//        ModelAndView modelAndView = new ModelAndView();
////        modelAndView.addObject("AllspID", sp.getPlanNo());
//        //modelAndView.addObject("sp", sp);
//        modelAndView.addObject("SpList", sciencePlansList);
//        modelAndView.setViewName("/SciObserver/validateSciplan");
//        return modelAndView;
//    }
////    @RequestMapping(value="/SciObserver/validateSciPlan", method = RequestMethod.POST)
////    public ModelAndView validateSciPlan(String validate){
////        SciencePlan sp = new SciencePlan();
////        ModelAndView modelAndView = new ModelAndView();
////        if(validate=="true") {
////            sp.setStatus(SciencePlan.STATUS.VALIDATED);
////            modelAndView.addObject("status","Validated");
////        }
////        else {
////            sp.setStatus(SciencePlan.STATUS.INVALIDATED);
////            modelAndView.addObject("status","Rejected");
////        }
//////        modelAndView.addObject("AllspID", sp.getPlanNo());
//////        modelAndView.addObject("sp", sp);
////        return modelAndView;
////    }
//
//    @RequestMapping(value="/permit/{id}", method = RequestMethod.GET)
//    public ModelAndView permitSciPlan(@PathVariable int id){
//        //SciencePlan sp = new SciencePlan();
//        ModelAndView modelAndView = new ModelAndView();
//
//        OCS ocs = new OCS();
//        SciencePlan sp = ocs.getSciencePlanByNo(id);
//
//        ocs.updateSciencePlanStatus(sp.getPlanNo(), SciencePlan.STATUS.VALIDATED);
//
//        System.out.println(sp.getStatus());
//
//        modelAndView.addObject("status", "validated");
//        modelAndView.addObject("id", sp.getPlanNo());
//        modelAndView.setViewName("/SciObserver/validateResult");
//
//        return modelAndView;
//    }
//
//    @RequestMapping(value="/reject/{id}", method = RequestMethod.GET)
//    public ModelAndView rejectSciPlan(@PathVariable int id){
//        ModelAndView modelAndView = new ModelAndView();
//
//        OCS ocs = new OCS();
//        SciencePlan sp = ocs.getSciencePlanByNo(id);
//
//        ocs.updateSciencePlanStatus(sp.getPlanNo(), SciencePlan.STATUS.INVALIDATED);
//
//        System.out.println(sp.getStatus());
//
//        modelAndView.addObject("status", "invalidated");
//        modelAndView.addObject("id", sp.getPlanNo());
//        modelAndView.setViewName("/SciObserver/validateResult");
//        return modelAndView;
//    }


    //////////////////////////////////////COLLECT ASTR DATA////////////////////////////////////////////////////
//    @RequestMapping(value="/SciObserver/collectAstrData", method = RequestMethod.GET)
//    public ModelAndView collectAstrDataFindID() {
//        SciencePlan sp = new SciencePlan();
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("sp",sp);
//        modelAndView.setViewName("/SciObserver/selectSciPlanID");
//        return modelAndView;
//    }
//
//    @RequestMapping(value="/SciObserver/collectAstrData", method = RequestMethod.POST)
//    public ModelAndView collectAstrData(String SciPlanNo) {
//        OCS ocs = new OCS();
//        SciencePlan sp = ocs.getSciencePlanByNo(Integer.parseInt(SciPlanNo));
//        ArrayList<String> links = new ArrayList<String>();
//        try {
//            AstronomicalData data = sp.retrieveAstroData();
//            links = data.getAstronomicalDataLinks();
//        } catch (IOException e) {
//            System.out.println(e);
//        }
//
//        DataProcRequirement dataReq = sp.getDataProcRequirements();
//
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("links", links);
//        modelAndView.addObject("SciPlanNo",SciPlanNo);
//        modelAndView.addObject("dataReq", dataReq);
//        modelAndView.setViewName("/SciObserver/collectAstrData");
//        return modelAndView;
//    }


    /*@PostMapping("/login")
    public String login_user(@RequestParam("username") String username, @RequestParam("password") String password,
                             HttpSession session, ModelMap modelMap)
    {

        //User auser= UserRepository.findByUsernamePassword(username, password);
        User auser = userService.findByUsernamePassword(username, password);
        if(auser!=null)
        {
            String uname=auser.getEmail();
            String upass=auser.getPassword();

            if(username.equalsIgnoreCase(uname) && password.equalsIgnoreCase(upass))
            {
                session.setAttribute("username",username);
                return "dummy";
            }
            else
            {
                modelMap.put("error", "Invalid Account");
                return "login";
            }
        }
        else
        {
            modelMap.put("error", "Invalid Account");
            return "login";
        }

    }*/

}
