package ru.mycrg.backend.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public class FileRepository extends PagingAndSortingRepository<File, Integer> {

}
