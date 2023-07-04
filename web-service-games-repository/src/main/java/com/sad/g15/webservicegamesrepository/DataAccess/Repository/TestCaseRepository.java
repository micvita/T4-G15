package com.sad.g15.webservicegamesrepository.DataAccess.Repository;

import com.sad.g15.webservicegamesrepository.DataAccess.Entity.TestCase;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;

public interface TestCaseRepository extends JpaRepository<TestCase, Integer>{

    @Transactional
    @Modifying(clearAutomatically=true)
    @Nullable
    @Query(value = "delete from test_case_player tcp where tcp.id = ?1", nativeQuery = true)
    public Integer deleteTestCasePlayer(int idTestCase);

    @Transactional
    @Modifying(clearAutomatically=true)
    @Nullable
    @Query(value = "delete from test_case_robot tcr where tcr.id = ?1", nativeQuery = true)
    public Integer deleteTestCaseRobot(int idTestCase);

    @Transactional
    @Modifying(clearAutomatically=true)
    @Nullable
    @Query(value = "delete from round_test_cases_player r where r.test_cases_player_id = ?1", nativeQuery = true)
    void deleteTestCasePlayerRef(int idTestCase);

    @Transactional
    @Modifying(clearAutomatically=true)
    @Nullable
    @Query(value = "delete from round_test_cases_robot r where r.test_cases_robot_id = ?1", nativeQuery = true)
    void deleteTestCaseRobotRef(int idTestCase);
}
