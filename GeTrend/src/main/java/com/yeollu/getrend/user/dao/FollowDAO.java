package com.yeollu.getrend.user.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.yeollu.getrend.user.vo.FollowVO;
import com.yeollu.getrend.user.vo.UserVO;

@Repository
public class FollowDAO {
	@Autowired
	private SqlSession session;

	public int insertFollow(FollowVO follow) {
		int cnt = 0;
		try {
			FollowMapper mapper = session.getMapper(FollowMapper.class);
			cnt = mapper.insertFollow(follow);
		} catch (Exception e) {
			e.printStackTrace();
		}return cnt;
	}

	public int deleteFollow(FollowVO follow) {
		int cnt = 0;
		try {
			FollowMapper mapper = session.getMapper(FollowMapper.class);
			cnt = mapper.deleteFollow(follow);
		} catch (Exception e) {
			e.printStackTrace();
		}return cnt;
	}
	
	public int checkFollow(FollowVO follow) {
		int ck = 0;
		try {
			FollowMapper mapper = session.getMapper(FollowMapper.class);
			ck = mapper.checkFollow(follow);			
		} catch (Exception e) {
			e.printStackTrace();
		}return ck;
	}
	public int countFollow(String user_email){
		int cnt = 0;
		try {
			FollowMapper mapper = session.getMapper(FollowMapper.class);
			cnt = mapper.countFollow(user_email);
		} catch (Exception e) {
			e.printStackTrace();
		}return cnt;
	}
	public int countFollower(String user_email){
		int cnt = 0;
		try {
			FollowMapper mapper = session.getMapper(FollowMapper.class);
			cnt = mapper.countFollower(user_email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cnt;
		}
	}
	
	
	public ArrayList<HashMap<String, Object>> selectFollowing(String user_email){
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {
			FollowMapper mapper = session.getMapper(FollowMapper.class);
			list = mapper.selectFollowing(user_email);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<HashMap<String, Object>> selectFollower(String follows_following){
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		try {
			FollowMapper mapper = session.getMapper(FollowMapper.class);
			list = mapper.selectFollower(follows_following);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

