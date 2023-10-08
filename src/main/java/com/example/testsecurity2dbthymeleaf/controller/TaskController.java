package com.example.testsecurity2dbthymeleaf.controller;

import com.example.testsecurity2dbthymeleaf.entity.Action;
import com.example.testsecurity2dbthymeleaf.entity.Task;
import com.example.testsecurity2dbthymeleaf.entity.User;
import com.example.testsecurity2dbthymeleaf.repository.LogsRepository;
import com.example.testsecurity2dbthymeleaf.repository.TaskRepository;
import com.example.testsecurity2dbthymeleaf.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LogsRepository logsRepository;

    @GetMapping("/list")
    public ModelAndView getAllUserTasks() {
        log.info("/list -> connection");
        ModelAndView mav =  new ModelAndView("list-tasks");
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Task> userTasks = userRepository.findByEmail(login).getTasks();
        mav.addObject("tasks", userTasks);
        return mav;
    }

    @GetMapping("/addTaskForm")
    public ModelAndView addTaskForm() {
        ModelAndView mav = new ModelAndView("add-task-form");
        Task task = new Task();
        mav.addObject("task", task);
        return mav;
    }

    @PostMapping("/saveTask")
    public String saveTask(@ModelAttribute Task task) {
        taskRepository.save(task);
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(login);
        List<Task> taskList = currentUser.getTasks();
        taskList.add(task);
        currentUser.setTasks(taskList);
        userRepository.save(currentUser);
        return "redirect:/list";
    }

    @GetMapping("/showUpdateForm")
    public ModelAndView showUpdateForm(@RequestParam Long taskId) {
        ModelAndView mav = new ModelAndView("add-task-form");
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        Task task = new Task();
        if (optionalTask.isPresent()) {
            task = optionalTask.get();
        }
        mav.addObject("task", task);
        return mav;
    }

    @GetMapping("/deleteTask")
    public String deleteTask(@RequestParam Long taskId) {
        String login = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(login);
        List<Task> userTasks = currentUser.getTasks();
        userTasks.removeIf(task -> task.getId() == taskId);
        currentUser.setTasks(userTasks);
        userRepository.save(currentUser);
        taskRepository.deleteById(taskId);
        return "redirect:/list";
    }
}
