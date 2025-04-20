package com.luv2code.springboot.cruddemo.controller;


import com.luv2code.springboot.cruddemo.entity.Employee;
import com.luv2code.springboot.cruddemo.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }
    @GetMapping("/list")
    public  String listEmployees(Model model){
        //get emp from db
        List<Employee> employees = employeeService.findAll();

        // add to spring model
        model.addAttribute("employees", employees);

        return  "employees/list-Employees";
    }

    @GetMapping("/showFormForAdd")
    public  String showFormForAdd(Model model){

        Employee emp = new Employee();
        model.addAttribute("employee", emp);
        return "employees/employee-form";
    }

    @GetMapping("/showFormForUpdate")
    public  String showFormForUpdate(@RequestParam("employeeId") int id, Model model){

        Employee emp  = employeeService.findById(id);

        model.addAttribute("employee", emp);

        return  "employees/employee-form";
    }

    @PostMapping("/save")
    public String saveEmployee(@ModelAttribute("employee") Employee emp){
        employeeService.save(emp);

        return "redirect:/employees/list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("employeeId") int id){
        employeeService.deleteById(id);
        return "redirect:/employees/list";
    }
}
