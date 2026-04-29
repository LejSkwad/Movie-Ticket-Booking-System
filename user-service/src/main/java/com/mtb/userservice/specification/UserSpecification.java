package com.mtb.userservice.specification;

import com.mtb.userservice.entity.Role;
import com.mtb.userservice.entity.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;


public class UserSpecification {
    public static Specification<User> hasSearch(String search){
        return ((root, query, builder) -> {
            return builder.or(
                    builder.like(builder.lower(root.get("email")), "%" + search.toLowerCase() + "%"),
                    builder.like(builder.lower(root.get("fullName")), "%" + search.toLowerCase() + "%")
            );
        });
    }

    public static Specification<User> hasRole(String role){
        return ((root, query, builder) -> {
            return builder.equal(root.get("role"), Role.valueOf(role.toUpperCase()));
        });
    }

    public static Specification<User> hasStatus(Boolean isActive){
        return ((root, query, builder) -> {
            return builder.equal(root.get("isActive"), isActive);
        });
    }

    public static Specification<User> createdBetween(LocalDate from, LocalDate to){
        return ((root, query, builder) -> {
            if(from != null && to != null){
                return builder.between(root.get("createdAt"), from, to);
            }
            if (from != null){
                return builder.greaterThanOrEqualTo(root.get("createdAt"), from);
            }
            return builder.lessThanOrEqualTo(root.get("createdAt"), to);
        });
    }
}
