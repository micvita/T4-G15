package com.sad.g15.webservicegamesrepository.Service;

import com.sad.g15.webservicegamesrepository.DataAccess.Entity.MatchHistory;
import com.sad.g15.webservicegamesrepository.DataAccess.Entity.Round;
import com.sad.g15.webservicegamesrepository.DataAccess.Repository.RepositoriesFacade;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RoundService {

    public RoundService(RepositoriesFacade facade, TestCaseService testCaseService) {
        this.facade = facade;
        this.testCaseService = testCaseService;
    }
    private final RepositoriesFacade facade;
    private TestCaseService testCaseService;

    public Optional<Round> readS(int round_id){
        return facade.getRoundRepository().findById(round_id).stream().findFirst();
    }

    public List<Round> readM(MatchHistory match){
        return facade.getRoundRepository().findByMatchId(match.getId());
    }

    public Round create(Round round){
        //Does not work
        /*round.getTestCasesPlayer().forEach(testCasePlayer -> {testCaseService.create(testCasePlayer);});
        round.getTestCasesRobot().forEach(testCaseRobot -> {testCaseService.create(testCaseRobot);});*/

        return facade.getRoundRepository().save(round);
    }

    public void delete(Round round){
        facade.getRoundRepository().delete(round);
    }

    public Round update(Round round){
        facade.getRoundRepository().deleteById(round.getId());
        return facade.getRoundRepository().save(round);
    }

    public Round readById(int id){
        return facade.getRoundRepository().getReferenceById(id);
    }
}
