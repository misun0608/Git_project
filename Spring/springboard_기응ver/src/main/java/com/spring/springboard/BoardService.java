package com.spring.springboard;

import java.util.ArrayList;

public interface BoardService {
	
	public int insertArticle(BoardVO article) throws Exception;
	public ArrayList<BoardVO> getArticles(int start, int end) throws Exception;
	public int getArticleCount() throws Exception;
	public BoardVO getArticle(int num) throws Exception;
	public BoardVO updateGetArticle(int num) throws Exception;
	public int updateArticle(BoardVO article) throws Exception;
	public int deleteArticle(int num, String passwd) throws Exception;
}
