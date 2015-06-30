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
				<jsp:param name="mid" value="2" />
			</jsp:include>
		</aside>

		<section id="content" class="column-right">
			<article>
				<h2>Manual Vaccination Registration Bulk Upload Form</h2>

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

					<c:url var="url" value="/admin/register/bulkupload.do" />
					<form method="POST" action="${url}" enctype="multipart/form-data">
        				File to upload (CSV format): <input type="file" name="file"><br /><br />
        				Hospital Key: <input type="text" name="hospitalKey"><br /> <br /> <br />
        				<input type="submit" value="Upload" class="button"> Press here to upload the file!
    				</form>
    				<div class="clear"></div>
    				<div class="clear"></div>
    				<div class="clear"></div>
    				<div class="clear"></div>
				</p>

			</article>
			<%@ include file="/WEB-INF/pages/admin/common/footer.jsp"%>
		</section>
		<div class="clear"></div>
	</section>

</body>
</html>

