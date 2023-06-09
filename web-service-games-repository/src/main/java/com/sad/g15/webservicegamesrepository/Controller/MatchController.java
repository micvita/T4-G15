package com.sad.g15.webservicegamesrepository.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.sad.g15.webservicegamesrepository.DataAccess.Entity.MatchHistory;
import com.sad.g15.webservicegamesrepository.DataAccess.Entity.Result;
import com.sad.g15.webservicegamesrepository.DataAccess.Entity.Round;
import com.sad.g15.webservicegamesrepository.DataAccess.Entity.TestCasePlayer;
import com.sad.g15.webservicegamesrepository.DataAccess.Entity.TestCaseRobot;
import com.sad.g15.webservicegamesrepository.Service.ServiceFacade;

@RestController

public class MatchController {

	@Autowired
	private ServiceFacade facade;

	/**
	 * -----------------------------------------addMatch----------------------------------------------------------------
	 * Il parametro deve essere passato come un JSON body:
	 * 
	 * { "idStudents": [value1, value2,...,valueN], "scenario": "exampleScenario" }
	 *
	 * @param requestBody
	 * @return "Match added successfully"
	 * -----------------------------------------------------------------------------------------------------------------
	 */
	@PostMapping(value = "/addMatch", consumes = "application/json")
	public ResponseEntity<String> addMatch(@RequestBody JsonNode requestBody) {

		ArrayList<Integer> idStudents = new ArrayList<>();

		for (JsonNode element : requestBody.get("idStudents")) {
			idStudents.add(element.asInt());
		}

		String scenario = requestBody.get("scenario").asText();

		facade.createMatch(idStudents, scenario);

		return ResponseEntity.status(HttpStatus.OK).body("Match added successfully");
	}

	/**
	 * -----------------------------------------addRound----------------------------------------------------------------
	 * Il parametro deve essere passato come un JSON Object:
	 *
	 * { "id_robot" : "1", ecc... }
	 *
	 * Bisogna specificare ID del match per salvare il round (id di round sarà
	 * salvato in seguito), il robot_id deve essere passato all'interno del JSON Object, il resto dei parametri è
	 * opzionale (come visibile sopra).
	 * 
	 * @param idMatch,round
	 * @return MatchHistory / Object
	 * -----------------------------------------------------------------------------------------------------------------
	 */
	@PutMapping("/updateMatch/{idMatch}/addRound")
	public ResponseEntity<String> addRound(@PathVariable int idMatch, @RequestBody Round round) {
		MatchHistory matchAddedRound = facade.createRound(idMatch, round);

		if(matchAddedRound!=null) return ResponseEntity.status(HttpStatus.OK).body("Round added to the specified Match");
		else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request - Could not add Round");
	}
	
	/**
	 * -----------------------------------------updateRound-------------------------------------------------------------
	 * Il parametro deve essere passato come un JSON Object:
	 *
	 * { "idRound" : 16, "idRobot" : 121 }
	 *
	 * Bisogna specificare ID del round, il nuovo risultato e il nuovo ID del Robot, l'ID
	 * del match viene invece indicato nell'URI
	 *  
	 * @param idMatch,requestBody
	 * @return "Round updated successfully"
	 * -----------------------------------------------------------------------------------------------------------------
	 */
	@PutMapping("/updateMatch/{idMatch}/updateRound")
	public ResponseEntity<String> updateRound(@PathVariable int idMatch, @RequestBody JsonNode requestBody) {
	
		int idRound = requestBody.get("idRound").asInt();
		int idRobot = requestBody.get("idRobot").asInt();
		
		facade.updateRound(idMatch, idRound, idRobot);
		return ResponseEntity.status(HttpStatus.OK).body("Round updated successfully");
	}

	/**
	 * -----------------------------------------updateMatch-------------------------------------------------------------
	 * Il parametro deve essere passato come un JSON Object:
	 *
	 * {
	 *     "id": 1,
	 *     "scenario": "scenario",
	 *     "endDate": "2023-06-02T21:00:00",
	 *     "results": [
	 *         {
	 *             "id": 1,
	 *             "result": "sconfitta"
	 *         }
	 *     ]
	 * }
	 *
	 * Bisogna specificare ID del match nell'URI, nel JSON i parametri che si vogliono modificare come
	 * scenario, endDate e results
	 *
	 * @param idMatch, match
	 * @return "Match updated successfully"
	 * -----------------------------------------------------------------------------------------------------------------
	 */
	@PutMapping(value = "/updateMatch/{idMatch}", consumes = "application/json")
	public ResponseEntity<String> updateMatch(@PathVariable int idMatch, @RequestBody MatchHistory match) {

		MatchHistory updated_match = facade.updateMatch(idMatch, match);

		if(updated_match!=null) return ResponseEntity.status(HttpStatus.OK).body("Match updated successfully");
		else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request - Could not update Match");
	}

	/**
	 * -----------------------------------------addTestCasePlayer-------------------------------------------------------
	 * Il parametro deve essere passato come un JSON Object:
	 *
	 * { "totalResult" : 12568, "compilingResult" : 1212, ecc...} <---- per gli altri campi vedere TestCasePlayer entity
	 *
	 * Bisogna specificare ID del match, quello di Round e il player che ha creato
	 * il Test. Il resto dei dati riguarda i punteggi di coverage e viene passato come JSON Object.
	 * Gli altri parametri come PathVariables.
	 * 
	 * @param idMatch,idRound,idPlayer,testCasePlayer
	 * @return MatchHistory / Object
	 * -----------------------------------------------------------------------------------------------------------------
	 */
	@PutMapping("/updateMatch/{idMatch}/updateRound/{idRound}/addTestCasePlayer/{idPlayer}")
	public ResponseEntity<String> addTestcasePlayer(@PathVariable int idMatch, @PathVariable int idRound,
										  @PathVariable int idPlayer, @RequestBody TestCasePlayer testCasePlayer) {

		Round updatedRound = facade.createTestCasePlayer(idMatch, idRound, idPlayer, testCasePlayer);

		if(updatedRound!=null) return ResponseEntity.status(HttpStatus.OK).body("TestCasePlayer added to the " +
				"specified round");
		else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request - Could not add TestCasePlayer");
	}

	/**
	 * -----------------------------------------addTestCaseRobot--------------------------------------------------------
	 * Il parametro deve essere passato come un JSON Object:
	 *
	 * { "totalResult" : 12568, "compilingResult" : 1212, ecc...}
	 *
	 * Bisogna specificare ID del match e quello del Round a cui aggiungere il test case.
	 * Il resto dei dati riguarda i punteggi di coverage e viene passato come JSON Object.
	 * Gli ID come PathVariables.
	 *
	 * @param idMatch,idRound,testCaseRobot
	 * @return MatchHistory / Object
	 * -----------------------------------------------------------------------------------------------------------------
	 */
	@PutMapping("/updateMatch/{idMatch}/updateRound/{idRound}/addTestCaseRobot")
	public ResponseEntity<String> addTestcaseRobot(@PathVariable int idMatch, @PathVariable int idRound,
										 @RequestBody TestCaseRobot testCaseRobot) {

		Round updatedRound = facade.createTestCaseRobot(idMatch, idRound, testCaseRobot);

		if(updatedRound!=null) return ResponseEntity.status(HttpStatus.OK).body("TestCaseRobot added to the " +
				"specified round");
		else return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request - Could not add TestCaseRobot");
	}

	/**
	 * -----------------------------------------getMatchbyId------------------------------------------------------------
	 * Metodo get Riceve sul path indicato un id e restituisce il match con l'id indicato.
	 * 
	 * @param idMatch
	 * @return single Match.
	 * -----------------------------------------------------------------------------------------------------------------
	 */
	@GetMapping("/getSingleMatch/{idMatch}")
	public MatchHistory getMatchS(@PathVariable int idMatch) {
		return facade.readSMatch(idMatch);
	}


	/**
	 * -----------------------------------------getResultByIdPlayer-----------------------------------------------------
	 * Il seguente metodo mostra, dato l'id di un determinato giocatore, il risultato di tutte le partite
	 * che ha giocato.
	 *
	 * @param idPlayer
	 * @return List<Result>
	 * -----------------------------------------------------------------------------------------------------------------
	 */
	@GetMapping("/getResultPlayer/{idPlayer}")
	public List<Result> getResultByIdPlayer(@PathVariable int idPlayer){
		return facade.readResultIdPlayer(idPlayer);
	}
	
	/**
	 * -----------------------------------------deleteRound-----------------------------------------------------
	 * Il seguente metodo elimina un round dato il suo id.
	 * @param idRound
	 * @return ResponseEntity
	 * -----------------------------------------------------------------------------------------------------------------
	 */
	@DeleteMapping("/deleteRound/{idRound}")
	public ResponseEntity<String> deleteRound(@PathVariable int idRound) {
		boolean deleted = facade.deleteRoundById(idRound);
		if (deleted) return ResponseEntity.status(HttpStatus.OK).body("Round deleted successfully");
		else return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
	}
	
}