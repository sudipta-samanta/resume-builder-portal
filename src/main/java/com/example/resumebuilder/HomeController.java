package com.example.resumebuilder;

import com.example.resumebuilder.models.Education;
import com.example.resumebuilder.models.Job;
import com.example.resumebuilder.models.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    UserProfileRepository repository;

    @GetMapping("/")
    public String home(){
        Optional<UserProfile> profileOptional = repository.findByUserName("amit");
        profileOptional.orElseThrow(() -> new RuntimeException("Not found"));
        UserProfile userProfile = profileOptional.get();

        Job job1 = new Job();
        job1.setCompany("Company 1");
        job1.setDesignation("Designation");
        job1.setId(1);
        job1.setStartDate(LocalDate.of(2020, 1, 1));
        //job1.setEndDate(LocalDate.of(2020, 3, 1));
        job1.setCurrentJob(true);
        job1.getResponsibilities().add("Responsible for seasonal menus featuring local seafood");
        job1.getResponsibilities().add("Performed staff education on cooking techniques necessary for components and assembly during service");
        job1.getResponsibilities().add("Coordinated with FOH and staff to make everyone aware of possible menu allergies and nightly specials");

        Job job2 = new Job();
        job2.setCompany("Company 2");
        job2.setDesignation("Designation");
        job2.setId(2);
        job2.setStartDate(LocalDate.of(2019, 1, 1));
        job2.setEndDate(LocalDate.of(2019, 12, 30));
        job2.getResponsibilities().add("Maintain an operating COG of 27 percent");
        job2.getResponsibilities().add("Oversee all hiring for BOH employee");
        job2.getResponsibilities().add("Perform weekly food inventory");

        userProfile.getJobs().clear();
        userProfile.getJobs().add(job1);
        userProfile.getJobs().add(job2);

        Education e1 = new Education();
        e1.setStartDate(LocalDate.of(2019, 1, 1));
        e1.setEndDate(LocalDate.of(2019, 12, 30));
        e1.setCollege("College 1");
        e1.setQualification("some degree");
        e1.setSummary("Studied a lot");

        Education e2 = new Education();
        e2.setStartDate(LocalDate.of(2019, 1, 1));
        e2.setEndDate(LocalDate.of(2019, 12, 30));
        e2.setCollege("College 2");
        e2.setQualification("another degree");
        e2.setSummary("Studied a lot again");

        userProfile.getEducations().clear();
        userProfile.getEducations().add(e1);
        userProfile.getEducations().add(e2);

        userProfile.getSkills().clear();
        userProfile.getSkills().add("Recipes and menu planning");
        userProfile.getSkills().add("Signature dish creation");
        userProfile.getSkills().add("Kitchen Management");
        userProfile.getSkills().add("Food plating and presentation");

        repository.save(userProfile);



        return "profile";
    }

    @GetMapping("/edit")
    public String edit(Principal principal, Model model){
        String userId = principal.getName();

        Optional<UserProfile> userProfileOptional = repository.findByUserName(userId);
        userProfileOptional.orElseThrow(() -> new RuntimeException("Not found: " + userId));

        model.addAttribute("userId", userId);
        UserProfile userProfile = userProfileOptional.get();
        model.addAttribute("userProfile", userProfile);

        return "profile-edit";
    }

    @PostMapping("/edit")
    public String postEdit(Principal principal, Model model, @ModelAttribute UserProfile userProfile){
        String userName = principal.getName();
        Optional<UserProfile> userProfileOptional = repository.findByUserName(userName);
        userProfileOptional.orElseThrow(() -> new RuntimeException("Not found: " + userName));
        int id = userProfileOptional.get().getId();
        userProfile.setId(id);
        userProfile.setUserName(userName);
        repository.save(userProfile);
        // save updated values in form
        return "redirect:/view/" + userName;
    }

    @GetMapping("/view/{userId}")
    public String view(@PathVariable String userId, Model model){
        Optional<UserProfile> userProfileOptional = repository.findByUserName(userId);
        userProfileOptional.orElseThrow(() -> new RuntimeException("Not found: " + userId));

        model.addAttribute("userId", userId);
        UserProfile userProfile = userProfileOptional.get();
        model.addAttribute("userProfile", userProfile);
        //return "profile";

        return "profile-templates/" + userProfile.getTheme() + "/index";
    }
}

