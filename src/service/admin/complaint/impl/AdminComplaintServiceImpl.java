package service.admin.complaint.impl;

import java.sql.Connection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import common.JDBCTemplate;
import dao.admin.complaint.face.AdminComplaintDao;
import dao.admin.complaint.impl.AdminComplaintDaoImpl;
import dto.AdminComplaint;
import service.admin.complaint.face.AdminComplaintService;
import util.AdminComplaintPaging;

public class AdminComplaintServiceImpl implements AdminComplaintService {

	//ComplaintDao 객체 생성
	private AdminComplaintDao complaintDao = new AdminComplaintDaoImpl();
	
	
	//-------------신고 목록(페이징 적용)--------------
	@Override
	public List<AdminComplaint> getList(AdminComplaintPaging complaintPaging) {
		
		//신고글 전체 조회결과 처리(페이징)
		return complaintDao.selectAll(JDBCTemplate.getConnection(), complaintPaging);
	}
	
	
	//-----------------페이징 처리-----------------
	@Override
	public AdminComplaintPaging getComplaintPaging(HttpServletRequest req) {
		
		//전달파라미터 curPage 파싱
		String param = req.getParameter("curPage");
		int curPage = 0;
		if(param != null && !"".equals(param)) {
			curPage = Integer.parseInt(param);
		}
		
		//Complaint 테이블에 등록된 총 신고글 수 조회
		int totalCount = complaintDao.selectCountAll(JDBCTemplate.getConnection());
		
		//Paging객체 생성
		AdminComplaintPaging complaintPaging = new AdminComplaintPaging(totalCount, curPage);
		
		return complaintPaging;
	}
	
	
	//-------------전달파라미터 - comNo--------------
	@Override
	public AdminComplaint getComNo(HttpServletRequest req) {
		
		//comNo를 저장할 객체 생성
		AdminComplaint comNo = new AdminComplaint();
		
		//comNo 전달파라미터 검증
		String param = req.getParameter("comNo");
		if(param!=null && !"".equals(param)) {
		
			comNo.setComNo(Integer.parseInt(param)); //comNo 전달파라미터 추출
		}
		return comNo; //결과 객체 반환
	}

	
	//-------------신고 상세조회--------------
	@Override
	public AdminComplaint view(AdminComplaint comNo) {
		Connection conn = JDBCTemplate.getConnection();
		
		AdminComplaint complaint = complaintDao.selectComplaintByComNo(conn, comNo);
		
		return complaint;
	}
	
	//-----------챌린지개설자 아이디 조회-------
	public String getChUid(AdminComplaint viewComplaint) {
		return complaintDao.selectChUidByChNo(JDBCTemplate.getConnection(), viewComplaint);
	}
	
	
	//-------------신고 수정--------------
	@Override
	public void update(HttpServletRequest req) {
		
		AdminComplaint complaint = null;
		complaint = new AdminComplaint();
		
		complaint.setComAdminContent(req.getParameter("comAdminContent"));
		complaint.setComManage(req.getParameter("comManage"));
		
		
		System.out.println( complaint );
		
		Connection conn = JDBCTemplate.getConnection();
		
		if(complaint != null) {
			if(complaintDao.update(conn, complaint) > 0) {
				JDBCTemplate.commit(conn);
			
			} else {
				JDBCTemplate.rollback(conn);
			}
		}
	}
	
	
	//-------------신고 삭제--------------
	@Override
	public void delete(AdminComplaint comNo) {
		Connection conn = JDBCTemplate.getConnection();
		
		if(complaintDao.delete(conn, comNo) > 0) {
			JDBCTemplate.commit(conn);
			
		} else {
			JDBCTemplate.rollback(conn);
		}
	}
	

	//--------------신고 등록--------------
	
	public void insert(HttpServletRequest req) {
		
		AdminComplaint complaint = new AdminComplaint();
	
		complaint.setComContent(req.getParameter("comContent"));
		complaint.setComAdminContent(req.getParameter("comAdminContent"));
		complaint.setComManage(req.getParameter("comManage"));

		
		Connection conn = JDBCTemplate.getConnection();
			
	
			if(complaintDao.update(conn, complaint) > 0) {
				JDBCTemplate.commit(conn);
			
			} else {
				JDBCTemplate.rollback(conn);
			}
		
		
	}
	
}
