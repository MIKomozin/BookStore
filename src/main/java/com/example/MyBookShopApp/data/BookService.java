package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.entity.Book;
import com.example.MyBookShopApp.data.repository.BookRepository;
import com.example.MyBookShopApp.errs.BookStoreApiWrongParameterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBookData() {
        return bookRepository.findAll();
    }

    //NEW BOOK SERVICE METHODS

    public List<Book> getBooksByAuthor(String authorName) {
        String container = "%"+authorName+"%";
        return bookRepository.findBooksByAuthorNameContaining(container);
    }

    public List<Book> getBooksByTitle(String title) throws BookStoreApiWrongParameterException {
        if (title.equals("") || title.length() <= 1) {
            throw new BookStoreApiWrongParameterException("Wrong values passed to one or more parameters");
        } else {
            List<Book> data = bookRepository.findBooksByTitleContaining(title);
            if (data.size() > 0) {
                return data;
            } else {
                throw new BookStoreApiWrongParameterException("No data found with specified parameters");
            }
        }
    }

    public List<Book> getBooksWithPriceBetween(Integer min, Integer max) {
        return bookRepository.findBooksByPriceBetween(min, max);
    }

    public List<Book> getBooksWithPrise(Integer price) {
        return bookRepository.findBooksByPriceIs(price);
    }

    public List<Book> getBooksWithMaxDiscount() {
        return bookRepository.getBooksWithMaxDiscount();
    }

    public List<Book> getBestsellers() {
        return bookRepository.getBestsellers();
    }

    public Page<Book> getPageRecommendedBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<Book> getPagePopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    public Page<Book> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByTitleContaining(searchWord, nextPage);
    }

    //RECENT
    //without pubDate
    public Page<Book> getPageRecentBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAll(nextPage);
    }

    //with pubDate
    public Page<Book> getPageRecentBooksByPubDateBetween(Date from, Date to, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByPubDateBetween(from, to, nextPage);
    }

    //only with pubDate from
    public Page<Book> getPageRecentBooksByPubDateAfter(Date from, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByPubDateAfter(from, nextPage);
    }

    //only with pubDate to
    public Page<Book> getPageRecentBooksByPubDateBefore(Date to, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByPubDateBefore(to, nextPage);
    }

    public Book getBookBySlug(String slug) {
        return bookRepository.findBooksBySlug(slug);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    //modul7 task 1
    public List<Book> getBooksBySlugIn(String[] slugs) {
        return bookRepository.findBooksBySlugIn(slugs);
    }

    public Integer getPriceOfAllBooks(List<Book> books) {
        Integer sumPrice = 0;
        for (Book book:books) {
            sumPrice = sumPrice + book.getPrice();
        }
        return sumPrice;
    }

    public Integer getDiscountPriceOfAllBooks(List<Book> books) {
        Integer sumDiscountPrice = 0;
        for (Book book:books) {
            sumDiscountPrice = sumDiscountPrice + book.discountPrice();
        }
        return sumDiscountPrice;
    }

    public Integer getRatingBookBySlug(String slug) {
        Integer sumRating = bookRepository.findSumRatingBookBySlug(slug); //суммарный рейтинг книги как произведение кол-ва пользователей на кол-во выбранных звезд
        Integer numberOfUsers = bookRepository.findNumberOfUsersGiveRatingBookBySlug(slug); //суммарное кол-во пользователей поставивших рейтинг
        if (sumRating == null || numberOfUsers == null || numberOfUsers == 0) {
            return 0;
        } else {
            Integer rating = Math.toIntExact(Math.round(Double.valueOf(sumRating) / numberOfUsers));
            return rating;
            //Logger.getLogger(this.getClass().getSimpleName()).info("суммарный рейтинг: " + sumRating);
            //Logger.getLogger(this.getClass().getSimpleName()).info("число пользователей " + numberOfUsers);
            //Logger.getLogger(this.getClass().getSimpleName()).info("рейтинг в звездах: " + rating);
        }
    }

    public Integer getSumRatingBookBySlug(String slug) {
        Integer sumRating = bookRepository.findSumRatingBookBySlug(slug); //суммарный рейтинг книги как произведение кол-ва пользователей на кол-во выбранных звезд
        if (sumRating == null) {
            return 0;
        } else {
            return sumRating;
        }
    }

    public Integer getNumberOfUsersBookBySlugAndByNumberOfStars(String slug, Integer numberOfStars) {
        Integer numberOfUsersByStars = bookRepository.findNumberOfUsersBookBySlugAndByNumberOfStars(slug, numberOfStars);
        if (numberOfUsersByStars == null) {
            return 0;
        } else {
            return numberOfUsersByStars;
        }
    }

    public Book getBookById(Integer id) {
        return bookRepository.findBooksById(id);
    }
}
