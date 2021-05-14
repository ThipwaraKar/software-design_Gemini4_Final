package com.gemini4.loginpage.controller;

import edu.gemini.app.ocs.OCS;
import edu.gemini.app.ocs.model.AstronomicalData;
import edu.gemini.app.ocs.model.DataProcRequirement;
import edu.gemini.app.ocs.model.SciencePlan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class CollectAstronomicalData {
    @RequestMapping(value="/SciObserver/collectAstrData", method = RequestMethod.GET)
    public ModelAndView collectAstrDataFindID() {
        SciencePlan sp = new SciencePlan();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("sp",sp);
        modelAndView.setViewName("/SciObserver/selectSciPlanID");
        return modelAndView;
    }

    @RequestMapping(value="/SciObserver/collectAstrData", method = RequestMethod.POST)
    public ModelAndView collectAstrData(String SciPlanNo) {
        OCS ocs = new OCS();
        SciencePlan sp = ocs.getSciencePlanByNo(Integer.parseInt(SciPlanNo));
        ArrayList<String> links = new ArrayList<String>();
        try {
            AstronomicalData data = sp.retrieveAstroData();
            links = data.getAstronomicalDataLinks();
        } catch (IOException e) {
            System.out.println(e);
        }

        DataProcRequirement dataReq = sp.getDataProcRequirements();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("links", links);
        modelAndView.addObject("SciPlanNo",SciPlanNo);
        modelAndView.addObject("dataReq", dataReq);
        modelAndView.setViewName("/SciObserver/collectAstrData");
        return modelAndView;
    }
}
