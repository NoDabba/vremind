<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<nav id="mainnav">
	<ul>
		<li ${param.mid == 1 ? 'class="selected-item"' : ''} ><a href="<c:url value="/admin/register.do" />">Registration</a></li>
		<li ${param.mid == 2 ? 'class="selected-item"' : ''} ><a href="<c:url value="/admin/register/bulkupload.do" />">Registration File Upload</a></li>
		<li ${param.mid == 3 ? 'class="selected-item"' : ''} ><a href="#">Reports</a></li>
		<li><a href="<c:url value="/admin/signoff.do" />">Sign Out</a></li>
	</ul>
</nav>