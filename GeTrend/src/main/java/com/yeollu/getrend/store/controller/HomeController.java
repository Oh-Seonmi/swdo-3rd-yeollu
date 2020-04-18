package com.yeollu.getrend.store.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yeollu.getrend.crawler.CrawlerExecutor;
import com.yeollu.getrend.store.dao.InstaLocationDAO;
import com.yeollu.getrend.store.dao.MangoStoreDAO;
import com.yeollu.getrend.store.dao.ScoreDAO;
import com.yeollu.getrend.store.dao.StoreDAO;
import com.yeollu.getrend.store.util.map.core.Polygon;
import com.yeollu.getrend.store.util.map.model.Point;
import com.yeollu.getrend.store.util.preprocess.core.QueryStringSender;
import com.yeollu.getrend.store.vo.InstaImageVO;
import com.yeollu.getrend.store.vo.InstaLocationVO;
import com.yeollu.getrend.store.vo.InstaStoreInfoVO;
import com.yeollu.getrend.store.vo.InstaStoreVO;
import com.yeollu.getrend.store.vo.MangoStoreInfoVO;
import com.yeollu.getrend.store.vo.PostImageVO;
import com.yeollu.getrend.store.vo.ReqParmVO;
import com.yeollu.getrend.store.vo.ScoreVO;
import com.yeollu.getrend.store.vo.StoreVO;

@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	private static final int SIZE_OF_LIST = 5;

	@Autowired
	private StoreDAO storeDAO;

	@Autowired
	private InstaLocationDAO instaLocationDAO;

	@Autowired
	private MangoStoreDAO mangoStoreDAO;

	@Autowired
	private ScoreDAO scoreDAO;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpSession session, Model model) {
		
		return "home";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<InstaStoreInfoVO> search(@RequestBody ReqParmVO reqParm, HttpSession session) {
		logger.info("검색 시작");
		long startTime = System.currentTimeMillis();
		
		if(session.getAttribute("istores") != null) {
			logger.info("current istores : {}", session.getAttribute("istores"));
			session.removeAttribute("istores");
		}
		
		ArrayList<Point> points = reqParm.getPoints();
		ArrayList<String> categoryValues = reqParm.getCategoryValues();
		ArrayList<String> opentimeValues = reqParm.getOpentimeValues();
		logger.info("points : {}", points);
		logger.info("categoryValues : {}", categoryValues);
		logger.info("opentimeValues : {}", opentimeValues);
		
		
		// DB에 저장된 모든 상가 리스트 중에서 카테고리에 해당되는 상가들만 조회
		ArrayList<StoreVO> storeList = storeDAO.selectStoresByStoreCate1(categoryValues);
		logger.info("storeList size : {}", storeList.size());

		
		// View로부터 넘겨받은 다각형의 꼭지점을 이용해 좌표상의 다각형 생성하여 판별
		Polygon polygon = new Polygon();
		for(Point point : points) {
			polygon.addPoint(point);
		}

		
		// 상가 리스트(storeList) 중에서 다각형 내부에 존재하는 상가들만 추출 => selectedStoreList
		ArrayList<StoreVO> selectedStoreList = new ArrayList<StoreVO>();
		for(StoreVO store : storeList) {
			if (polygon.isContains(store.getStore_x(), store.getStore_y())) {
				selectedStoreList.add(store);
			}
		}
		logger.info("selectedStoreList size : {}", selectedStoreList.size());

		
		// 인스타그램에 쿼리스트링을 보내 상가의 위치 정보 수집하여 location_id를 하나 리턴받아
		// InstaStore 객체 생성하여 instaStoreList에 수집
		ArrayList<InstaStoreVO> instaStoreList = new ArrayList<InstaStoreVO>();
		for (StoreVO store : selectedStoreList) {
			String location_id = QueryStringSender.send(store);
			if (location_id == null || location_id.equals("")) {
			} else {
				if (!instaLocationDAO.isExistedInstaLocation(location_id)) {
					InstaLocationVO instaLocation = new InstaLocationVO();
					instaLocation.setLocation_id(location_id);
					instaLocation.setStore_no(store.getStore_no());
					instaLocationDAO.insertInstaLocation(instaLocation);
				}
				InstaStoreVO instaStore = storeDAO.selectInstaStore(store.getStore_no());
				if (instaStore != null) {
					instaStore.setLocation_id(location_id);
					instaStoreList.add(instaStore);
				}
			}
		}
		logger.info("instaStoreList size : {}", instaStoreList.size());

		
		// 망고플레이트 정보 추가
		ArrayList<MangoStoreInfoVO> mangoStoreInfoList = new ArrayList<MangoStoreInfoVO>();
		for(InstaStoreVO instaStore : instaStoreList) {
			MangoStoreInfoVO mangoStoreInfo = new MangoStoreInfoVO();
			mangoStoreInfo = mangoStoreDAO.selectMangoStoreInfoByStoreNoAndDays(instaStore.getStore_no(), opentimeValues);
			mangoStoreInfoList.add(mangoStoreInfo);
		}
		
		
		// 좋아요와 별점을 기반으로 스코어가 높은 순으로 정렬
		ArrayList<ScoreVO> scoreList = new ArrayList<ScoreVO>();
		for(InstaStoreVO instaStore : instaStoreList) {
			ScoreVO score = scoreDAO.selectScoreByStoreNo(instaStore.getStore_no());
			
			if(score == null) {
				score = new ScoreVO();
				score.setStore_no(instaStore.getStore_no());
				score.setSum_of_like(0);
				score.setSum_of_star(0.0);
			}
			scoreList.add(score);
		}
		
		Collections.sort(scoreList, new Comparator<ScoreVO>() {
			public int compare(ScoreVO score1, ScoreVO score2) {
				if((score1.getSum_of_like() * 100 + score1.getSum_of_like()) < (score2.getSum_of_like() * 100 + score2.getSum_of_like())) {
					return 1;
				} else {
					return -1;
				}
				
			};
		});
		
		ArrayList<InstaStoreVO> _instaStoreList = new ArrayList<InstaStoreVO>();
		for(int i = 0; i < scoreList.size(); i++) {
			for(int j = 0; j < instaStoreList.size(); j++) {
				if(scoreList.get(i).getStore_no().equals(instaStoreList.get(j).getStore_no())) {
					_instaStoreList.add(instaStoreList.get(j));
					break;
				}
			}
		}
		if(_instaStoreList.size() > SIZE_OF_LIST) {
			instaStoreList = new ArrayList<InstaStoreVO> (_instaStoreList.subList(0, SIZE_OF_LIST));
		} else {
			instaStoreList = _instaStoreList;
		}
		logger.info("instaStoreList size : {}", instaStoreList.size());
		
		

		// 인스타그램 크롤링 요청
		ArrayList<InstaImageVO> instaImageList = new ArrayList<InstaImageVO>();
		ArrayList<CrawlerExecutor> crawlerExecutorList = new ArrayList<CrawlerExecutor>();
		for (InstaStoreVO instaStore : instaStoreList) {
			CrawlerExecutor crawlerExecutor = new CrawlerExecutor();
			crawlerExecutor.setLocation(instaStore.getLocation_id());
			new Thread(crawlerExecutor, "crawling :  " + instaStore.getLocation_id()).start();
			crawlerExecutorList.add(crawlerExecutor);
		}
		for (CrawlerExecutor crawlerExecutor : crawlerExecutorList) {
			instaImageList.add(crawlerExecutor.getInstaImage());
		}
		CrawlerExecutor.killChromeDriver();

		
		// View로 보낼 최종 객체 리스트
		ArrayList<InstaStoreInfoVO> instaStoreInfoList = new ArrayList<InstaStoreInfoVO>();
		try {
			for (int i = 0; i < instaStoreList.size(); i++) {
				InstaStoreInfoVO instaStoreInfo = new InstaStoreInfoVO();
				instaStoreInfo.setInstaStore(instaStoreList.get(i));
				instaStoreInfo.setMangoStoreInfo(mangoStoreInfoList.get(i));
				instaStoreInfo.setScore(scoreList.get(i));

				if (instaImageList.size() > i) {
					instaStoreInfo.setInstaImage(instaImageList.get(i));
				} else {
					instaStoreInfo.setInstaImage(null);
				}
				logger.info("instaStoreInfo name : {}", instaStoreInfo.getInstaStore().getStore_name());
				instaStoreInfoList.add(instaStoreInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("instaStoreInfoList size : {}", instaStoreInfoList.size());
		
		
		// 인스타그램 좋아요가 높은 순으로 재정렬
		for(InstaStoreInfoVO instaStoreInfo : instaStoreInfoList) {
			ScoreVO score = instaStoreInfo.getScore();
			
			int sum = 0;
			if(instaStoreInfo.getInstaImage().getPostImgList() != null) {
				for(PostImageVO postImage : instaStoreInfo.getInstaImage().getPostImgList()) {
					sum += postImage.getLike();
				}
			}
			score.setSum_of_insta_like(sum);
			instaStoreInfo.setScore(score);
		}
		
		Collections.sort(instaStoreInfoList, new Comparator<InstaStoreInfoVO>() {
			public int compare(InstaStoreInfoVO instaScoreInfo1, InstaStoreInfoVO instaScoreInfo2) {
				if(instaScoreInfo1.getScore().getSum_of_insta_like() < instaScoreInfo2.getScore().getSum_of_insta_like()) {
					return 1;
				} else {
					return -1;
				}
			};
		});
		
		
		// 세션에 저장
		session.setAttribute("istores", instaStoreInfoList);

		long endTime = System.currentTimeMillis();
		long diff = (endTime - startTime) / 1000;
		logger.info("걸린 시간 : {}", diff);
		logger.info("검색 종료");

		return instaStoreInfoList;
	}
	
	
	
}
