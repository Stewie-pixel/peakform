package com.peakform.repository;

import com.peakform.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("SELECT m FROM Member m WHERE lower(m.fullName) LIKE lower(concat('%', :term, '%'))")
    List<Member> secureSearchByName(@Param("term") String term);

    @Procedure(procedureName = "sp_search_members_by_name")
    List<Member> secureSearchByNameViaStoredProcedure(@Param("search_term") String term);

    List<Member> findBySuburbIgnoreCase(String suburb);
}
