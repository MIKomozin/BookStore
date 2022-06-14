package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.entity.Book;
import com.example.MyBookShopApp.data.entity.Genre;
import com.example.MyBookShopApp.data.entity.Tag;
import com.example.MyBookShopApp.data.repository.BookRepository;
import com.example.MyBookShopApp.data.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    private GenreRepository genreRepository;
    private BookRepository bookRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository, BookRepository bookRepository) {
        this.genreRepository = genreRepository;
        this.bookRepository = bookRepository;
    }

    //для отображения всех жанров из БД
    public List<Genre> getGenresData() {
        return genreRepository.findAll();
    }

    //находим жанр по его идентификатору slug
    public Genre getGenreBySlug(String slugInd) {
        return genreRepository.findGenreBySlug(slugInd);
    }

    public Page<Book> getBooksByGenreSlug(String slugInd, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return  bookRepository.findBooksByGenreSlug(getGenreBySlug(slugInd).getSlug(),nextPage);
    }
}
