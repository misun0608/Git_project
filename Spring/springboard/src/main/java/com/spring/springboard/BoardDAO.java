package com.spring.springboard;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.stereotype.Repository;

@Repository
public class BoardDAO {
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	// 게시판 글 개수 구하기
	public int getArticleCount() throws Exception {
		int count = 0;

		try {
			conn = JDBCUtil.getConnection();

			pstmt = conn.prepareStatement("select count(*) from sboard"); // count(*) : 행수를 구할 때 사용
			rs = pstmt.executeQuery();

			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			JDBCUtil.closeResource(rs, pstmt, conn);
		}
		return count;
	}

	// 게시판 글 리스트
	public ArrayList<BoardVO> getArticles(int start, int end) throws Exception {
		// int start, int end model로 받아야하나?
		ArrayList<BoardVO> articleList = null;
		String sql = null;

		try {
			conn = JDBCUtil.getConnection();
			sql = "select * from " + "(select rownum rnum, num, writer, passwd, subject, email, content, reg_date, "
					+ "readcount, ref, re_step, re_level from "
					+ "(select * from sboard order by ref desc, re_step asc)) " + "where rnum >= ? and rnum <= ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				articleList = new ArrayList<BoardVO>();
				do {
					BoardVO article = new BoardVO(); // 글 하나를 저장하기 위한 객체
					article.setNum(rs.getInt("num"));
					article.setWriter(rs.getString("writer"));
					article.setPasswd(rs.getString("passwd"));
					article.setSubject(rs.getString("subject"));
					article.setEmail(rs.getString("email"));
					article.setContent(rs.getString("content"));
					article.setReg_date(rs.getTimestamp("reg_date"));
					article.setReadcount(rs.getInt("readcount"));
					article.setRef(rs.getInt("ref"));
					article.setRe_step(rs.getInt("re_step"));
					article.setRe_level(rs.getInt("re_level"));
					articleList.add(article);
				} while (rs.next());
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			JDBCUtil.closeResource(rs, pstmt, conn);
		}
		return articleList;
	}

	// 글쓰기
	public void insertArticle(BoardVO article) throws Exception {
		// 매개변수 다른거 쓸 수 있을거같은데...

		int num = article.getNum();
		int ref = article.getRef();
		int re_step = article.getRe_step();
		int re_level = article.getRe_level();
		int number = 0;

		String sql = "";

		try {
			conn = JDBCUtil.getConnection();

			pstmt = conn.prepareStatement("select max(num) from sboard");
			rs = pstmt.executeQuery();

			if (rs.next()) {
				number = rs.getInt(1) + 1;
			} else {
				number = 1; // 최초의 글쓰기
			}

			// 원글에 대한 답글 or 답글에 대한 답글쓰기
			if (num != 0) {
				// ref 같은 애들 중에서 원글 re_step보다 큰 것들 1씩 증가
				sql = "update sboard set re_step = re_step +1 where ref = ? and re_step > ?";

				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, ref);
				pstmt.setInt(2, re_step);
				pstmt.executeUpdate();

				re_step = re_step + 1; // 댓글의 순서
				re_level = re_level + 1; // 들여쓰기
			}

			// 원글쓰기
			else {
				ref = number;
				re_step = 0;
				re_level = 0;
			}

			sql = "insert into sboard(num, writer, passwd, subject, email, content, reg_date, readcount, ";
			sql += "ref, re_step, re_level) values (sboard_seq.nextval,?,?,?,?,?,sysdate,?,?,?,?)";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, article.getWriter());
			pstmt.setString(2, article.getPasswd());
			pstmt.setString(3, article.getSubject());
			pstmt.setString(4, article.getEmail());
			pstmt.setString(5, article.getContent());
//			pstmt.setTimestamp(6, article.getReg_date());
			pstmt.setInt(6, article.getReadcount());
			pstmt.setInt(7, ref);
			pstmt.setInt(8, re_step);
			pstmt.setInt(9, re_level);

			pstmt.executeUpdate();

		} catch (Exception ex) {
			System.out.println("글쓰기 오류 : " + ex.getMessage());
			ex.printStackTrace();
		} finally {
			JDBCUtil.closeResource(rs, pstmt, conn);
		}
	}

	// 제목 클릭해서 글내용 들어갈때
	public BoardVO getArticle(int num) throws Exception {
		BoardVO article = null;

		try {
			conn = JDBCUtil.getConnection();

			pstmt = conn.prepareStatement("select * from sboard where num = ?");
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				article = new BoardVO();

				article.setNum(rs.getInt("num"));
				article.setWriter(rs.getString("writer"));
				article.setPasswd(rs.getString("passwd"));
				article.setSubject(rs.getString("subject"));
				article.setEmail(rs.getString("email"));
				article.setContent(rs.getString("content"));
				article.setReg_date(rs.getTimestamp("reg_date"));
				article.setReadcount(rs.getInt("readcount"));
				article.setRef(rs.getInt("ref"));
				article.setRe_step(rs.getInt("re_step"));
				article.setRe_level(rs.getInt("re_level"));

				pstmt = conn.prepareStatement("update sboard set readcount=? where num=?");
				pstmt.setInt(1, rs.getInt("readcount") + 1);
				pstmt.setInt(2, num);
				pstmt.executeUpdate();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			JDBCUtil.closeResource(rs, pstmt, conn);
		}
		return article;
	}

	// 글 수정 폼에 뿌려줄 데이터
	public BoardVO updateGetArticle(int num) throws Exception {
		BoardVO article = null;

		try {
			conn = JDBCUtil.getConnection();
			pstmt = conn.prepareStatement("select * from sboard where num = ?");
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				article = new BoardVO();
				article.setNum(rs.getInt("num"));
				article.setWriter(rs.getString("writer"));
				article.setPasswd(rs.getString("passwd"));
				article.setSubject(rs.getString("subject"));
				article.setEmail(rs.getString("email"));
				article.setContent(rs.getString("content"));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			JDBCUtil.closeResource(rs, pstmt, conn);
		}
		return article;

	}

	// 글 수정
	public int updateArticle(BoardVO article) throws Exception {
		String dbpasswd = "";
		String sql = "";
		int x = -1;
		try {
			conn = JDBCUtil.getConnection();

			pstmt = conn.prepareStatement("select passwd from sboard where num =?"); // 글수정 가능한지 패스워드 체크
			pstmt.setInt(1, article.getNum());
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dbpasswd = rs.getString("passwd");

				if (dbpasswd.equals(article.getPasswd())) { // 입력한 패스워드와 db패스워드 같은지 비교
					sql = "update sboard set writer=?,passwd=?,subject=?," + "email=?,content=? where num=?";

					pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, article.getWriter());
					pstmt.setString(2, article.getPasswd());
					pstmt.setString(3, article.getSubject());
					pstmt.setString(4, article.getEmail());
					pstmt.setString(5, article.getContent());
					pstmt.setInt(6, article.getNum());
					pstmt.executeUpdate();
					x = 1;
				} else { // 패스워드 잘못되면 x=0
					x = 0;
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			JDBCUtil.closeResource(rs, pstmt, conn);
		}
		return x; // 리턴값은 1(업데이트 된거) , 0 , -1
	}

	// 글 삭제
	public int deleteArticle(int num, String passwd) throws Exception {
		String dbpasswd = "";
		int x = -1;

		try {
			conn = JDBCUtil.getConnection();

			pstmt = conn.prepareStatement("select passwd from sboard where num = ?");
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dbpasswd = rs.getString("passwd");
				if (dbpasswd.equals(passwd)) {
					pstmt = conn.prepareStatement("delete from sboard where num = ?");
					pstmt.setInt(1, num);
					pstmt.executeUpdate();

					x = 1;
				} else
					x = 0;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			JDBCUtil.closeResource(rs, pstmt, conn);
		}
		return x;
	}

}
