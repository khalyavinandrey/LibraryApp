package Project2.controllers;

import Project2.dao.PersonDAO;
import Project2.models.Person;
import Project2.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class ControllerForPeople {
    private final PersonService personService;

    @Autowired
    public ControllerForPeople(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("")
    public String showPeople(Model model) {
        model.addAttribute("personList", personService.findAllPerson());
        return "/people/showPeople";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.findPersonById(id));
        model.addAttribute("bookList", personService.getBookList(id));
        return "/people/showPersonById";
    }

    @GetMapping("/new")
    public String createPerson(Model model) {
        Person person = new Person();
        model.addAttribute("person", person);
        return "/people/createPerson";
    }

    @PostMapping()
    public String postNewPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/people/new";
        }
        personService.createPerson(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String updatePerson(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.findPersonById(id));
        return "/people/updatePerson";
    }

    @PatchMapping("/{id}")
    public String patchPerson(@ModelAttribute("person") @Valid Person person, @PathVariable("id") int id,
                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "redirect:/people/{id}";
        }

        personService.updatePerson(person, id);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        personService.deletePerson(id);
        return "redirect:/people";
    }
}
