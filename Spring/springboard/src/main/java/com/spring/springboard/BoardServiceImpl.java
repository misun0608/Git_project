package com.spring.springboard;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("boardService")
public class BoardServiceImpl implements BoardService {
	@Autowired(required=false)
	private BoardDAO boardDAO = null;
	
	// 게시판 글 개수 구하기
	@Override
	public int getArticleCount() throws Exception {
		try {
			int res = boardDAO.getArticleCount();
			return res;
		}catch(Exception e) {
			throw new Exception("글 개수구하기 실패", e);
		}
	}

	@Override
	public ArrayList<BoardVO> getArticles(int start, int end) throws Exception {
		try {
			ArrayList<BoardVO> article_list = new ArrayList<BoardVO>();
			article_list = boardDAO.getArticles(start, end);
			return article_list;
		}catch(Exception e) {
			throw new Exception("글 리스트 검색 실패", e);
		}
	}

	@Override
	public void insertArticle(BoardVO article) throws Exception {
		try {
			boardDAO.insertArticle(article);
		}catch(Exception e) {
			throw new Exception("글쓰기 실패", e);
		}
	}

	@Override
	public BoardVO getArticle(int num) throws Exception {
		try {
			BoardVO vo = boardDAO.getArticle(num);
			return vo;
		}catch(Exception e) {
			throw new Exception("글 내용 불러오기 실패", e);
		}
	}
	
	@Override
	public BoardVO updateGetArticle(int num) throws Exception{
		try {
			BoardVO vo = boardDAO.getArticle(num);
			return vo;
		}catch(Exception e) {
			throw new Exception("수정내용 불러오기 실패", e);
		}
	}
	
	@Override
	public int updateArticle(BoardVO article) throws Exception{
		try {
			int res = boardDAO.updateArticle(article);
			return res;
		}catch(Exception e) {
			throw new Exception("수정 실패", e);
		}
	}
	
	@Override
	public int deleteArticle(int num, String passwd) throws Exception{
		try {
			int res = boardDAO.deleteArticle(num, passwd);
			return res;
		}catch(Exception e) {
			throw new Exception("삭제 실패", e);
		}
	}
	
}
