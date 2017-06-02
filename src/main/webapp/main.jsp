<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<%@ include file="/include/scriptlibs.jsp"%>
<!-- page script -->
<script>
function showData() {  
	alert("show data");
    $('#tableList').bootstrapTable('refresh');  
}  
  
$('#tableList').bootstrapTable({  
    columns: [{  
        field: 'id',  
        title: 'SEQ',  
    }, {  
        field: 'year',  
        title: 'YEAR',  
    }, {  
        field: 'month',  
        title: 'MONTH',  
    },{  
        field: 'creDate',  
        title: 'DATE',  
    },{  
        field: 'merBasicId',  
        title: 'Product ID',  
    },{  
        field: 'merName',  
        title: 'Product Name',  
    },{  
        field: 'categoryTypeName',  
        title: 'Product Type',  
    },{  
        field: 'city',  
        title: 'CITY',  
    },{  
        field: 'area',  
        title: 'AREA',  
    },{  
        field: 'tradeAreaName',  
        title: 'Trade Area',  
    }],//页面需要展示的列，后端交互对象的属性  
    pagination: true,  //开启分页  
    sidePagination: 'server',//服务器端分页  
    pageNumber: 1,//默认加载页  
    pageSize: 20,//每页数据  
    pageList: [20, 50, 100, 500],//可选的每页数据  
    queryParams: function (params) {  
        return {  
        startDate: $("#txtStartDate").val(),  
        endDate: $("#txtEndDate").val(),  
        merName: $("#txtMerName").val(),  
            pageSize: params.limit,  
            offset: params.offset  
        }  
    },//请求服务器数据时的参数  
    url: '<%=request.getContextPath()%>/data/list' //服务器数据的加载地址  
}); 
</script>
</head>
<body>
	<br>
	<form action="myTest/aaa" method="post">
		MSG:<input type="text" name="msg" value="" /> <input type="submit"
			value="Submit" />
	</form>
	<br>
	<br>
	<br>
	
	<br>
	<div class="row">
		<table class="table table-hover">
			<thead>
				<tr>
					<th></th>
					<th>变量索引</th>
					<th>变量名</th>
					<th>当前值</th>
					<th>最小量程</th>
					<th>最大量程</th>
					<th>告警下限</th>
					<th>告警上限</th>
				</tr>
			</thead>
			<tbody id="example">
				<c:forEach items="${list}" var="food" varStatus="status">
					<tr>
						<td></td>
						<td>${food.varIndex}</td>
						<td>${food.name}</td>
						<td>${food.value}</td>
						<td>${food.minValue}</td>
						<td>${food.maxValue}</td>
						<td>${food.warnLowLimit}</td>
						<td>${food.warnHighLimit}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<div align="center">
		<div id="page"></div>
	</div>
	<script type='text/javascript'>  
                    //根据第一次传递进来的值初始化分页插件  
                    
                    var options = {
                    	bootstrapMajorVersion: 2,//版本
                   		currentPage: ${page.pageNow},  
                        totalPages: ${page.totalPageCount},  
                        numberOfPages:10,  
                        alignment:'center',
                        itemTexts: function (type, page, current) {  
                              switch (type) {  
                                case "first":  
                                  return "首页";  
                                case "prev":  
                                  return "上一页";  
                                case "next":  
                                  return "下一页";  
                                case "last":  
                                  return "末页";  
                                case "page":  
                                  return page;  
                              }  
                       }, 
                      //事件响应  
                     onPageClicked: function (event, originalEvent, type, page) {  
                            //异步访问数据  
                            $.ajax({  
                              url: "<%=request.getContextPath()%>/list?pn="+page,  
                              type: "post",  
                              date: "",  
                              success: function (data1) {  
                                  $("#example").empty();  
                                  page = data1.page;  
                                  list = data1.list;  
                                 if (list != null) {  
                                    $.each( list, function(index, food){    
                                        var html = '<tr>'+//  
                                        	'<td></td>' +
                                           '<td>'+food.varIndex+'</td>'+//  
                                           '<td>'+food.name+'</td>'+//  
                                           '<td>'+food.value+'</td>'+//  
                                           '<td>'+food.minValue+'</td>'+//  
                                           '<td>'+food.maxValue+'</td>'+//  
                                           '<td>'+food.warnLowLimit+'</td>'+//
                                           '<td>'+food.warnHighLimit+'</td>'+//
                                           '</tr>';  
                                            $("#example").append(html);   
                                        });  
                                    }else{  
                                        $("example").append('没有找到相应数据');  
                                    }  
                                }  
                            });  
                          }  
                    }
                    //初始化分页插件  
                   $('#page').bootstrapPaginator(options);  
                </script>
</body>
</html>