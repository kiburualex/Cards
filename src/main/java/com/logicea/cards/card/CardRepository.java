package com.logicea.cards.card;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Alex Kiburu
 */
public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("""
        FROM Card m WHERE createdOn BETWEEN to_date(:start,'YYYY-MM-DD') AND to_date(:end,'YYYY-MM-DD')\s
        AND ( lower(m.owner) LIKE  CASE WHEN :role='USER' THEN :owner ELSE '%%' END )
        """)
    public Page<Card> findByDate(
            @Param("start") String start,
            @Param("end") String end,
            Pageable pageable,
            String role,
            String owner);

    @Query("""
            FROM Card m WHERE createdOn BETWEEN to_date(:start,'YYYY-MM-DD') AND to_date(:end,'YYYY-MM-DD')\s
            AND ( lower(m.name) LIKE %:search% or lower(m.color) LIKE %:search% or lower(m.status) LIKE %:search%)
            AND ( lower(m.owner) LIKE  CASE WHEN :role='USER' THEN :owner ELSE '%%' END )
        """)
    public Page<Card> findByDateAndSearch(
            @Param("start") String start,
            @Param("end") String end,
            @Param("search") String search,
            Pageable pageable,
            String role,
            String owner);

    @Query("""
        FROM Card m WHERE ( lower(m.name) LIKE %:search% or lower(m.color) LIKE %:search% or lower(m.status) LIKE %:search%)\s
        AND ( lower(m.owner) LIKE  CASE WHEN :role='USER' THEN :owner ELSE '%%' END )
        """)
    public Page<Card> findBySearch(
            @Param("search") String search,
            Pageable pageable,
            String role,
            String owner);

    @Query("""
            FROM Card m WHERE lower(m.owner) LIKE  CASE WHEN :role='USER' THEN :owner ELSE '%%' END 
            """)
    public Page<Card> findAll(
            Pageable pageable,
            String role,
            String owner);
}
