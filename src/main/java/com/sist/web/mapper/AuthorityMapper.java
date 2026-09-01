package com.sist.web.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.sist.web.vo.AuthorityVO;

@Mapper
@Repository
public interface AuthorityMapper {

	@Select("SELECT no,member_id,authority "
			+"FROM authority "
			+"WHERE member_id=#{member_id}")
	// 권한 여러개인 사람이 있으니 list로 받는다
	public List<AuthorityVO> getAuthorityData(int member_id);
}
