package com.yeollu.getrend.user.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yeollu.getrend.store.dao.InstaReplyDAO;
import com.yeollu.getrend.store.dao.LikeDAO;
import com.yeollu.getrend.store.vo.InstaReplyVO;
import com.yeollu.getrend.store.vo.LikeVO;
import com.yeollu.getrend.user.dao.FollowDAO;
import com.yeollu.getrend.user.dao.UserDAO;
import com.yeollu.getrend.user.vo.FollowVO;
import com.yeollu.getrend.user.vo.UserVO;

/**
 * @Class 	: MypageController.java
 * @Package	: com.yeollu.getrend.user.controller
 * @Project : GeTrend
 * @Author	: 조은채
 * @Since	: 2020. 3. 31.
 * @Version	: 1.0
 * @Desc	: 마이페이지 관련 작업을 수행한다.
 */
@Controller
@RequestMapping(value="/mypage")
public class MypageController {
	
	/**
	 * Fields
	 */
	private static final Logger logger = LoggerFactory.getLogger(MypageController.class);
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private InstaReplyDAO replyDAO;
	@Autowired
	private LikeDAO likeDAO;
	@Autowired
	private FollowDAO followDAO;
	
	/**
	 * @Method	: mypage
	 * @Return	: String
	 * @Author	: 조은채
	 * @Since	: 2020. 3. 31.
	 * @Version	: 1.0
	 * @Desc	: 마이페이지를 구성하는 정보를 모델에 저장한 후 페이지를 전환한다.
	 * @param user_name
	 * @param session
	 * @param model
	 */
	@RequestMapping(value = "/mypage", method = RequestMethod.GET)
	public String mypage(String user_name, HttpSession session, Model model) {
		String user_email = (String)session.getAttribute("loginemail");		
		//내 프로필
		if(user_name.equals(userDAO.selectEmail(user_email).getUser_name())) {
			logger.info("내 프로필");
			model.addAttribute("user", userDAO.selectEmail(user_email));
			model.addAttribute("like", likeDAO.likeStoreCountByEmail(user_email));
			model.addAttribute("follow", followDAO.countFollow(user_email));
			model.addAttribute("follower", followDAO.countFollower(user_email));
			ArrayList<HashMap<String, Object>> likeList = likeDAO.likeSelectByEmail(user_email);
			
			for (HashMap<String, Object> hashMap : likeList) {		
				//category 구분
				switch ((String)hashMap.get("STORE_CATE1")) {
				case "한식": 	case "닭/오리요리": case "별식/퓨전요리":case "분식": hashMap.put("CATE1", "korean.png"); hashMap.put("CATE2", "한식");break;
				case "양식": hashMap.put("CATE1", "western.png"); hashMap.put("CATE2", "양식");break;
				case "일식/수산물": hashMap.put("CATE1", "japanese.png"); hashMap.put("CATE2", "일식/수산물");break;
				case "중식": hashMap.put("CATE1", "chinese.png"); hashMap.put("CATE2", "중식");break;
				case "카페": case "제과제빵떡케익":hashMap.put("CATE1", "cafe.png");hashMap.put("CATE2", "카페/디저트");break;
				case "치킨": case "패스트푸드": hashMap.put("CATE1", "chicken.png");hashMap.put("CATE2", "치킨/피자<br>패스트푸드");break;
				case "기타음식업": case "뷔페":case "유흥주점":case "음식배달서비스":  hashMap.put("CATE1", "others.png");hashMap.put("CATE2", "기타");break;
				}
			}	
			model.addAttribute("likeList", likeList);
			
			
			ArrayList<HashMap<String, Object>> replyList = replyDAO.replyListByEmail(user_email);
			
			for (HashMap<String, Object> hashMap : replyList) {
				//좋아요한 가게인지 확인
				LikeVO like = new LikeVO();
				like.setStore_no((String)hashMap.get("STORE_NO"));
				like.setUser_email((String)hashMap.get("USER_EMAIL"));
				int likeCheck = likeDAO.likeSelectByEmailStoreno(like);
				if(likeCheck == 1) hashMap.put("LIKE", true);
				else hashMap.put("LIKE", false);	
				
				//category 구분
				switch ((String)hashMap.get("STORE_CATE1")) {
				case "한식": 	case "닭/오리요리": case "별식/퓨전요리":case "분식": hashMap.put("CATE1", "korean.png"); hashMap.put("CATE2", "한식");break;
				case "양식": hashMap.put("CATE1", "western.png"); hashMap.put("CATE2", "양식");break;
				case "일식/수산물": hashMap.put("CATE1", "japanese.png"); hashMap.put("CATE2", "일식/수산물");break;
				case "중식": hashMap.put("CATE1", "chinese.png"); hashMap.put("CATE2", "중식");break;
				case "카페": case "제과제빵떡케익":hashMap.put("CATE1", "cafe.png");hashMap.put("CATE2", "카페/디저트");break;
				case "치킨": case "패스트푸드": hashMap.put("CATE1", "chicken.png");hashMap.put("CATE2", "치킨/피자<br>패스트푸드");break;
				case "기타음식업": case "뷔페":case "유흥주점":case "음식배달서비스":  hashMap.put("CATE1", "others.png");hashMap.put("CATE2", "기타");break;
				}
			}
			model.addAttribute("replyList",replyList);	
			
			
		//남 프로필	
		}else {
			logger.info("남 프로필");
			UserVO user = userDAO.selectName(user_name);
			model.addAttribute("user", user);
			logger.info("user : {}", user);
			model.addAttribute("like", likeDAO.likeStoreCountByEmail(user.getUser_email()));
			model.addAttribute("follow", followDAO.countFollow(user.getUser_email()));
			model.addAttribute("follower", followDAO.countFollower(user.getUser_email()));
			ArrayList<HashMap<String, Object>> likeList = likeDAO.likeSelectByEmail(user.getUser_email());
			
			for (HashMap<String, Object> hashMap : likeList) {		
				//category 구분
				switch ((String)hashMap.get("STORE_CATE1")) {
				case "한식": 	case "닭/오리요리": case "별식/퓨전요리":case "분식": hashMap.put("CATE1", "korean.png"); hashMap.put("CATE2", "한식");break;
				case "양식": hashMap.put("CATE1", "western.png"); hashMap.put("CATE2", "양식");break;
				case "일식/수산물": hashMap.put("CATE1", "japanese.png"); hashMap.put("CATE2", "일식/수산물");break;
				case "중식": hashMap.put("CATE1", "chinese.png"); hashMap.put("CATE2", "중식");break;
				case "카페": case "제과제빵떡케익":hashMap.put("CATE1", "cafe.png");hashMap.put("CATE2", "카페/디저트");break;
				case "치킨": case "패스트푸드": hashMap.put("CATE1", "chicken.png");hashMap.put("CATE2", "치킨/피자<br>패스트푸드");break;
				case "기타음식업": case "뷔페":case "유흥주점":case "음식배달서비스":  hashMap.put("CATE1", "others.png");hashMap.put("CATE2", "기타");break;
				}
			}	
			model.addAttribute("likeList", likeList);			
			
			ArrayList<HashMap<String, Object>> replyList = replyDAO.replyListByEmail(user.getUser_email());
			for (HashMap<String, Object> hashMap : replyList) {
				//좋아요한 가게인지 확인
				LikeVO like = new LikeVO();
				like.setStore_no((String)hashMap.get("STORE_NO"));
				like.setUser_email((String)hashMap.get("USER_EMAIL"));
				int likeCheck = likeDAO.likeSelectByEmailStoreno(like);
				if(likeCheck == 1) hashMap.put("LIKE", true);
				else hashMap.put("LIKE", false);
				
				//category 구분
				switch ((String)hashMap.get("STORE_CATE1")) {
				case "한식": 	case "닭/오리요리": case "별식/퓨전요리":case "분식": hashMap.put("CATE1", "korean.png"); hashMap.put("CATE2", "한식");break;
				case "양식": hashMap.put("CATE1", "western.png"); hashMap.put("CATE2", "양식");break;
				case "일식/수산물": hashMap.put("CATE1", "japanese.png"); hashMap.put("CATE2", "일식/수산물");break;
				case "중식": hashMap.put("CATE1", "chinese.png"); hashMap.put("CATE2", "중식");break;
				case "카페": case "제과제빵떡케익":hashMap.put("CATE1", "cafe.png");hashMap.put("CATE2", "카페/디저트");break;
				case "치킨": case "패스트푸드": hashMap.put("CATE1", "chicken.png");hashMap.put("CATE2", "치킨/피자<br>패스트푸드");break;
				case "기타음식업": case "뷔페":case "유흥주점":case "음식배달서비스":  hashMap.put("CATE1", "others.png");hashMap.put("CATE2", "기타");break;
				}
			}
			model.addAttribute("replyList",replyList);
			
			//내가 팔로잉 중인 사람인지 체크
			FollowVO follow = new FollowVO();
			follow.setUser_email(user_email);
			follow.setFollows_following(user.getUser_email());
			int ck = followDAO.checkFollow(follow);
			if(ck == 1) {
				model.addAttribute("following", true);
			}else {
				model.addAttribute("following", false);
			}
		}
		return "mypage/mypage";
	}
	
	/**
	 * @Method	: mypage
	 * @Return	: String
	 * @Author	: 조은채
	 * @Since	: 2020. 3. 31.
	 * @Version	: 1.0
	 * @Desc	: user_name이 아님 loginemail로 접속했을 때의 마이페이지를 구성하는 정보를 모델에 저장한 후 페이지를 전환한다.
	 * @param session
	 * @param model
	 */
	@RequestMapping(value = "/mypageSession", method = RequestMethod.GET)
	public String mypage(HttpSession session, Model model) {
		
		String user_email = (String)session.getAttribute("loginemail");		
			
		model.addAttribute("user", userDAO.selectEmail(user_email));
		model.addAttribute("like", likeDAO.likeStoreCountByEmail(user_email));
		model.addAttribute("follow", followDAO.countFollow(user_email));
		model.addAttribute("follower", followDAO.countFollower(user_email));
		ArrayList<HashMap<String, Object>> likeList = likeDAO.likeSelectByEmail(user_email);

		for (HashMap<String, Object> hashMap : likeList) {		
			//category 구분
			switch ((String)hashMap.get("STORE_CATE1")) {
			case "한식": 	case "닭/오리요리": case "별식/퓨전요리":case "분식": hashMap.put("CATE1", "korean.png"); hashMap.put("CATE2", "한식");break;
			case "양식": hashMap.put("CATE1", "western.png"); hashMap.put("CATE2", "양식");break;
			case "일식/수산물": hashMap.put("CATE1", "japanese.png"); hashMap.put("CATE2", "일식/수산물");break;
			case "중식": hashMap.put("CATE1", "chinese.png"); hashMap.put("CATE2", "중식");break;
			case "카페": case "제과제빵떡케익":hashMap.put("CATE1", "cafe.png");hashMap.put("CATE2", "카페/디저트");break;
			case "치킨": case "패스트푸드": hashMap.put("CATE1", "chicken.png");hashMap.put("CATE2", "치킨/피자<br>패스트푸드");break;
			case "기타음식업": case "뷔페":case "유흥주점":case "음식배달서비스":  hashMap.put("CATE1", "others.png");hashMap.put("CATE2", "기타");break;
			}
		}	
		model.addAttribute("likeList", likeList);
		
		ArrayList<HashMap<String, Object>> replyList = replyDAO.replyListByEmail(user_email);
		for (HashMap<String, Object> hashMap : replyList) {
			//좋아요한 가게인지 확인
			LikeVO like = new LikeVO();
			like.setStore_no((String)hashMap.get("STORE_NO"));
			like.setUser_email((String)hashMap.get("USER_EMAIL"));
			int likeCheck = likeDAO.likeSelectByEmailStoreno(like);
			if(likeCheck == 1) hashMap.put("LIKE", true);
			else hashMap.put("LIKE", false);
			
			//category 구분
			switch ((String)hashMap.get("STORE_CATE1")) {
			case "한식": 	case "닭/오리요리": case "별식/퓨전요리":case "분식": hashMap.put("CATE1", "korean.png"); hashMap.put("CATE2", "한식");break;
			case "양식": hashMap.put("CATE1", "western.png"); hashMap.put("CATE2", "양식");break;
			case "일식/수산물": hashMap.put("CATE1", "japanese.png"); hashMap.put("CATE2", "일식/수산물");break;
			case "중식": hashMap.put("CATE1", "chinese.png"); hashMap.put("CATE2", "중식");break;
			case "카페": case "제과제빵떡케익":hashMap.put("CATE1", "cafe.png");hashMap.put("CATE2", "카페/디저트");break;
			case "치킨": case "패스트푸드": hashMap.put("CATE1", "chicken.png");hashMap.put("CATE2", "치킨/피자<br>패스트푸드");break;
			case "기타음식업": case "뷔페":case "유흥주점":case "음식배달서비스":  hashMap.put("CATE1", "others.png");hashMap.put("CATE2", "기타");break;
			}
		}		
		model.addAttribute("replyList",replyList);	
	
		return "mypage/mypage";
	}
	
	/**
	 * @Method	: followerList
	 * @Return	: ArrayList<HashMap<String,Object>>
	 * @Author	: 조은채
	 * @Since	: 2020. 3. 31.
	 * @Version	: 1.0
	 * @Desc	: follows_following을 구독하는 사람 리스트를 반환한다.
	 * @param follows_following
	 * @param session
	 * @param model
	 */
	@RequestMapping(value = "/followerList", method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<HashMap<String, Object>> followerList(String follows_following, HttpSession session, Model model) {
		ArrayList<HashMap<String, Object>> list = followDAO.selectFollower(follows_following);
		logger.info("list {}",list);
		return list;
	}
	
	/**
	 * @Method	: followList
	 * @Return	: ArrayList<HashMap<String,Object>>
	 * @Author	: 조은채
	 * @Since	: 2020. 3. 31.
	 * @Version	: 1.0
	 * @Desc	: user_email이 구독하는 사람 리스트를 반환한다.
	 * @param user_email
	 * @param session
	 * @param model
	 */
	@RequestMapping(value = "/followList", method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<HashMap<String, Object>> followList(String user_email, HttpSession session, Model model) {
		ArrayList<HashMap<String, Object>> list = followDAO.selectFollowing(user_email);
		logger.info("list {}",list);
		return list;
	}
	
	/**
	 * @Method	: likeStoreList
	 * @Return	: ArrayList<HashMap<String,Object>>
	 * @Author	: 조은채
	 * @Since	: 2020. 3. 31.
	 * @Version	: 1.0
	 * @Desc	: user_email이 구독하는 가게 리스트를 반환한다.
	 * @param user_email
	 * @param session
	 * @param model
	 */
	@RequestMapping(value = "/likeStoreList", method = RequestMethod.POST)
	@ResponseBody
	public ArrayList<HashMap<String, Object>> likeStoreList(String user_email, HttpSession session, Model model) {
		ArrayList<HashMap<String, Object>> list = likeDAO.likeSelectByEmail(user_email);
		for (HashMap<String, Object> hashMap : list) {
			//category 구분
			switch ((String)hashMap.get("STORE_CATE1")) {
			case "한식": 	case "닭/오리요리": case "별식/퓨전요리":case "분식": hashMap.put("CATE1", "korean.png"); hashMap.put("CATE2", "한식");break;
			case "양식": hashMap.put("CATE1", "western.png"); hashMap.put("CATE2", "양식");break;
			case "일식/수산물": hashMap.put("CATE1", "japanese.png"); hashMap.put("CATE2", "일식/수산물");break;
			case "중식": hashMap.put("CATE1", "chinese.png"); hashMap.put("CATE2", "중식");break;
			case "카페": case "제과제빵떡케익":hashMap.put("CATE1", "cafe.png");hashMap.put("CATE2", "카페/디저트");break;
			case "치킨": case "패스트푸드": hashMap.put("CATE1", "chicken.png");hashMap.put("CATE2", "치킨/피자<br>패스트푸드");break;
			case "기타음식업": case "뷔페":case "유흥주점":case "음식배달서비스":  hashMap.put("CATE1", "others.png");hashMap.put("CATE2", "기타");break;
			}
		}		
		logger.info("list {}",list);
		return list;
	}
	
	/**
	 * @Method	: deleteReply
	 * @Return	: String
	 * @Author	: 조은채
	 * @Since	: 2020. 3. 31.
	 * @Version	: 1.0
	 * @Desc	: 댓글을 삭제한다.
	 * @param reply
	 * @param user_name
	 * @param session
	 */
	@RequestMapping(value = "/deleteReply", method = RequestMethod.GET)
	public String deleteReply(InstaReplyVO reply, String user_name, HttpSession session) {
		logger.info("{}", reply);
		String user_email = (String)session.getAttribute("loginemail");
		reply.setUser_email(user_email);
		int cnt = replyDAO.replyRemove(reply);
		if(cnt > 0) {
			logger.info("댓글 삭제 성공");
		} else {
			logger.info("댓글 삭제 실패");
		}
		String _user_name = "";
		try {
			_user_name = URLEncoder.encode(user_name,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			_user_name = userDAO.selectEmail(user_email).getUser_name();
		}
		return "redirect:mypage?user_name=" + _user_name;
	}
}
