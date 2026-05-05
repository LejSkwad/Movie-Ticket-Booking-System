package com.mtb.movieservice.specification;
import com.mtb.movieservice.entity.Movie;
import com.mtb.movieservice.entity.MovieStatus;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class MovieSpecification {
    public static Specification<Movie> hasName(String name){
        return ((root, query, builder) -> {
            return builder.or(
                    builder.like(builder.lower(root.get("director")), "%" + name.toLowerCase() + "%"),
                    builder.like(builder.lower(root.get("title")), "%" + name.toLowerCase() + "%"));
        });
    }

    public static Specification<Movie> hasStatus(String status){
        return ((root, query, builder) -> {
            return builder.equal(root.get("status"), MovieStatus.valueOf(status.toUpperCase()));
        });
    }

    public static Specification<Movie> hasAllGenres(List<Long> genreIds) {
        return (root, query, builder) -> {
            Predicate[] predicates = genreIds.stream()
                    .map(genreId -> {
                        Subquery<Long> sub = query.subquery(Long.class);
                        Root<Movie> subRoot = sub.correlate(root); // link với outer query
                        Join<Object, Object> join = subRoot.join("genres");
                        sub.select(join.<Long>get("id"))
                                .where(builder.equal(join.get("id"), genreId));
                        return builder.exists(sub);
                    })
                    .toArray(Predicate[]::new);

            return builder.and(predicates);
        };
    }
}
