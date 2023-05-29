package Project2.controllers;

import Project2.dao.BookDAO;
import Project2.models.Book;
import Project2.services.BookService;
import Project2.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/books")
public class ControllerForBooks {
    private final BookService bookService;
    private final PersonService personService;
    @Autowired
    public ControllerForBooks(BookDAO bookDAO, BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping("")
    public String showBooks(Model model,
                            @RequestParam(name = "page", required = false) Integer page,
                            @RequestParam(name = "size", required = false) Integer size,
                            @RequestParam(name = "sort_by_year", required = false, defaultValue = "false") boolean sort_by_year) {
        if (page == null || size == null) {
            model.addAttribute("bookList", bookService.findAllBooks());
        } else {
            if (sort_by_year) {
                model.addAttribute("bookList", bookService.findAllSortedBooks(PageRequest.of(page, size)));
            } else {
                model.addAttribute("bookList", bookService.findAllBooks(PageRequest.of(page, size)));
            }
        }
        return "/books/showBooks";
    }

    @GetMapping("/new")
    public String addBook(Model model) {
        Book book = new Book();
        model.addAttribute("book", book);
        return "/books/addBook";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findBookById(id));
        model.addAttribute("person", bookService.checkBookOwner(id));
        model.addAttribute("people", personService.findAllPerson());
        return "/books/showBook";
    }

    @GetMapping("/search")
    public String searchBook() {
        return "/books/searchBook";
    }

    @PostMapping("/search")
    public String postSearchingBook(Model model,
                                    @RequestParam("query") String query) {
        model.addAttribute("book", bookService.getBooksBySearch(query));
        System.out.println(query);
        System.out.println(bookService.getBooksBySearch(query));
        return "/books/searchBook";
    }

    @PostMapping()
    public String postNewBook(@ModelAttribute("book") Book book) {
        bookService.addBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String editBook(@PathVariable("id") int id, Model model) {
        model.addAttribute("book", bookService.findBookById(id));
        return "/books/editBook";
    }

    @PatchMapping("/{id}")
    public String patchBook(@ModelAttribute("book") Book book, @PathVariable("id") int id) {
        bookService.updateBook(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @PostMapping("/{id}")
    public String deleteBookOwner(@PathVariable("id") int id) {
        bookService.deleteBookOwner(id);
        return "redirect:/books/{id}";
    }

    @PostMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") int book_id, int personId) {
        bookService.assignBookToPerson(personId, book_id);
        return "redirect:/books/{id}";
    }
}

