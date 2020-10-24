<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2019/11/12 0012
  Time: 11:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ceshi</title>
    <script src="js/jquery-1.7.2.min.js"></script>
    <script>
        $(function () {
           $("#btn").click(function () {
               alert("hello btn");
           });
        });
    </script>
</head>
<body>
历史年化利率${historyAverageRate}
总人数${allUserCount}
<h2>累计投资金额${allBidMoney}</h2>
<button id="btn">发送请求</button>

</body>
</html>
