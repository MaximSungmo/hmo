package kr.co.sunnyvale.sunny.service.impl;

import kr.co.sunnyvale.sunny.domain.Report;
import kr.co.sunnyvale.sunny.repository.hibernate.ReportRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="reportService" )
@Transactional
public class ReportService  {
	
	@Autowired
	private ReportRepository reportRepository;
	
	@Transactional
	public void save(Report report) {
		reportRepository.save(report);
	}
	
}