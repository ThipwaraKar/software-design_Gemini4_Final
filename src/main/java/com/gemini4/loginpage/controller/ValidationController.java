package com.gemini4.loginpage.controller;

import edu.gemini.app.ocs.OCS;
import edu.gemini.app.ocs.model.SciencePlan;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@RestController
public class ValidationController {
    @RequestMapping(value="/SciObserver/validateSciPlan/{SciPlanNo}", method = RequestMethod.GET)
    public ModelAndView validateSciPlanID(@PathVariable int SciPlanNo){
        //  int id = 1;
        OCS test = new OCS();
        SciencePlan sp = test.getSciencePlanByNo(SciPlanNo);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("sp", sp);
        modelAndView.setViewName("/SciObserver/validateSciPlan");
        return modelAndView;
    }

    @RequestMapping(value="/SciObserver/validateSciPlan", method = RequestMethod.GET)
    public ModelAndView validateSciPlan(){
        OCS ocs = new OCS();
        ArrayList<SciencePlan> sciencePlansList = new ArrayList<SciencePlan>();
        for (SciencePlan sp: ocs.getAllSciencePlans()) {
            if(sp.getStatus().equals(SciencePlan.STATUS.SUBMITTED)) {
                sciencePlansList.add(sp);
            }
        }
        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("AllspID", sp.getPlanNo());
        //modelAndView.addObject("sp", sp);
        modelAndView.addObject("SpList", sciencePlansList);
        modelAndView.setViewName("/SciObserver/validateSciplan");
        return modelAndView;
    }
//    @RequestMapping(value="/SciObserver/validateSciPlan", method = RequestMethod.POST)
//    public ModelAndView validateSciPlan(String validate){
//        SciencePlan sp = new SciencePlan();
//        ModelAndView modelAndView = new ModelAndView();
//        if(validate=="true") {
//            sp.setStatus(SciencePlan.STATUS.VALIDATED);
//            modelAndView.addObject("status","Validated");
//        }
//        else {
//            sp.setStatus(SciencePlan.STATUS.INVALIDATED);
//            modelAndView.addObject("status","Rejected");
//        }
////        modelAndView.addObject("AllspID", sp.getPlanNo());
////        modelAndView.addObject("sp", sp);
//        return modelAndView;
//    }

    @RequestMapping(value="/permit/{id}", method = RequestMethod.GET)
    public ModelAndView permitSciPlan(@PathVariable int id){
        //SciencePlan sp = new SciencePlan();
        ModelAndView modelAndView = new ModelAndView();

        OCS ocs = new OCS();
        SciencePlan sp = ocs.getSciencePlanByNo(id);

        ocs.updateSciencePlanStatus(sp.getPlanNo(), SciencePlan.STATUS.VALIDATED);

        System.out.println(sp.getStatus());

        modelAndView.addObject("status", "validated");
        modelAndView.addObject("id", sp.getPlanNo());
        modelAndView.setViewName("/SciObserver/validateResult");

        return modelAndView;
    }

    @RequestMapping(value="/reject/{id}", method = RequestMethod.GET)
    public ModelAndView rejectSciPlan(@PathVariable int id){
        ModelAndView modelAndView = new ModelAndView();

        OCS ocs = new OCS();
        SciencePlan sp = ocs.getSciencePlanByNo(id);

        ocs.updateSciencePlanStatus(sp.getPlanNo(), SciencePlan.STATUS.INVALIDATED);

        System.out.println(sp.getStatus());

        modelAndView.addObject("status", "invalidated");
        modelAndView.addObject("id", sp.getPlanNo());
        modelAndView.setViewName("/SciObserver/validateResult");
        return modelAndView;
    }
}
