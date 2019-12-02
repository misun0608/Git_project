<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.spring.springboard.BoardVO"%>
<%@ page import="com.spring.springboard.BoardDAO"%>
    <%@ include file ="color.jsp" %>
<!DOCTYPE html>
<html>
<head>
<title>게시판</title>
</head>

<%
	int num = Integer.parseInt(request.getParameter("num"));	// 수정하기를 눌렀을떄 num, pageNum 전달됨
	String pageNum = request.getParameter("pageNum");
	
	try{
		BoardDAO dbPro = new BoardDAO();
		BoardVO article = dbPro.updateGetArticle(num);
%>
<body bgcolor="<%=bodyback_c%>">
	<center><b>글수정</b></center>
		<form method="post" name="writeform" action="updateprocess.bo?pageNum=<%=pageNum%>">	<!-- article객체에는 pageNum이 없기때문에 -->
			<table width="400" border="1" cellspacing="0" cellpadding="0" bgcolor="<%=bodyback_c%>" align="center">
				<tr>
					<td width="70" bgcolor="<%=value_c%>" align="center">
						이름
					</td>
					<td width="330" align="left">
						<input type="text" size="10" maxlength="10" name="writer" value="<%=article.getWriter()%>">
						<input type="hidden" name="num" value="<%=article.getNum()%>">
					</td>
				</tr>
				<tr>
					<td width="70" bgcolor="<%=value_c%>" align="center"> 제 목 </td>
					<td width="330" align="left">
						<input type="text" size="40" maxlength="50" name="subject" value="<%=article.getSubject()%>">
					</td>
				</tr>
				<tr>
					<td width="70" bgcolor="<%=value_c%>" align="center"> Email </td>
					<td width="330" align="left">
						<input type="text" size="40" maxlength="30" name="email" value="<%=article.getEmail()%>">
					</td>
				<tr>
					<td width="70" bgcolor="<%=value_c%>" align="center"> 내 용 </td>
					<td width="330" align="left">
						<textarea name="content" rows="13" cols="40"><%=article.getContent()%></textarea>
					</td>
				</tr>
				<tr>
					<td width="70" bgcolor="<%=value_c%>" align="center"> 비밀번호 </td>
					<td width="330" align="left">
						<input type="password" size="8" maxlength="12" name="passwd">
					</td>
				</tr>
				<tr>
					<td colspan="2" bgcolor="<%=value_c%>" align="center">
						<input type="submit" value="글수정">
						<input type="reset" value="다시작성">
						<input type="button" value="목록보기" onclick="document.location.href='list.bo?pageNum=<%=pageNum%>'">
					</td>
				</tr>
			</table>
		</form>
<%
	}catch(Exception e) {}
%>
</body>
</html>