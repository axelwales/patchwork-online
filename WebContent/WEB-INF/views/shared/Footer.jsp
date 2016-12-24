<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

</div> <!-- close #content -->

<script src="<c:out value='${context}'/>/static/scripts/jquery-3.1.1.js"></script>
<script src="<c:out value='${context}'/>/static/scripts/jquery-ui.js"></script>
<script src="<c:out value='${context}'/>/static/scripts/bootstrap.js"></script>
<script src="<c:out value='${context}'/>/static/scripts/paper-full.js"></script>
<c:if test="${ includeTree }" >
<script type="text/paperscript" src="<c:out value='${context}'/>/static/scripts/treebuilder.js" canvas="tree-canvas"></script>
</c:if>
<c:forEach items="${requestScope.jsFiles}" var="file">
	<script src="<c:out value='${context}'/>/static/scripts/<c:out value='${file}'/>.js"></script>
</c:forEach>

</body>
</html>