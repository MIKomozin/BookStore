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
import java.util.Objects;

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
        return bookRepository.findRecommendedBookAndSort(nextPage);
    }

    public Page<Book> getPagePopularBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findPopularBookAndSort(nextPage);
    }

    //без даты публикации. Метод подходит для слайдера новинок на главной странице
    //и для начальной страницы новинок, так как параметры from и to null
    public Page<Book> getPageRecentBooks(Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findAllBooksAndSortByPubDate(nextPage);
    }

    //с датой публикации или без нее
    public Page<Book> getPageRecentBooksByPubDateBetween(Date from, Date to, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        if (from != null && to != null) {
        return bookRepository.findBooksByPubDateBetween(from, to, nextPage);
        } else if (from != null) {
            return bookRepository.findBooksByPubDateAfter(from, nextPage);
        } else if (to != null) {
            return bookRepository.findBooksByPubDateBefore(to, nextPage);
        } else {
            return bookRepository.findAllBooksAndSortByPubDate(nextPage);
        }
    }

    public Page<Book> getPageOfSearchResultBooks(String searchWord, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return bookRepository.findBooksByTitleContaining(searchWord, nextPage);
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
    public Double getRatingBookBySlug(String slug) {
        Integer sumRating = bookRepository.findSumRatingBookBySlug(slug); //суммарный рейтинг книги как произведение кол-ва пользователей на кол-во выбранных звезд
        Integer numberOfUsers = bookRepository.findNumberOfUsersGiveRatingBookBySlug(slug); //суммарное кол-во пользователей поставивших рейтинг
        if (sumRating == null || numberOfUsers == null || numberOfUsers == 0) {
            return 0.0;
        } else {
            return (sumRating * 1.0) / numberOfUsers;
        }
    }

    //рейтинг книги для отображения. Округляется до ближайщего целого в пределах от 0 до 5 вкл.
    public Integer getRatingBookBySlugForShow(String slug) {
        return Math.toIntExact(Math.round(getRatingBookBySlug(slug)));
    }

    //суммарное количество звезд у книги (суммарный рейтинг)
    public Integer getSumRatingBookBySlug(String slug) {
        Integer sumRating = bookRepository.findSumRatingBookBySlug(slug); //суммарный рейтинг книги как произведение кол-ва пользователей на кол-во выбранных звезд
        return Objects.requireNonNullElse(sumRating, 0);
    }

    //колличество пользователей выбравших определенный рейтинг для книги (1, 2, 3, 4 или 5 звезд)
    public Integer getNumberOfUsersBookBySlugAndByNumberOfStars(String slug, Integer numberOfStars) {
        Integer numberOfUsersByStars = bookRepository.findNumberOfUsersBookBySlugAndByNumberOfStars(slug, numberOfStars);
        return Objects.requireNonNullElse(numberOfUsersByStars, 0);
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

    //метод для подсчета популярности книги
    public Double getPopIndex(Integer id) {
        Double popIndex = bookRepository.findPopIndex(id);
        return Objects.requireNonNullElse(popIndex, 0.0);
    }
}
