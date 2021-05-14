package com.gemini4.loginpage.controller;

import edu.gemini.app.ocs.OCS;
import edu.gemini.app.ocs.model.DataProcRequirement;
import edu.gemini.app.ocs.model.SciencePlan;
import edu.gemini.app.ocs.model.StarSystem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Calendar;

@Controller
public class ScienceplanController {
    @RequestMapping(value="/submitSciPlan", method = RequestMethod.POST)
    public ModelAndView submitSciencePlan(String creator,
                                          int funding,
                                          String objective, String location, String starsSystemInput, String startDate, String endDate,
                                          String fileType, String quality, String colorType, double contrast, double brightness,
                                          double saturation, double highlights, double exposure, double shadows, double whites, double blacks, double luminance, double hue, String submitter
    ){
        ModelAndView modelAndView = new ModelAndView();
        SciencePlan sp = new SciencePlan();
        OCS ocs = new OCS();

        System.out.println(creator);
        System.out.println(funding);
        System.out.println(starsSystemInput);

        DataProcRequirement dp = new DataProcRequirement();
        sp.setCreator(creator);
        sp.setSubmitter(submitter);
        sp.setFundingInUSD(funding);
        sp.setObjectives(objective);
        System.out.println(startDate);
        String[] startDateArr = startDate.split("T")[0].split("-");
        String[] startTimeArr = startDate.split("T")[1].split(":");

        Calendar startDateObj = Calendar.getInstance();
        startDateObj.set(
                Integer.parseInt(startDateArr[0]),
                Integer.parseInt(startDateArr[1]),
                Integer.parseInt(startDateArr[2]),
                Integer.parseInt(startTimeArr[0]),
                Integer.parseInt(startTimeArr[1]));

        System.out.println(endDate);
        String[] endDateArr = endDate.split("T")[0].split("-");
        String[] endTimeArr = endDate.split("T")[1].split(":");

        Calendar endDateObj = Calendar.getInstance();
        endDateObj.set(
                Integer.parseInt(endDateArr[0]),
                Integer.parseInt(endDateArr[1]),
                Integer.parseInt(endDateArr[2]),
                Integer.parseInt(endTimeArr[0]),
                Integer.parseInt(endTimeArr[1]));
        sp.setStartDate(startDateObj.getTime());
        sp.setEndDate(endDateObj.getTime());
        StarSystem.CONSTELLATIONS star_temp = StarSystem.CONSTELLATIONS.Andromeda;
        for(StarSystem.CONSTELLATIONS star : StarSystem.CONSTELLATIONS.values()) {
            if(star.engName.equalsIgnoreCase(starsSystemInput)) {
                star_temp = star;
            }
        }
        sp.setStarSystem(star_temp);
        sp.setDataProcRequirements(dp);
        sp.setPlanNo(1);
        if(location.equalsIgnoreCase("chile")) {
            sp.setTelescopeLocation(SciencePlan.TELESCOPELOC.CHILE);
        } else if(location.equalsIgnoreCase("hawaii")) {
            sp.setTelescopeLocation(SciencePlan.TELESCOPELOC.HAWAII);
        } else {
            sp.setTelescopeLocation(SciencePlan.TELESCOPELOC.CHILE);
        }

        dp.setFileType(fileType);
        dp.setFileQuality(quality);
        dp.setColorType(colorType);
        dp.setContrast(contrast);
        dp.setBrightness(brightness);
        dp.setBrightness(saturation);
        dp.setHighlights(highlights);
        dp.setExposure(exposure);
        dp.setShadows(shadows);
        dp.setWhites(whites);
        dp.setBlacks(blacks);
        dp.setLuminance(luminance);
        dp.setHue(hue);

        sp.setStatus(SciencePlan.STATUS.SUBMITTED);
        ocs.submitSciencePlan(sp);

        System.out.println(">> "+ sp.getStarSystem());
        System.out.println(ocs.getAllSciencePlans());
        modelAndView.addObject("sp", sp);
        modelAndView.setViewName("/Astronomer/createSciPlan");
        return modelAndView;
    }


    @RequestMapping(value="/Astronomer/createSciPlan", method = RequestMethod.GET)
    public ModelAndView createSciPlan(){
        StarSystem.CONSTELLATIONS[] allStars = StarSystem.CONSTELLATIONS.values();
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allStars", allStars);
        modelAndView.setViewName("/Astronomer/createSciPlan");
        return modelAndView;
    }


}
