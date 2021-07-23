package com.ndrmf.engine.repository;


import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.ndrmf.engine.model.OfferLetter;


public interface OfferLetterRepository  extends JpaRepository<OfferLetter, UUID>{
	@Query(value = "SELECT DISTINCT ol FROM OfferLetter ol WHERE ol.proposalRef.id = :proposalId")
	OfferLetter findAllRequestsByProposalId(@Param("proposalId") UUID proposalId);

}