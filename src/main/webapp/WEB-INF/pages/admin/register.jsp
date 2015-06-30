<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

<title>vRemind Admin Module - Manual Registration Form</title>

<link href="<c:url value ="/assets/css/jquery-ui.css " /> "
	rel="stylesheet" type="text/css" />
<script src="<c:url value="/assets/js/jquery.js" />"
	type="text/javascript"></script>
<script src="<c:url value="/assets/js/jquery-ui.js" />"
	type="text/javascript"></script>
<link rel="stylesheet"
	href="<c:url value ="/assets/css/admin/styles.css" />" type="text/css" />

<meta name="viewport"
	content="width=device-width, minimum-scale=1.0, maximum-scale=1.0" />
<style>
.error {
	color: #ff0000;
}

.errorblock {
	color: #000;
	background-color: #ffEEEE;
	border: 3px solid #ff0000;
	padding: 8px;
	margin: 16px;
}

.sucessblock {
	color: #330000;
	background-color: #66ccff;
	border: 3px solid #ff0000;
	padding: 8px;
	margin: 16px;
}
</style>
<script>
	$(function() {
		$("#datepicker").datepicker({
			showOn : "button",
			buttonImage : "<c:url value="/assets/images/calendar.gif" />",
			buttonImageOnly : true,
			dateFormat : "dd-mm-yy",
			buttonText : "Select date"
		});
	});
</script>
</head>

<body>
	<section id="body" class="width">

		<aside id="sidebar" class="column-left">
			<%@ include file="/WEB-INF/pages/admin/common/header.jsp"%>
			<jsp:include page="/WEB-INF/pages/admin/common/sidebar.jsp"
				flush="true">
				<jsp:param name="mid" value="1" />
			</jsp:include>
		</aside>

		<section id="content" class="column-right">
			<article>
				<h2>Manual Vaccination Registration Form&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</h2>

				<p>
					<c:if test="${errors != null}">
						<div class="errorblock">
							<c:forEach items="${errors}" var="current">
								<c:out value="${current}" />
								<br>
							</c:forEach>
						</div>
					</c:if>
					<c:if test="${sucess != null}">
						<div class="sucessblock">
							<c:out value="${sucess}" />
						</div>
					</c:if>

					<c:url var="url" value="/admin/submitregistration.do" />
					<form:form action="${url}" commandName="ManualRegistrationCommand"
						method="post">
						<table border="1">
							<tr>
								<td>Mobile Number:</td>
								<td><form:input path="mobile" /></td>
								<td><font color="red">*</font></td>
							</tr>
							<tr>
								<td>Baby Name:</td>
								<td><form:input path="babyName" /></td>
								<td><font color="red">*</font></td>
							</tr>
							<tr>
								<td>Mother Name:</td>
								<td><form:input path="motherName" /></td>
								<td><font color="red">optional</font></td>
							</tr>
							<tr>
								<td>Father Name:</td>
								<td><form:input path="fatherName" /></td>
								<td><font color="red">optional</font></td>
							</tr>
							<tr>
								<td>Date of Birth:</td>
								<td><form:input path="dob" id="datepicker" /></td>
								<td><font color="red">* (dd-MM-yyyy)</font></td>
							</tr>
							<tr>
								<td>Email:</td>
								<td><form:input path="email" /></td>
								<td><font color="red">optional</font></td>
							</tr>
							<tr>
								<td>Operator Circle:</td>
								<td><form:input path="circle" /></td>
								<td><font color="red">optional</font></td>
							</tr>
							<tr>
								<td>Operator Name:</td>
								<td><form:input path="operatorName" /></td>
								<td><font color="red">optional</font></td>
							</tr>
							<tr>
								<td>Date & Time of Registration:</td>
								<td><form:input path="dateTime" /></td>
								<td><font color="red">optional (dd-MM-yyyy HH:mm:ss
										a)</font></td>
							</tr>
							<tr>
								<td colspan="3" align="center"><input type="reset"
									value="Reset" class="button">&nbsp;&nbsp;&nbsp; <input type="submit"
									value="Submit" class="button"/></td>
							</tr>
						</table>
					</form:form>
				</p>

			</article>
			<%@ include file="/WEB-INF/pages/admin/common/footer.jsp"%>
		</section>
		<div class="clear"></div>
	</section>

</body>
</html>

