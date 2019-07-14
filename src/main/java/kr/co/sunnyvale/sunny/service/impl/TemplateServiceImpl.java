package kr.co.sunnyvale.sunny.service.impl;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Tag;
import kr.co.sunnyvale.sunny.domain.Template;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;
import kr.co.sunnyvale.sunny.repository.hibernate.TemplateRepository;
import kr.co.sunnyvale.sunny.service.TemplateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service( value="templateService" )
@Transactional
public class TemplateServiceImpl implements TemplateService {
	
	@Autowired
	private TemplateRepository templateRepository;

	@Override
	@Transactional
	public void save(Template template) {
		templateRepository.save(template);
		
	}

	@Override
	public void update(Template template) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Template find(Long templateId) {
		
		return templateRepository.findUniqByObject("id", templateId);
	}

	@Override
	public List<Template> getTemplates(Sunny sunny, Stream stream) {
		
		List<Template> templates = templateRepository.findListByObject("site", sunny.getSite(), stream);
		
		return templates; 
		
	}

	@Override
	@Transactional
	public void delete(Long templateId) {
		Template template = templateRepository.select(templateId);
		templateRepository.delete(template);
		
	}

	
	
}