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

@Service
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> getBooksByAuthor(String authorName) {
        return bookRepository.findBooksByAuthorNameContaining(authorName);
    }

    public List<Book> getBooksByTitle(String title) throws BookStoreApiWrongParameterException {
        if (title.length() <= 1) {
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

    public Book getBookBySlug(String slug) {
        return bookRepository.findBooksBySlug(slug);
    }

    public Book save(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getBooksBySlugIn(String[] slugs) {
        return bookRepository.findBooksBySlugIn(slugs);
    }

    //цена всех выбранных книг без учета скидки
    public int getPriceOfAllBooks(List<Book> books) {
        int sumPrice = 0;
        for (Book book:books) {
            sumPrice = sumPrice + book.getPrice();
        }
        return sumPrice;
    }

    //сумма скидки для всех выбранных
    public int getDiscountPriceOfAllBooks(List<Book> books) {
        int sumDiscountPrice = 0;
        for (Book book:books) {
            sumDiscountPrice = sumDiscountPrice + book.discountPrice();
        }
        return sumDiscountPrice;
    }

    //рейтинг книги на основании голосов (от 1 до 5 включительно)
    public Integer getRatingBookBySlug(String slug) {
        Integer sumRating = bookRepository.findSumRatingBookBySlug(slug); //суммарный рейтинг книги как произведение кол-ва пользователей на кол-во выбранных звезд
        Integer numberOfUsers = bookRepository.findNumberOfUsersGiveRatingBookBySlug(slug); //суммарное кол-во пользователей поставивших рейтинг
        if (sumRating == null || numberOfUsers == null || numberOfUsers == 0) {
            return 0;
        } else {
            return Math.toIntExact(Math.round(Double.valueOf(sumRating) / numberOfUsers));
        }
    }

    //суммарное количество звезд у книги
    public Integer getSumRatingBookBySlug(String slug) {
        Integer sumRating = bookRepository.findSumRatingBookBySlug(slug); //суммарный рейтинг книги как произведение кол-ва пользователей на кол-во выбранных звезд
        if (sumRating == null) {
            return 0;
        } else {
            return sumRating;
        }
    }

    //колличество пользователей выбравших определенный рейтинг для книги (1, 2, 3, 4 или 5 звезд)
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

    public Integer getBookIdByReviewId(Integer reviewId) {
        return bookRepository.findBookIdByReviewId(reviewId);
    }

    //количество оставленных отзывов для определенной книги
    public Integer getNumberOfReviewsForBook(String slug) {
        Book book = getBookBySlug(slug);
        return book.getBookReviewList().size();//размер массива и есть количетсов отзывов для каждой книги
    }
}
