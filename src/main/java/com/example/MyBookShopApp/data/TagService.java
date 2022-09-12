package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.entity.Book;
import com.example.MyBookShopApp.data.entity.Tag;
import com.example.MyBookShopApp.data.repository.BookRepository;
import com.example.MyBookShopApp.data.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TagService {

    private final TagRepository tagRepository;
    private final BookRepository bookRepository;

    @Autowired
    public TagService(TagRepository tagRepository, BookRepository bookRepository) {
        this.tagRepository = tagRepository;
        this.bookRepository = bookRepository;
    }

    //метод для отображения всех тегов из БД
    public List<Tag> getTagData() {
        return tagRepository.findAll();
    }

    //метод для нахождения тэга по ID
    public Tag getTagByTagName(String tagName) {
        return tagRepository.findTagByTagName(tagName);
    }

    //список книг для определенного тэга
    public Page<Book> getBooksByTagName(String tagName, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);
        return  bookRepository.findBooksByTagName(tagName, nextPage);
    }
}

