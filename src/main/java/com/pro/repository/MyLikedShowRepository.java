package com.pro.repository;

import com.pro.dto.MyLikedShowDto;
import com.pro.entity.ShowLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MyLikedShowRepository extends JpaRepository<ShowLikes, Long> {

    @Query(value = """
        SELECT 
            si.SHOW_INFO AS showNo,
            si.SHOW_NAME AS showName,
            si.SHOW_POSTER AS poster,
            CONCAT(DATE_FORMAT(s.SHOW_START_TIME, '%Y.%m.%d'), ' ~ ', DATE_FORMAT(s.SHOW_END_TIME, '%Y.%m.%d')) AS period,
            CASE WHEN s.SHOW_START_TIME > NOW() THEN 'TRUE' ELSE 'FALSE' END AS available
        FROM TBL_LIKE sl
        JOIN TBL_SHOWINFO si ON sl.SHOW_NO = si.SHOW_INFO
        JOIN TBL_SHOW s ON si.SHOW_INFO = s.SHOW_INFO
        WHERE sl.MEMBER = :memberId
    """, nativeQuery = true)
    List<Object[]> findMyLikedShows(@Param("memberId") Long memberId);
}
