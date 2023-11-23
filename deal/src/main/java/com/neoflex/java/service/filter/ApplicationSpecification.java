package com.neoflex.java.service.filter;

import com.neoflex.java.dto.FindApplicationDTO;
import com.neoflex.java.model.Application;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplicationSpecification {
    public static Specification<Application> getSpec(FindApplicationDTO filter) {
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!ObjectUtils.isEmpty(filter.getStatus()))
                predicates.add(criteriaBuilder.equal(root.get("status"), filter.getStatus()));

            if (!ObjectUtils.isEmpty(filter.getMaritalStatus()))
                predicates.add(criteriaBuilder.equal(root.get("client").get("maritalStatus"), filter.getMaritalStatus()));

            if (!ObjectUtils.isEmpty(filter.getGender()))
                predicates.add(criteriaBuilder.equal(root.get("client").get("gender"), filter.getGender()));

            if (!ObjectUtils.isEmpty(filter.getAmount()))
                predicates.add(criteriaBuilder.greaterThan(root.get("credit").get("amount"), filter.getAmount()));

            if (!ObjectUtils.isEmpty(filter.getTerm()))
                predicates.add(criteriaBuilder.greaterThan(root.get("credit").get("term"), filter.getTerm()));

            if (!ObjectUtils.isEmpty(filter.getIsInsuranceEnabled()))
                predicates.add(criteriaBuilder.equal(root.get("credit").get("isInsuranceEnabled"), filter.getIsInsuranceEnabled()));

            if (!ObjectUtils.isEmpty(filter.getIsSalaryClient()))
                predicates.add(criteriaBuilder.equal(root.get("credit").get("isSalaryClient"), filter.getIsSalaryClient()));

            if (CollectionUtils.isEmpty(predicates)) return query.where().getRestriction();
            return query.where(criteriaBuilder.and(predicates.toArray(Predicate[]::new))).getRestriction();
        });
    }
}
