package cn.com.dao;

import java.util.List;

import cn.com.entity.Mf_comment;
import cn.com.entity.Mf_new;

public interface Mf_commentDaoInf {
	public int insertComment(Mf_comment comment);
	public List<Mf_comment> selCommentByNew(Mf_new n);
}
