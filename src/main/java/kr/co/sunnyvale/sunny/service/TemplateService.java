package kr.co.sunnyvale.sunny.service;

import java.util.List;

import kr.co.sunnyvale.sunny.domain.Tag;
import kr.co.sunnyvale.sunny.domain.Template;
import kr.co.sunnyvale.sunny.domain.extend.Stream;
import kr.co.sunnyvale.sunny.domain.extend.Sunny;

public interface TemplateService {

	public void save(Template template);

	public void update(Template template);

	public Template find(Long templateId);

	public List<Template> getTemplates(Sunny sunny, Stream stream);

	public void delete(Long templateId);

}