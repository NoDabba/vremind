<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page session="false" %>
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="lt-ie9 lt-ie8 lt-ie7" lang="en"> <![endif]-->
<!--[if IE 7]> <html class="lt-ie9 lt-ie8" lang="en"> <![endif]-->
<!--[if IE 8]> <html class="lt-ie9" lang="en"> <![endif]-->
<!--[if gt IE 8]><!-->
<html lang="en">
<!--<![endif]-->
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>Partner Login Form</title>
<link href="<c:url value ="/assets/css/style.css" />" rel="stylesheet"
	type="text/css" />

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
.header {
	background: #ccccff;
	text-align: justify;
	/* IE special */
	width: 100%;
	-ms-text-justify: distribute-all-lines;
	text-justify: distribute-all-lines;
}

.header:after {
	content: '';
	display: inline-block;
	width: 100%;
	height: 0;
	font-size: 0;
	line-height: 0;
}

s1 {
	display: inline-block;
	margin-top: 0.321em;
	/* ie 7*/
	*display: inline;
	*zoom: 1;
	*text-align: left;
}

s2 {
	display: inline-block;
	margin-top: 0.321em;
	/* ie 7*/
	*display: inline;
	*zoom: 1;
	*text-align: left;
}

.nav {
	display: inline-block;
	vertical-align: baseline;
	/* ie 7*/
	*display: inline;
	*zoom: 1;
	*text-align: right;
}
</style>

</head>
<body>
	<div class="header">
		<img class="s1" src="http://www.vremind.org/images/logo.png"
			alt="vRemind" height="80px" />
		<div class="nav">
		</div>
	</div>
	
	<br>
	<c:if test="${errors != null}">
		<div class="errorblock">
			<c:forEach items="${errors}" var="current">
				<c:out value="${current}" />
				<br>
			</c:forEach>
		</div>
	</c:if>

	<section class="container">
		<div class="login">
			<h1>vRemind Partner Login Module</h1>
			<form method="post"
				action="<c:url value="/partner/${partnerId}/submitsignon.do" />">
				<p>
					<input type="text" name="userId" value="" placeholder="Username">
				</p>
				<p>
					<input type="password" name="password" value=""
						placeholder="Password">
				</p>
				<p class="submit">
					<input type="submit" name="commit" value="Login">
				</p>
			</form>
		</div>

		<div class="login-help">
			<p>
				Forgot your password? <a href="#">Click here to reset it</a>.
			</p>
		</div>
	</section>

</body>
</html>
