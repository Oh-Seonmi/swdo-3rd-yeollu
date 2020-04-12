package com.yeollu.getrend.store.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yeollu.getrend.crawler.CrawlerExecutor;
import com.yeollu.getrend.store.dao.InstaLocationDAO;
import com.yeollu.getrend.store.dao.MangoDayDAO;
import com.yeollu.getrend.store.dao.MangoStoreDAO;
import com.yeollu.getrend.store.dao.MangoTimeDAO;
import com.yeollu.getrend.store.dao.SearchedStoreDAO;
import com.yeollu.getrend.store.dao.StoreDAO;
import com.yeollu.getrend.store.util.map.core.LocationDistance;
import com.yeollu.getrend.store.util.map.core.Polygon;
import com.yeollu.getrend.store.util.map.model.Point;
import com.yeollu.getrend.store.util.preprocess.core.DayOfTheWeekCategorizer;
import com.yeollu.getrend.store.util.preprocess.core.QueryStringSender;
import com.yeollu.getrend.store.util.preprocess.core.StringPreprocessor;
import com.yeollu.getrend.store.util.preprocess.core.TimeCategorizer;
import com.yeollu.getrend.store.vo.ReqParmVO;
import com.yeollu.getrend.store.vo.InstaImageVO;
import com.yeollu.getrend.store.vo.InstaLocationVO;
import com.yeollu.getrend.store.vo.InstaStoreInfoVO;
import com.yeollu.getrend.store.vo.InstaStoreVO;
import com.yeollu.getrend.store.vo.MangoDayVO;
import com.yeollu.getrend.store.vo.MangoStoreInfoVO;
import com.yeollu.getrend.store.vo.MangoStoreVO;
import com.yeollu.getrend.store.vo.MangoTimeVO;
import com.yeollu.getrend.store.vo.StoreVO;
import com.yeollu.getrend.user.util.ProfileImageHandler;

@Controller
public class HomeController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private StoreDAO storeDAO;

	@Autowired
	private SearchedStoreDAO searchedDAO;

	@Autowired
	private InstaLocationDAO instaLocationDAO;

	@Autowired
	private MangoStoreDAO mangoStoreDAO;

	@Autowired
	private MangoDayDAO mangoDayDAO;
	
	@Autowired
	private MangoTimeDAO mangoTimeDAO;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(HttpSession session, Model model) {
		return "home";
	}

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<InstaStoreInfoVO> search(@RequestBody ReqParmVO reqParm, HttpSession session) {
		logger.info("search");
		long startTime = System.currentTimeMillis();
		
		ArrayList<Point> points = reqParm.getPoints();
		ArrayList<String> categoryValues = reqParm.getCategoryValues();
		ArrayList<String> opentimeValues = reqParm.getOpentimeValues();
		logger.info("points : {}", points);
		logger.info("categoryValues : {}", categoryValues);
		logger.info("opentimeValues : {}", opentimeValues);
		
		// DB에 저장된 모든 상가 리스트를 가져옴
		// ArrayList<StoreVO> list = storeDAO.selectAllStores();
		
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

		// 망고플레이트 정보 추가 + 크롤링 요청할 로케이션 아이디 리스트 생성
		ArrayList<MangoStoreInfoVO> mangoStoreInfoList = new ArrayList<MangoStoreInfoVO>();
		ArrayList<String> locationList = new ArrayList<String>();
		for(InstaStoreVO instaStore : instaStoreList) {
			MangoStoreInfoVO mangoStoreInfo = new MangoStoreInfoVO();
//			mangoStoreInfo = mangoStoreDAO.selectMangoStoreInfoByStoreNo(instaStore.getStore_no());
			mangoStoreInfo = mangoStoreDAO.selectMangoStoreInfoByStoreNo(instaStore.getStore_no(), opentimeValues);
			mangoStoreInfoList.add(mangoStoreInfo);
			locationList.add(instaStore.getLocation_id());
		}

		// 인스타그램 크롤링 요청
		ArrayList<InstaImageVO> instaImageList = new ArrayList<InstaImageVO>();
		ArrayList<CrawlerExecutor> crawlerExecutorList = new ArrayList<CrawlerExecutor>();
		for (String location : locationList) {
			CrawlerExecutor crawlerExecutor = new CrawlerExecutor();
			crawlerExecutor.setLocation(location);
			new Thread(crawlerExecutor, "crawling :  " + location).start();
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
//				instaStoreInfo.setMangoStore(mangoStoreList.get(i));
				instaStoreInfo.setMangoStoreInfo(mangoStoreInfoList.get(i));

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
		session.setAttribute("istores", instaStoreInfoList);

		long endTime = System.currentTimeMillis();
		long diff = (endTime - startTime) / 1000;
		logger.info("걸린 시간 : {}", diff);

		return instaStoreInfoList;
	}
	
	@RequestMapping(value = "/recommend", method = RequestMethod.GET)
	@ResponseBody
	public ArrayList<InstaStoreInfoVO> recommend(HttpSession session, @RequestParam(value = "adr") String adr) {
		logger.info("adr : {}", adr);
		if(session.getAttribute("recommendIStores") != null) {
			return (ArrayList<InstaStoreInfoVO>) session.getAttribute("recommendIStores");
		}
		
		String store_adr = "";
		String[] temps = adr.split(" ");
		for(String str : temps) {
			if(str.contains("동")) {
				store_adr = str;
				break;
			}
		}
		logger.info("store_adr : {}", store_adr);
		
		
		// DB에 저장된 모든 상가들 중 접속한 동에 속한 상가 리스트만 가져옴
		ArrayList<StoreVO> storeList = storeDAO.selectStoresByStoreAdr(store_adr);
		
		ArrayList<StoreVO> selectedStoreList = new ArrayList<StoreVO>();
		if(storeList.size() > 3) {
			selectedStoreList = new ArrayList<StoreVO>(storeList.subList(0, 3));
		} else {
			selectedStoreList = storeList;
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
		
		ArrayList<String> opentimeValues = new ArrayList<String>();
		opentimeValues.add("일");
		opentimeValues.add("월");
		opentimeValues.add("화");
		opentimeValues.add("수");
		opentimeValues.add("목");
		opentimeValues.add("금");
		opentimeValues.add("토");

		// 망고플레이트 정보 추가 + 크롤링 요청할 로케이션 아이디 리스트 생성
		ArrayList<MangoStoreInfoVO> mangoStoreInfoList = new ArrayList<MangoStoreInfoVO>();
		ArrayList<String> locationList = new ArrayList<String>();
		for(InstaStoreVO instaStore : instaStoreList) {
			MangoStoreInfoVO mangoStoreInfo = new MangoStoreInfoVO();
			mangoStoreInfo = mangoStoreDAO.selectMangoStoreInfoByStoreNo(instaStore.getStore_no(), opentimeValues);
			mangoStoreInfoList.add(mangoStoreInfo);
			locationList.add(instaStore.getLocation_id());
		}

		// 인스타그램 크롤링 요청
		ArrayList<InstaImageVO> instaImageList = new ArrayList<InstaImageVO>();
		ArrayList<CrawlerExecutor> crawlerExecutorList = new ArrayList<CrawlerExecutor>();
		for (String location : locationList) {
			CrawlerExecutor crawlerExecutor = new CrawlerExecutor();
			crawlerExecutor.setLocation(location);
			new Thread(crawlerExecutor, "crawling :  " + location).start();
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

				if (instaImageList.size() > i) {
					instaStoreInfo.setInstaImage(instaImageList.get(i));
				} else {
					instaStoreInfo.setInstaImage(null);
				}
				instaStoreInfoList.add(instaStoreInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		session.setAttribute("recommendIStores", instaStoreInfoList);
		
		return instaStoreInfoList;
	}
	
}
