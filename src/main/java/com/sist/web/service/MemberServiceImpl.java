package com.sist.web.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sist.web.mapper.AuthorityMapper;
import com.sist.web.mapper.MemberMapper;
import com.sist.web.vo.AuthorityVO;
import com.sist.web.vo.MemberVO;

import lombok.RequiredArgsConstructor;
/*        
 * => 결합성이 낮음 => 다른 클래스에 영향이 없음 => 유지보수에 주로 사용
 *  User <------> Controller <------> Service <------> Repository
 *          |
 *       Security(여기서 인증을 거친다)
 */     
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

	private final MemberMapper mMapper;
	private final AuthorityMapper aMapper;
	@Override
	public MemberVO findByUsername(String username) {
		// TODO Auto-generated method stub
		return mMapper.findByUsername(username);
	}
	@Override
	public List<AuthorityVO> getAuthorityData(int member_id) {
		// TODO Auto-generated method stub
		return aMapper.getAuthorityData(member_id);
	}
}
