package com.github.covidalert.microservicetemplate.repositories;

import com.github.covidalert.microservicetemplate.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoviesRepository extends JpaRepository<Movie, Long>
{
}
