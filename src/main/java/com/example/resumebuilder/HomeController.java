package com.example.resumebuilder;

import com.example.resumebuilder.models.Job;
import com.example.resumebuilder.models.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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


        List<Job> jobs = new ArrayList<>();
        jobs.add(job1);
        jobs.add(job2);
        userProfile.getJobs().clear();
        userProfile.getJobs().add(job1);
        userProfile.getJobs().add(job2);

        repository.save(userProfile);



        return "profile";
    }

    @GetMapping("/edit")
    public String edit(){
        return "edit page";
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

