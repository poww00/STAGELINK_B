package com.pro.repository;

import com.pro.dto.MyLikedShowProjection;
import com.pro.entity.ShowLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MyLikedShowRepository extends JpaRepository<ShowLikes, Long> {

    @Query(value = """
    SELECT 
        s.SHOW_NO AS showNo,
        si.SHOW_NAME AS showName,
        si.SHOW_POSTER AS poster,
        CONCAT(DATE_FORMAT(s.SHOW_START_TIME, '%Y.%m.%d'), ' ~ ', DATE_FORMAT(s.SHOW_END_TIME, '%Y.%m.%d')) AS period,
        CASE WHEN s.SHOW_START_TIME > NOW() THEN TRUE ELSE FALSE END AS available
    FROM TBL_LIKE sl
    JOIN TBL_SHOW s ON sl.SHOW_NO = s.SHOW_NO
    JOIN TBL_SHOWINFO si ON s.SHOW_INFO = si.SHOW_INFO
    WHERE sl.MEMBER = :memberId
""", nativeQuery = true)
    List<MyLikedShowProjection> findMyLikedShows(@Param("memberId") Long memberId);


}
