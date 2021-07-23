package com.ndrmf.engine.service;


import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Date;

import com.ndrmf.common.rFile;

import com.ndrmf.engine.dto.*;

import com.ndrmf.engine.model.GrantDisbursmentWithdrawalFiles;
import com.ndrmf.engine.model.GrantDisbursment;
import com.ndrmf.engine.model.GrantDisbursmentAdvanceReviews;
import com.ndrmf.engine.model.ProjectProposal;
import com.ndrmf.engine.model.QuarterAdvance;
import com.ndrmf.engine.model.GrantDisbursmentAdvanceLiquidation;
import com.ndrmf.engine.model.InitialAdvance;
import com.ndrmf.engine.model.GrantDisbursmentAdvanceLiquidationSoes;

import com.ndrmf.engine.repository.*;

import com.ndrmf.exception.ValidationException;
import com.ndrmf.setting.repository.ProcessTypeRepository;
import com.ndrmf.user.dto.GrantDisbursmentUserLookUpItem;
import com.ndrmf.user.dto.UserLookupItem;
import com.ndrmf.util.enums.ProcessType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ndrmf.common.AuthPrincipal;
import com.ndrmf.user.repository.UserRepository;
import com.ndrmf.util.constants.SystemRoles;
import com.ndrmf.util.enums.ProcessStatus;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
//import org.postgresql.:


@Service
public class GrantDisbursmentService {
	@Autowired
	private ProjectProposalRepository projProposalRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private GrantDisbursmentRepository grantDisbursmentRepo;
	@Autowired
	private InitialAdvanceRepository initialAdvanceRepository;
	@Autowired
	private QuarterAdvanceRepository quarterAdvanceRepository;
	@Autowired
	private ProcessTypeRepository processTypeRepo;
	@Autowired
	private GrantDisbursmentAdvanceReviewsRepository grantDisbursmentAdvanceReviewsRepository;
	@Autowired
	private GrantDisbursmentAdvanceLiquidationRepository grantDisbursmentAdvanceLiquidationRepository;
	@Autowired
	private GrantDisbursmentAdvanceLiquidationSoesRepository gdalsoesrepo;
	@Autowired
	private FileStoreService fileStoreService;
	@Autowired
	private GrantDisbursmentWithdrawalFilesRepository gdwfrepo;

	public List<ProjectProposalListItem> getProjectProposalsInGrantDisbursmentWithInitAdvanceStatus(
			AuthPrincipal currentUser, 
			ProcessStatus status) throws IOException{
		
		if (currentUser.getRoles() != null && (currentUser.getRoles().contains(SystemRoles.PROCESS_OWNER))){
			
		}
		List<GrantDisbursment> grants = grantDisbursmentRepo.findAllRequestsByPoAndOwner(currentUser.getUserId());
		List<ProjectProposal> props = projProposalRepo.findAllRequestsByStatus(ProcessStatus.GRANT_DISBURSMENT.getPersistenceValue());
		List<ProjectProposalListItem> ret = new ArrayList<ProjectProposalListItem>();
		for (int i = 0; i < props.size(); i++) {
			ProjectProposal q = props.get(i);
			ProjectProposalListItem ppli = new ProjectProposalListItem(q.getId(), q.getName(), q.getThematicArea().getName(),
					q.getInitiatedBy().getFullName(), q.getCreatedDate(), q.getStatus());
			if (props.get(i).getJvUser() != null)
				ppli.setJvId(props.get(i).getJvUser().getId());
			else
				ppli.setJvId(null);
			ret.add(ppli);
		}

		return ret;

	}

	@Transactional
	public void commenceGrantDisbursment(
			AuthPrincipal principle,
			UUID proposalId,
			GrantDisbursmentAdvanceRequest body){
		com.ndrmf.setting.model.ProcessType disbursmentProcessType = processTypeRepo
				.findById(ProcessType.DISBURSEMENT.name())
				.orElseThrow(() -> new ValidationException("Disbursment Process is not defined."));
		ProjectProposal p = projProposalRepo.findById(proposalId)
				.orElseThrow(() -> new ValidationException("Invalid Proposal ID"));
		if (p.getGrantDisbursment() == null){
			InitialAdvance ia = new InitialAdvance();
//			ia.setAmount(body.getAmount());
//			ia.setData(body.getData());
			ia.setStatus(ProcessStatus.PENDING);
			ia.setInitAdvanceStatus(ProcessStatus.PENDING);
			ia.setQuarter(1);
//			final UUID userId = disbursmentProcessType.getOwner().getId();
			GrantDisbursment gb = new GrantDisbursment();
			gb.setInitAdvance(ia);
			gb.setOwner(userRepo.getOne(disbursmentProcessType.getOwner().getId()));
			p.setGrantDisbursment(gb);
		} else {
			QuarterAdvance qa = new QuarterAdvance();
			qa.setQuarter(p.getGrantDisbursment().getQuarterAdvances().size() + 2);
			p.getGrantDisbursment().addQuarterAdvance(qa);
//			quarterAdvanceRepository.save(qa);
		}

	}

	@Transactional
	public List<GrantDisbursmentListItem> getAllDisbursments(AuthPrincipal currentUser){
		List<GrantDisbursment> gds = grantDisbursmentRepo.findAllRequestsByPoAndOwner(currentUser.getUserId());
		List<GrantDisbursmentListItem> dto = new ArrayList<GrantDisbursmentListItem>();

		gds.forEach(c -> {
			GrantDisbursmentListItem gdli = new GrantDisbursmentListItem();
			gdli.setId(c.getId());
			gdli.setCreate_date(c.getCreatedDate());
			gdli.setCreated_by(c.getCreatedBy());
			gdli.setProposalName(c.getProposalRef().getName());
			dto.add(gdli);
		});

		return dto;
	}

	@Transactional
	public GrantDisbursmentItem getSingleDisbursment(
			AuthPrincipal principal,
			UUID disbursmentId
	){
		GrantDisbursmentItem dto = new GrantDisbursmentItem();
		GrantDisbursment gdi = grantDisbursmentRepo.findById(disbursmentId)
				.orElseThrow(() -> new ValidationException("Disbursment Id is not valid."));
		com.ndrmf.setting.model.ProcessType disbursmentProcessType = processTypeRepo
				.findById(ProcessType.DISBURSEMENT.name())
				.orElseThrow(() -> new ValidationException("Disbursment Process is not defined."));

		InitialAdvanceItem iai = new InitialAdvanceItem();
		iai.setAmount(gdi.getInitAdvance().getAmount());
		iai.setData(gdi.getInitAdvance().getData());
		iai.setId(gdi.getInitAdvance().getId());
		iai.setStatus(gdi.getInitAdvance().getStatus().getPersistenceValue());
		iai.setQuarter(gdi.getInitAdvance().getQuarter());
//		if (gdi.getInitAdvance().getSubStatus() != null)
//			iai.setSubStatus(gdi.getInitAdvance().getSubStatus().getPersistenceValue());

		iai.setPayeesName(gdi.getInitAdvance().getPayeesName());
		iai.setPayeesAccount(gdi.getInitAdvance().getPayeesAccount());
		iai.setPayeesAddress(gdi.getInitAdvance().getPayeesAddress());
		iai.setBankAddress(gdi.getInitAdvance().getBankAddress());
		iai.setBankName(gdi.getInitAdvance().getBankName());
		iai.setSwiftCode(gdi.getInitAdvance().getSwiftCode());
		iai.setSpecialPaymentInstruction(gdi.getInitAdvance().getSpecialPaymentInstruction());

		if (gdi.getInitAdvance().getInitAdvanceStatus() != null)
			iai.setSubStatus(gdi.getInitAdvance().getInitAdvanceStatus().getPersistenceValue());

		List<GrantDisbursmentAdvanceReviews> gdar = grantDisbursmentAdvanceReviewsRepository.
				findAllRequestsByInitialAdvance(gdi.getInitAdvance().getId());
		gdar.forEach(r -> {
			InitialAdvanceItem.InitialAdvanceReviewsItem review = new InitialAdvanceItem.InitialAdvanceReviewsItem();
			review.setAssigned(r.getAssignee().getId().equals(principal.getUserId()));
			review.setAssignee(new UserLookupItem(r.getAssignee().getId(), r.getAssignee().getFullName()));
			review.setComments(r.getComments());
			review.setId(r.getId());
			review.setStatus(r.getStatus());
			review.setSubStatus(r.getSubStatus());
			iai.addInitialAdvanceReviewItem(review);
		});

		if (gdi.getInitAdvance().getGrantDisbursmentAdvanceLiquidationRef() != null){
			GrantDisbursmentAdvanceLiquidation gdal;
			gdal = grantDisbursmentAdvanceLiquidationRepository.findRequestsByInitialAdvanceId(
					gdi.getInitAdvance().getId()
			).orElseThrow(() -> new ValidationException("Initial Advance ID is not defined."));
			InitialAdvanceItem.InitialAdvanceLiquidationItem iali;
			iali = new InitialAdvanceItem.InitialAdvanceLiquidationItem();
			iali.setId(gdal.getId());
			iali.setData(gdal.getData());
			iali.setAmount(gdal.getAmount());
			iali.setStatus(gdal.getStatus());
			iali.setSubStatus(gdal.getSubStatus());

			if (gdal.getLiquidationSoes() != null){
				 gdal.getLiquidationSoes().stream()
						 .filter(item -> item.getSoeType().equals("ndrmf") && item.isEnabled())
						 .forEach(c -> {
							 GrantDisbursmentAdvanceLiquidationSoesItem gdalsoei;
							 gdalsoei = new GrantDisbursmentAdvanceLiquidationSoesItem();

							 gdalsoei.setActivityId(c.getActivityId());
							 gdalsoei.setChequeNumber(c.getChequeNumber());
							 gdalsoei.setRemarks(c.getRemarks());
							 gdalsoei.setInvoiceAmount(c.getInvoiceAmount());
							 gdalsoei.setPaidAmount(c.getPaidAmount());
							 gdalsoei.setDateOfPayment(c.getDateOfPayment());
							 gdalsoei.setVendorName(c.getVendorName());
							 gdalsoei.setSoeType(c.getSoeType());
							 gdalsoei.setId(c.getId());

							 iali.addNdrmfSoe(gdalsoei);
						 });
				gdal.getLiquidationSoes().stream()
						.filter(item -> item.getSoeType().equals("fip") && item.isEnabled())
						.forEach(c -> {
							GrantDisbursmentAdvanceLiquidationSoesItem gdalsoei;
							gdalsoei = new GrantDisbursmentAdvanceLiquidationSoesItem();

							gdalsoei.setActivityId(c.getActivityId());
							gdalsoei.setChequeNumber(c.getChequeNumber());
							gdalsoei.setRemarks(c.getRemarks());
							gdalsoei.setInvoiceAmount(c.getInvoiceAmount());
							gdalsoei.setPaidAmount(c.getPaidAmount());
							gdalsoei.setDateOfPayment(c.getDateOfPayment());
							gdalsoei.setVendorName(c.getVendorName());
							gdalsoei.setSoeType(c.getSoeType());
							gdalsoei.setId(c.getId());

							iali.addFipSoe(gdalsoei);
						});
			}
			iali.setPayeesName(gdal.getPayeesName());
			iali.setPayeesAccount(gdal.getPayeesAccount());
			iali.setPayeesAddress(gdal.getPayeesAddress());
			iali.setBankAddress(gdal.getBankAddress());
			iali.setBankName(gdal.getBankName());
			iali.setSwiftCode(gdal.getSwiftCode());
			iali.setSpecialPaymentInstruction(gdal.getSpecialPaymentInstruction());
			iali.setReassignmentComments(gdal.getReassignmentComments());
			iali.setReassignedOn(gdal.getReassignedOn());

			iai.setAdvanceLiquidationItem(iali);
		}

		iai.setReassignmentComments(gdi.getInitAdvance().getReassignComments());
		iai.setReassignedOn(gdi.getInitAdvance().getReassignedOn());

		dto.setInitialAdvance(iai);

		List<QuarterAdvance> qal = gdi.getQuarterAdvances();
		List<QuarterAdvanceItem> qali = new ArrayList<>();

		if (qal.size() > 0){

			qal.forEach(c -> {
				QuarterAdvanceItem qai = new QuarterAdvanceItem();
				qai.setData(c.getData());
				qai.setStatus(c.getStatus());
				qai.setSubStatus(c.getSubStatus());
				qai.setId(c.getId());
				qai.setAmount(c.getAmount());
				qai.setPayeesName(c.getPayeesName());
				qai.setPayeesAccount(c.getPayeesAccount());
				qai.setPayeesAddress(c.getPayeesAddress());
				qai.setBankAddress(c.getBankAddress());
				qai.setBankName(c.getBankName());
				qai.setSwiftCode(c.getSwiftCode());
				qai.setSpecialPaymentInstruction(c.getSpecialPaymentInstruction());
				qai.setQuarter(c.getQuarter());

				qai.setReassignmentComments(c.getReassignmentComments());
				qai.setReassignedOn(c.getReassignedOn());

				List<GrantDisbursmentAdvanceReviews> gdarqa;
				gdarqa = grantDisbursmentAdvanceReviewsRepository.
						findAllRequestsByQuarterAdvance(c.getId());
				gdarqa.forEach(r -> {
					QuarterAdvanceItem.QuarterAdvanceReviewsListItem review;
					review = new QuarterAdvanceItem.QuarterAdvanceReviewsListItem();
					review.setAssigned(r.getAssignee().getId().equals(principal.getUserId()));
					review.setAssignee(new UserLookupItem(r.getAssignee().getId(), r.getAssignee().getFullName()));
					review.setComments(r.getComments());
					review.setId(r.getId());
					review.setStatus(r.getStatus());
					review.setSubStatus(r.getSubStatus());
					qai.addQuarterAdvanceReviewsList(review);
				});

				if (c.getGrantDisbursmentAdvanceLiquidationRef() != null){
					GrantDisbursmentAdvanceLiquidation qagdl;
					qagdl = grantDisbursmentAdvanceLiquidationRepository.findRequestsByQuarterAdvanceId(
							c.getId()
					).orElseThrow(() -> new ValidationException("Liquidation ID is not defined."));
					QuarterAdvanceItem.AdvanceLiquidationItem qagdli;
					qagdli = new QuarterAdvanceItem.AdvanceLiquidationItem();
					qagdli.setId(qagdl.getId());
					qagdli.setData(qagdl.getData());
					qagdli.setAmount(qagdl.getAmount());
					qagdli.setStatus(qagdl.getStatus());
					qagdli.setSubStatus(qagdl.getSubStatus());
					qagdli.setReassignmentComments(qagdl.getReassignmentComments());
					qagdli.setReassignedOn(qagdl.getReassignedOn());

//					System.out.println(qagdl.getLiquidationSoes().size());

					if (qagdl.getLiquidationSoes() != null){
						qagdl.getLiquidationSoes().stream()
								.filter(item -> item.getSoeType().equals("ndrmf") && item.isEnabled())
								.forEach(d -> {
									GrantDisbursmentAdvanceLiquidationSoesItem gdalsoei;
									gdalsoei = new GrantDisbursmentAdvanceLiquidationSoesItem();

									gdalsoei.setActivityId(d.getActivityId());
									gdalsoei.setChequeNumber(d.getChequeNumber());
									gdalsoei.setRemarks(d.getRemarks());
									gdalsoei.setInvoiceAmount(d.getInvoiceAmount());
									gdalsoei.setPaidAmount(d.getPaidAmount());
									gdalsoei.setDateOfPayment(d.getDateOfPayment());
									gdalsoei.setVendorName(d.getVendorName());
									gdalsoei.setSoeType(d.getSoeType());
									gdalsoei.setId(d.getId());

									qagdli.addNdrmfSoe(gdalsoei);
								});
						qagdl.getLiquidationSoes().stream()
								.filter(item -> item.getSoeType().equals("fip") && item.isEnabled())
								.forEach(e -> {
									GrantDisbursmentAdvanceLiquidationSoesItem gdalsoei;
									gdalsoei = new GrantDisbursmentAdvanceLiquidationSoesItem();

									gdalsoei.setActivityId(e.getActivityId());
									gdalsoei.setChequeNumber(e.getChequeNumber());
									gdalsoei.setRemarks(e.getRemarks());
									gdalsoei.setInvoiceAmount(e.getInvoiceAmount());
									gdalsoei.setPaidAmount(e.getPaidAmount());
									gdalsoei.setDateOfPayment(e.getDateOfPayment());
									gdalsoei.setVendorName(e.getVendorName());
									gdalsoei.setSoeType(e.getSoeType());
									gdalsoei.setId(e.getId());

									qagdli.addFipSoe(gdalsoei);
								});
					}
					qagdli.setPayeesName(qagdl.getPayeesName());
					qagdli.setPayeesAccount(qagdl.getPayeesAccount());
					qagdli.setPayeesAddress(qagdl.getPayeesAddress());
					qagdli.setBankAddress(qagdl.getBankAddress());
					qagdli.setBankName(qagdl.getBankName());
					qagdli.setSwiftCode(qagdl.getSwiftCode());
					qagdli.setSpecialPaymentInstruction(qagdl.getSpecialPaymentInstruction());

					qai.setAdvanceLiquidationItem(qagdli);
				}

				qali.add(qai);
			});
		}

		dto.setId(gdi.getId());
		dto.setQuarterAdvanceList(qali);
		dto.setProposalName(gdi.getProposalRef().getName());
		dto.setImplementationPlan(gdi.getProposalRef().getPip().getImplementationPlan());
		dto.setOwner(new UserLookupItem(gdi.getOwner().getId(), gdi.getOwner().getFullName()));
		dto.setAssigned(principal.getUserId().equals(gdi.getOwner().getId()));
		dto.setStatus(gdi.getStatus());
		dto.setSubStatus(gdi.getSubStatus());

		if (gdi.getInitAdvanceRequestStatus() != null){
			dto.setInitAdvanceStatus(gdi.getInitAdvanceRequestStatus().getPersistenceValue());
		}

		if (gdi.getNextQuarterRequestStatus() != null)
			dto.setQuarterAdvanceStatus(gdi.getNextQuarterRequestStatus().getPersistenceValue());

//		GrantDisbursmentUserLookUpItem gduser = new GrantDisbursmentUserLookUpItem();

		String jvUsername = null;

		if (gdi.getProposalRef().getJvUser() != null)
			jvUsername = gdi.getProposalRef().getJvUser().getFullName();

		dto.setInitiatedBy(
			new GrantDisbursmentUserLookUpItem(
				gdi.getProposalRef().getInitiatedBy().getId(),
				gdi.getProposalRef().getInitiatedBy().getFullName(),
				jvUsername,
				gdi.getProposalRef().getType()
			)
		);

		return dto;
	}

	@Transactional
	public void submitInitalAdvance(
			AuthPrincipal user,
			UUID disbursmentId,
			InitialAdvanceSubmitRequest body){

		com.ndrmf.setting.model.ProcessType disbursmentProcessType = processTypeRepo
				.findById(ProcessType.DISBURSEMENT.name())
				.orElseThrow(() -> new ValidationException("Disbursment Process is not defined."));

		if (disbursmentProcessType.getOwner() == null) {
			throw new ValidationException(
					"Process Owner is not defined for this process. To continue, admin should define Process Owner for this process first.");
		}

		GrantDisbursment gd = grantDisbursmentRepo.findById(disbursmentId)
				.orElseThrow(() -> new ValidationException("Disbursement Id is not valid."));

		gd.setOwner(disbursmentProcessType.getOwner());
		gd.setInitAdvanceRequestStatus(ProcessStatus.COMPLETED);
		gd.setStatus(ProcessStatus.MARKED_TO_PO.getPersistenceValue());
		gd.setSubStatus(ProcessStatus.PENDING.getPersistenceValue());

		InitialAdvance ia = gd.getInitAdvance();
		ia.setData(body.getData());
		ia.setStatus(ProcessStatus.COMPLETED);
		ia.setAmount(body.getAmount());

		ia.setPayeesName(body.getPayeesName());
		ia.setPayeesAddress(body.getPayeesAddress());
		ia.setBankName(body.getBankName());
		ia.setBankAddress(body.getBankAddress());
		ia.setPayeesAccount(body.getPayeesAccount());
		ia.setSwiftCode(body.getSwiftCode());
		ia.setSpecialPaymentInstruction(body.getSpecialPaymentInstruction());

		gd.setInitAdvance(ia);
		grantDisbursmentRepo.save(gd);

	}

	@Transactional
	public void assignInitialAdvanceReviews(
			AuthPrincipal user,
			UUID disbursmentId,
			InitialAdvanceAssignReviewsRequest body
	){
		GrantDisbursment gd = grantDisbursmentRepo.findById(disbursmentId)
				.orElseThrow(() -> new ValidationException("Disbursment Id is not valid."));

		InitialAdvance ia = gd.getInitAdvance();

		if (body.getReviewers() != null) {
			body.getReviewers().forEach(r -> {
				GrantDisbursmentAdvanceReviews gdar = new GrantDisbursmentAdvanceReviews();
				gdar.setStatus(ProcessStatus.PENDING.getPersistenceValue());
				gdar.setAssignee(userRepo.getOne(r));
				ia.addReviewList(gdar);
			});
		}

//		gd.get().setInitAdvance(ia);
		gd.setStatus(ProcessStatus.REVIEW_PENDING.getPersistenceValue());
//		grantDisbursmentRepo.save(gd);

	}

	@Transactional
	public void submitInitialAdvanceReview(
			AuthPrincipal user,
			UUID disbursmentId,
			InitialAdvanceSubmitReviewRequest body
	){
		GrantDisbursment gd = grantDisbursmentRepo.findById(disbursmentId)
				.orElseThrow(() -> new ValidationException("Disbursment Id is not valid."));

		GrantDisbursmentAdvanceReviews review;
		review = grantDisbursmentAdvanceReviewsRepository.findRequestsById(body.getId())
				.orElseThrow(() -> new ValidationException("Review id is not valid"));

		review.setComments(body.getComments());
		review.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
		review.setSubStatus(body.getSubStatus());

		List<GrantDisbursmentAdvanceReviews> gdiar = gd.getInitAdvance().getReviewsList();
		if (gdiar.stream()
				.allMatch(r -> r.getStatus().equals(ProcessStatus.COMPLETED.getPersistenceValue()))) {
			gd.setStatus(ProcessStatus.REVIEW_COMPLETED.getPersistenceValue());
		}

		List<GrantDisbursmentAdvanceReviews> gdqar;
		gdqar = grantDisbursmentAdvanceReviewsRepository.findAllRequestsByQuarterAdvance(body.getQaId());

		System.out.println("SIze of qa Review List:--" + gdqar.size());

		if (gdqar.stream()
				.allMatch(r -> r.getStatus().equals(ProcessStatus.COMPLETED.getPersistenceValue()))) {
			review.getQuarterAdvanceRef().setStatus(ProcessStatus.REVIEW_COMPLETED.getPersistenceValue());
			gd.setStatus(ProcessStatus.REVIEW_COMPLETED.getPersistenceValue());
		}
	}

	@Transactional
	public void submitQuarterAdvance(
			AuthPrincipal user,
			UUID disbursmentId,
			QuarterAdvanceSubmitRequest body){

		GrantDisbursment gd;
		gd = grantDisbursmentRepo.findById(disbursmentId)
				.orElseThrow(() -> new ValidationException("Disbursement Id is not valid."));
		gd.setNextQuarterRequestStatus(ProcessStatus.PENDING);

		QuarterAdvance qa;
		qa = quarterAdvanceRepository.findById(body.getId())
				.orElseThrow(() -> new ValidationException("Quarter Advance Id not valid."));

		qa.setData(body.getData());
		qa.setAmount(body.getAmount());
		qa.setPayeesName(body.getPayeesName());
		qa.setPayeesAddress(body.getPayeesAddress());
		qa.setBankName(body.getBankName());
		qa.setBankAddress(body.getBankAddress());
		qa.setPayeesAccount(body.getPayeesAccount());
		qa.setSwiftCode(body.getSwiftCode());
		qa.setSpecialPaymentInstruction(body.getSpecialPaymentInstruction());


		qa.setStatus(ProcessStatus.COMPLETED.getPersistenceValue());
		qa.setSubStatus(ProcessStatus.MARKED_TO_PO.getPersistenceValue());

	}

	@Transactional
	public void assignQuarterAdvanceReviews(
			AuthPrincipal user,
			UUID disbursmentId,
			InitialAdvanceAssignReviewsRequest body
	){
		QuarterAdvance qa;
		qa = quarterAdvanceRepository.findById(body.getInitialAdvanceId())
				.orElseThrow(() -> new ValidationException("Quarter Advance Id not valid."));

		if (body.getReviewers() != null) {
			body.getReviewers().forEach(r -> {
				GrantDisbursmentAdvanceReviews gdar = new GrantDisbursmentAdvanceReviews();
				gdar.setStatus(ProcessStatus.PENDING.getPersistenceValue());
				gdar.setAssignee(userRepo.getOne(r));
				qa.addReviewList(gdar);
			});
			qa.setStatus(ProcessStatus.REVIEW_PENDING.getPersistenceValue());
		}


	}

	@Transactional
	public void approveInitialAdvance(
			AuthPrincipal user,
			UUID disbursmentId,
			ProcessStatus status
	){
		GrantDisbursment gd = grantDisbursmentRepo.findById(disbursmentId)
				.orElseThrow(() -> new ValidationException("Disbursment Id is not valid."));

//		System.out.println(status);
//		System.out.println(status.getPersistenceValue());

		InitialAdvance ia = gd.getInitAdvance();
		if (ProcessStatus.APPROVED.getPersistenceValue().equals(status.getPersistenceValue())){
			if (ia.getGrantDisbursmentAdvanceLiquidationRef() == null){
				GrantDisbursmentAdvanceLiquidation gdal = new GrantDisbursmentAdvanceLiquidation();
				gdal.setInitialAdvanceRef(ia);
				gdal.setStatus(ProcessStatus.NOT_INITIATED.getPersistenceValue());
				grantDisbursmentAdvanceLiquidationRepository.save(gdal);
				ia.setGrantDisbursmentAdvanceLiquidationRef(gdal);
			}
			ia.setStatus(status);
			ia.setInitAdvanceStatus(status);
			gd.setInitAdvanceRequestStatus(ProcessStatus.APPROVED);
		}else if (ProcessStatus.REJECTED.getPersistenceValue().equals(status.getPersistenceValue())){
			ia.setStatus(status);
			ia.setInitAdvanceStatus(status);
			gd.setInitAdvanceRequestStatus(ProcessStatus.REJECTED);
		}

	}

	@Transactional
	public void reassignInitialAdvance(
			AuthPrincipal user,
			UUID disbursmentId,
			Comment body
	){
		GrantDisbursment gd = grantDisbursmentRepo.findById(disbursmentId)
				.orElseThrow(() -> new ValidationException("Disbursment Id is not valid."));
//		System.out.println(status);
//		System.out.println(status.getPersistenceValue());
		InitialAdvance ia = gd.getInitAdvance();
		ia.setReassignComments(body.getComment());
		ia.setReassignedOn(new Date());
		ia.setStatus(ProcessStatus.REASSIGNED);
		ia.setInitAdvanceStatus(ProcessStatus.PENDING);
//		ia.setS
	}

	@Transactional
	public void approveQuarterAdvance(
			AuthPrincipal user,
			UUID advanceId,
			ProcessStatus status
	){
		QuarterAdvance qa;
		qa = quarterAdvanceRepository.findById(advanceId)
				.orElseThrow(() -> new ValidationException("Quarter Advance Id not valid."));

		if (ProcessStatus.APPROVED.getPersistenceValue().equals(status.getPersistenceValue())){
			if (qa.getGrantDisbursmentAdvanceLiquidationRef() == null){
				GrantDisbursmentAdvanceLiquidation gdal = new GrantDisbursmentAdvanceLiquidation();
				gdal.setQuarterAdvanceRef(qa);
				gdal.setStatus(ProcessStatus.NOT_INITIATED.getPersistenceValue());
				grantDisbursmentAdvanceLiquidationRepository.save(gdal);
				qa.setGrantDisbursmentAdvanceLiquidationRef(gdal);
			}
			qa.setStatus(status.getPersistenceValue());
			qa.setSubStatus(status.getPersistenceValue());
		}else if (ProcessStatus.REJECTED.getPersistenceValue().equals(status.getPersistenceValue())){
			qa.setStatus(status.getPersistenceValue());
			qa.setSubStatus(status.getPersistenceValue());
		}
	}

	@Transactional
	public void submitInitialAdvanceLiquidationWithSoes(
			AuthPrincipal user,
			UUID liquidationId,
			GrantDisbursmentAdvanceLiquidationSubmitRequest body){
		GrantDisbursmentAdvanceLiquidation gdal;
		gdal = grantDisbursmentAdvanceLiquidationRepository.findById(liquidationId)
				.orElseThrow(() -> new ValidationException("Liquidation Id is not valid."));

		gdal.setPayeesName(body.getPayeesName());
		gdal.setPayeesAddress(body.getPayeesAddress());
		gdal.setBankName(body.getBankName());
		gdal.setBankAddress(body.getBankAddress());
		gdal.setPayeesAccount(body.getPayeesAccount());
		gdal.setSwiftCode(body.getSwiftCode());
		gdal.setSpecialPaymentInstruction(body.getSpecialPaymentInstruction());
		gdal.setStatus(ProcessStatus.UNDER_REVIEW.getPersistenceValue());


		gdal.getLiquidationSoes().forEach(c -> {
			c.setEnabled(false);
		});

		body.getFipSoes().forEach(item -> {
			GrantDisbursmentAdvanceLiquidationSoes gdalsoe = new GrantDisbursmentAdvanceLiquidationSoes();
			gdalsoe.setActivityId(item.getActivityId());
			gdalsoe.setVendorName(item.getVendorName());
			gdalsoe.setInvoiceAmount(item.getInvoiceAmount());
			gdalsoe.setDateOfPayment(item.getDateOfPayment());
			gdalsoe.setPaidAmount(item.getPaidAmount());
			gdalsoe.setChequeNumber(item.getChequeNumber());
			gdalsoe.setRemarks(item.getRemarks());
			gdalsoe.setSoeType(item.getSoeType());
			gdalsoe.setEnabled(true);

			gdal.addLiquidationSoe(gdalsoe);
		});

		body.getNdrmfSoes().forEach(item -> {
			GrantDisbursmentAdvanceLiquidationSoes gdalsoe = new GrantDisbursmentAdvanceLiquidationSoes();
			gdalsoe.setActivityId(item.getActivityId());
			gdalsoe.setVendorName(item.getVendorName());
			gdalsoe.setInvoiceAmount(item.getInvoiceAmount());
			gdalsoe.setDateOfPayment(item.getDateOfPayment());
			gdalsoe.setPaidAmount(item.getPaidAmount());
			gdalsoe.setChequeNumber(item.getChequeNumber());
			gdalsoe.setRemarks(item.getRemarks());
			gdalsoe.setSoeType(item.getSoeType());
			gdalsoe.setEnabled(true);

			gdal.addLiquidationSoe(gdalsoe);
		});

	}

	@Transactional
	public void approveAdvanceLiquidation(UUID liquidationId, String Status){
		GrantDisbursmentAdvanceLiquidation gdal;
		gdal = grantDisbursmentAdvanceLiquidationRepository.findById(liquidationId)
				.orElseThrow(() -> new ValidationException("Liquidation Id is not valid."));

		gdal.setSubStatus(ProcessStatus.APPROVED.getPersistenceValue());
		gdal.setStatus(ProcessStatus.APPROVED.getPersistenceValue());
	}

	@Transactional
	public void reassignAdvanceLiquidation(UUID liquidationId, String Status, Comment body){
		GrantDisbursmentAdvanceLiquidation gdal;
		gdal = grantDisbursmentAdvanceLiquidationRepository.findById(liquidationId)
				.orElseThrow(() -> new ValidationException("Liquidation Id is not valid."));
		gdal.setReassignmentComments(body.getComment());
		gdal.setReassignedOn(new Date());
		gdal.setStatus(ProcessStatus.REASSIGNED.getPersistenceValue());
		gdal.setSubStatus(ProcessStatus.PENDING.getPersistenceValue());
	}

	@Transactional
	public void reassignQuarterAdvance(UUID advanceId, String Status, Comment body){
		QuarterAdvance qa;
		qa = quarterAdvanceRepository.findById(advanceId)
				.orElseThrow(() -> new ValidationException("Advance Id is not valid."));
		qa.setReassignmentComments(body.getComment());
		qa.setReassignedOn(new Date());
		qa.setStatus(ProcessStatus.REASSIGNED.getPersistenceValue());
		qa.setSubStatus(ProcessStatus.PENDING.getPersistenceValue());
	}

	@Transactional
	public String uploadFileForGrantDisbursmentAdvance(
			AuthPrincipal principle,
			UUID advanceId,
			MultipartFile file,
			String type
	) throws IOException {

		QuarterAdvance qa = quarterAdvanceRepository.findById(advanceId)
				.orElseThrow(() -> new ValidationException("AdvanceId is not valid."));

	 	rFile persistedFile;
		persistedFile = fileStoreService.saveFile(file, principle.getUserId());

		GrantDisbursmentWithdrawalFiles gdwf = new GrantDisbursmentWithdrawalFiles();
		gdwf.setFileRef(persistedFile);
		gdwf.setPicByte(file.getBytes());


		if (type.equals("quarter")){
			gdwf.setQaRef(qa);
			qa.addAttachement(gdwf);
		}else{
			gdwf.setIaRef(qa.getGrantDisbursmentRef().getInitAdvance());
			qa.getGrantDisbursmentRef().getInitAdvance().addAttachement(gdwf);
		}

	 	return persistedFile.getPath();
	}

	@Transactional
	public List<GrantDisbursmentWithdrawalFilesListItem> getFilesForAdvance(
			UUID advanceId
	){
		List<GrantDisbursmentWithdrawalFiles> gdwf = new ArrayList<>();
		List<GrantDisbursmentWithdrawalFilesListItem> gdwflil = new ArrayList<>();

		gdwf = gdwfrepo.findRequestsByQuarterAdvanceId(advanceId);

		gdwf.stream().forEach(c -> {
			GrantDisbursmentWithdrawalFilesListItem gdwfli = new GrantDisbursmentWithdrawalFilesListItem();
			gdwfli.setName(c.getFileRef().getFileName());
			gdwfli.setPath(c.getFileRef().getPath());
			gdwfli.setCreated_by(c.getCreatedBy());

			gdwflil.add(gdwfli);
		});

		return gdwflil;
	}

}
