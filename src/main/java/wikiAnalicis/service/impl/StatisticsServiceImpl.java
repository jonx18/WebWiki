package wikiAnalicis.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import wikiAnalicis.dao.StatisticsDAO;
import wikiAnalicis.entity.Page;
import wikiAnalicis.entity.statics.PageStatistics;
import wikiAnalicis.service.StatisticsService;

@Service
@Transactional
public class StatisticsServiceImpl implements StatisticsService {
	@Autowired
	private StatisticsDAO statisticsDAO;

	public StatisticsServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public long createPageStatistics(PageStatistics pageStatistics) {
		return statisticsDAO.createPageStatistics(pageStatistics);
	}

	@Override
	public PageStatistics mergePageStatistics(PageStatistics pageStatistics) {
		return statisticsDAO.mergePageStatistics(pageStatistics);
	}

	@Override
	public PageStatistics updatePageStatistics(PageStatistics pageStatistics) {
		return statisticsDAO.updatePageStatistics(pageStatistics);
	}

	@Override
	public void deletePageStatistics(long id) {
		statisticsDAO.deletePageStatistics(id);
	}

	@Override
	public List<PageStatistics> getAllPagesStatistics() {
		return statisticsDAO.getAllPagesStatistics();
	}

	@Override
	public PageStatistics getPageStatistics(long id) {
		return statisticsDAO.getPageStatistics(id);
	}

	@Override
	public PageStatistics getPageStatistics(Page page) {
		return statisticsDAO.getPageStatistics(page);
	}

	@Override
	public List<PageStatistics> list(Integer offset, Integer maxResults) {
		return statisticsDAO.list(offset, maxResults);
	}





}
