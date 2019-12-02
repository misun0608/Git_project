<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.spring.springboard.BoardVO"%>
<%@ page import="com.spring.springboard.BoardDAO"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ include file="color.jsp"%>
<html>
<head>
<title>Insert title here</title>
</head>

<%
	int num = Integer.parseInt(request.getParameter("num"));
	String pageNum = request.getParameter("pageNum");
	int number = Integer.parseInt(request.getParameter("number"));

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	try {
		BoardDAO dbPro = new BoardDAO();
		BoardVO article = dbPro.getArticle(num);

		int ref = article.getRef(); // 왜 이 세개를 따로? 상세보기할때 댓글쓸때 필요하기때문?
		int re_step = article.getRe_step();
		int re_level = article.getRe_level();
%>

<body bgcolor="<%=bodyback_c%>">
	<center>
		<b> 글 내용 보기 </b><br>

		<form>
			<table width="555" border="1" cellspacing="0" cellpadding="0"
				bgcolor="<%=bodyback_c%>" align="center">
				<tr height="30" align="center" width="125">
					<td bgcolor="<%=value_c%>">글 번호</td>
					<td><%=number%></td>
					<td bgcolor="<%=value_c%>">조회수</td>
					<td width="180"><%=article.getReadcount()%></td>
				</tr>
				<tr height="30" align="center" width="125">
					<td bgcolor="<%=value_c%>">작성자</td>
					<td><%=article.getWriter()%></td>
					<td bgcolor="<%=value_c%>">작성일</td>
					<td width="180"><%=sdf.format(article.getReg_date())%></td>
				</tr>
				<tr height="30">
					<td align="center" width="125" bgcolor="<%=value_c%>">글제목</td>
					<td align="left" colspan="3">&nbsp;<%=article.getSubject()%>
					</td>
				</tr>
				<tr>
					<td align="center" width="125" bgcolor="<%=value_c%>">글내용</td>
					<td align="left" colspan="3"><pre><%=article.getContent()%></pre>
					</td>
				</tr>
				<tr height="30">
					<td colspan="4" bgcolor="<%=value_c%>" align="center"><input
						type="button" value="글수정"
						onclick="document.location.href='updateform.bo?num=<%=article.getNum()%>&pageNum=<%=pageNum%>'">
						<!-- 왜 파라미터를 각각 다르게 전달하는지 이해해야함 --> &nbsp;&nbsp; <input
						type="button" value="글삭제"
						onclick="document.location.href='deleteform.bo?num=<%=article.getNum()%>&pageNum=<%=pageNum%>'">
						&nbsp;&nbsp; <input type="button" value="답글쓰기"
						onclick="document.location.href='writeform.bo?num=<%=num%>&ref=<%=ref%>&re_step=<%=re_step%>&re_level=<%=re_level%>'">
						&nbsp;&nbsp; <input type="button" value="글목록"
						onclick="document.location.href='list.bo?pageNum=<%=pageNum%>'">
					</td>
				</tr>
			</table>
		</form>
	</center>

</body>

<%
	} catch (Exception ex) {
	}
%>
</html>