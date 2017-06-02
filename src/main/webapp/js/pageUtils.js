/**
 * page util values
 */
var MENU_ROOT_CODE = "0";
var ACTIVE_CLASS = 'active';
var BUTTON_DISABLED = 'disabled';
var MAINDIV = "#main";
var MAINNAV = "#mainNav";
var SIDEBAR = "#sideBar";
var ALL_MENUS;
var PAGES_DIR = "pages/";
var SPACE = ' ';
var SELECT_ALL_CHECKBOX = '<input type="checkbox">';

var TABLE_DIV = '#dataTableDiv';
var EDIT_DIV = '#editDiv';
var DETAIL_DIV = '#detailDiv';
var SEARCH_DIV = "#searchDiv";
var EDIT_MSG_DIV = '#editMsgDiv';

var FIRST_BTN = "#firstBtn";
var PREV_BTN = "#prevBtn";
var NEXT_BTN = "#nextBtn";
var LAST_BTN = "#lastBtn";

var CSS_STYLE = "style";
var CURSOR_DEFAULT = "cursor:default";
var CURSOR_POINTER = "cursor:pointer";

var TIME;

var MENU_PAGES = new Object();
MENU_PAGES["002000"] = PAGES_DIR + "databasesetting.jsp";
MENU_PAGES["002001"] = PAGES_DIR + "logsetting.jsp";

var PAGES_CONTENT;
var SERVER_CTX;
var FLAG_OTHERS = "2";
var FLAG_TRUE = "1";
var FLAG_FALSE = "0"; 

var PAGES_APPINFO;
var PAGES_DEVICEINFO;

var csrf_token="";
var ma_monitor = true;


String.prototype.replaceAll = function(s1,s2) { 
    return this.replace(new RegExp(s1,"gm"),s2); 
}
String.prototype.checkNOTNULL = function(){
	if(this == null || this == ""){
		return false;
	}else{
		return true;
	}
}
String.prototype.transJSON = function(){
		return eval("("+this+")");
}

String.prototype.checkEmail = function(){
	var re= /\w@\w*\.\w/;
	if(re.test(this)){
		return true;
	}else {
		return false;
	}
}


Array.prototype.indexOf = function (val) {
	for (var i = 0; i < this.length; i++) {
    	if (this[i] == val) {
        	return i;
        }
	}
	return -1;
}; 

Array.prototype.remove = function (val) {
	if (isNaN(val)) {
		this.remove(this.indexOf(val));
	} else if (val < 0 || val > this.length) {
		return false;
	} else {
		this.splice(val, 1);
	}
};

function doAjax(menuId,node,menuUrl){
	var appid = $("#appidCategroy").val();
	var id = $(node).attr("id");
	var currentPage = $(".em-pages").children("a.active").html();
	var limit = 10;
	var total = ($(".em-pages").children("a").length) * limit;
	var searchStr = $.trim($("#searchInput").val());
	var start = "0";
	var curPage = "1";
	switch (id) {
	case "prevBtn":
		start = (parseInt(currentPage)-2)*limit;
		curPage = parseInt(currentPage)-1;
		if(currentPage<=1){
			alert("已是第一页！");
			return;
		}
		break;
	case "nextBtn":
		curPage = parseInt(currentPage)+1;
		start = parseInt(currentPage)*parseInt(limit);
		if(parseInt(currentPage)*parseInt(limit)>=parseInt(total)){
			alert("已至最后一页！");
			return;
		}
		break;
	case "searchBtn":
		$("#rowNumSel option:nth-child(1)").attr("selected" , "selected");  
		limit = "10";
		if(searchStr == ""){
			return false;
		}
		break;
	default:
		curPage = $(node).html();
		start = (curPage - 1) * limit;
		break;
	}
	$.ajax({
		type : "POST",
		async : false,
		url : SERVER_CTX + menuUrl ,
		data : {
			"searchStr" : searchStr,
			"appid" : appid
		},
		success : function(response,status,jqXHR){
			var list = response;
			refreshTbody(menuId,list,start,limit);
		}
	});
}

function refreshTbody(menuId,list,start,limit){
	var dataTbodyHtml = "";
	for ( var i = start;i<list.length;i++) {
		if((i-start+1)>limit){
			break;
		}
		var bean = list[i];
		switch (menuId) {
		case "003005":
		case "003002":
			var trHtml = "<tr id=" + bean.deviceid + " >" + "<td>"
			+ SELECT_ALL_CHECKBOX + "</td>" 
			+ "<td title=" + bean.name + ">" + bean.name
			+ "</td>" + "<td title=" + bean.hostname + ">" + bean.hostname
			+ "</td>"; 
			if(bean.style == "android"){
				trHtml = trHtml + "<td title='安卓'>"
				+ "安卓" + "</td>" ;
			}else {
				trHtml = trHtml + "<td title='苹果'>"
				+ "苹果" + "</td>" ;
			}		
			if(bean.categroy == "table"){
				trHtml = trHtml + "<td title='平板'>"
				+ "平板" + "</td>" ;
			}else {
				trHtml = trHtml + "<td title='手机'>"
				+ "手机" + "</td>" ;
			}		
			trHtml = trHtml + "<td title="
				+ bean.deviceid + ">" + bean.deviceid + "</td>"
				+ "<td title=" + bean.mac + ">"
				+ bean.mac + "</td>" + "</td>"
				+ "<td title=" + bean.remark + ">"
				+ bean.remark + "</td>" ;
			if(bean.flag == "0"){
				trHtml = trHtml + "<td class='flagClass' title='关闭'>" + "关闭" + "</td>" + "</tr>";
			}else{
				trHtml = trHtml + "<td class='flagClass' title='启用'>" + "启用" + "</td>" + "</tr>";
			}
			
			dataTbodyHtml = dataTbodyHtml + trHtml;
			dataTbodyHtml = dataTbodyHtml.replaceAll("null", "");
			break;
		case "003003":
			var trHtml = "<tr id=" + bean.deviceid + " >" + "<td>"
			+ SELECT_ALL_CHECKBOX + "</td>" 
			+ "<td title=" + bean.name + ">" + bean.name
			+ "</td>" + "<td title=" + bean.hostname + ">" + bean.hostname
			+ "</td>"; 
			if(bean.style == "android"){
				trHtml = trHtml + "<td title='安卓'>"
				+ "安卓" + "</td>" ;
			}else {
				trHtml = trHtml + "<td title='苹果'>"
				+ "苹果" + "</td>" ;
			}		
			if(bean.categroy == "table"){
				trHtml = trHtml + "<td title='平板'>"
				+ "平板" + "</td>" ;
			}else {
				trHtml = trHtml + "<td title='手机'>"
				+ "手机" + "</td>" ;
			}		
			trHtml = trHtml + "<td title="
				+ bean.deviceid + ">" + bean.deviceid + "</td>"
				+ "<td title=" + bean.mac + ">"
				+ bean.mac + "</td>" + "</td>"
				+ "<td title=" + bean.remark + ">"
				+ bean.remark + "</td>" ;
			if(bean.flag == "0"){
				trHtml = trHtml + "<td class='flagClass' title='关闭'>" + "关闭" + "</td>" + "</tr>";
			}else{
				trHtml = trHtml + "<td class='flagClass' title='启用'>" + "启用" + "</td>" + "</tr>";
			}
			dataTbodyHtml = dataTbodyHtml + trHtml;
			dataTbodyHtml = dataTbodyHtml.replaceAll("null", "");
			break;
		case "003004":
			var trHtml = "<tr id=" + bean.id + " >" + "<td>"
			+ SELECT_ALL_CHECKBOX + "</td>" + "<td title="
			+ bean.deviceid + ">" + bean.deviceid + "</td>"
			+ "<td title=" + bean.name + ">" + bean.name
			+ "</td>" + "<td title=" + bean.hostname + ">" + bean.hostname
			+ "</td>"; 
			if(bean.style == "android"){
				trHtml = trHtml + "<td title='安卓'>"
				+ "安卓" + "</td>" ;
			}else {
				trHtml = trHtml + "<td title='苹果'>"
				+ "苹果" + "</td>" ;
			}		
			if(bean.categroy == "table"){
				trHtml = trHtml + "<td title='平板'>"
				+ "平板" + "</td>" ;
			}else {
				trHtml = trHtml + "<td title='手机'>"
				+ "手机" + "</td>" ;
			}		
			trHtml = trHtml + "<td title="
				+ bean.deviceid + ">" + bean.deviceid + "</td>"
				+ "<td title=" + bean.mac + ">"
				+ bean.mac + "</td>" + "</td>"
				+ "<td title=" + bean.remark + ">"
				+ bean.remark + "</td>" ;
			dataTbodyHtml = dataTbodyHtml + trHtml;
			dataTbodyHtml = dataTbodyHtml.replaceAll("null", "");
			break;
		case "001002":
			var trHtml = "<tr id=" + bean.pk_corp + " >" + "<td>"
				+ SELECT_ALL_CHECKBOX + "</td>" + "<td title="
				+ bean.code + ">" + bean.code + "</td>"
				+ "<td title=" + bean.name + ">" + bean.name
				+ "</td>" + "<td title=" + bean.flag + ">"
				+ ((bean.flag == "1") ? "正常" : "关闭") + "</td>" + "<td title=" + bean.description
				+ ">" + bean.description + "</td>" + "<td title="
				+ bean.user_id + ">" + bean.user_id + "</td>"
				+ "<td title=" + bean.user_name + ">"
				+ bean.user_name + "</td>" + "</tr>";
			dataTbodyHtml = dataTbodyHtml + trHtml;
			dataTbodyHtml = dataTbodyHtml.replaceAll("null", "");
			break;
		default:
			break;
		}
		
	}
	$("tbody").html("");
	$("tbody").append(dataTbodyHtml);
	var pageStr = "";
	var pages = (list.length + limit - 1)/ limit;
	var currentPage = start / limit + 1;
	for(var i=1;i<=pages;i++){
		if(i == currentPage){
			pageStr += "<a href='#' class='btn btn-xs active' id='" + i + "'>"+ i +"</a>";
		} else {
			pageStr += "<a href='#' class='btn btn-xs' id='" + i + "'>"+ i +"</a>";
		}
	}
	$(".em-pages").html(pageStr);
	$(".as-pagination").find("a").unbind("click");
	$(".as-pagination").find("a").bind("click",function(){
		doAjax(menuId,this,getMenuUrl(menuId));
	});
}
function getMenuName(menuId){
	for(i in ALL_MENUS){
		var menu = ALL_MENUS[i];
		if(menuId == menu.code){
			return menu.name;
		}
	}

}

function fixPass(isuser){
	var oldPass = $.trim($("#oldPass").val());
	var newPass = $.trim($("#newPass").val());
	var rePass = $.trim($("#rePass").val());
	if(oldPass == "" || newPass == "" || rePass == ""){
		$(".message").message("原密码、新密码和确认密码不能为空！");
		return false;
	}
	
	if(!newPass.checkPwd()){
		$(".message").message("密码必须包含字母和数字,且至少为8位！");					
		return;
	}
	
	if(newPass != rePass){
		$(".message").message("新密码和确认密码不一致！");
		return false;
	}
	$.ajax({
		type : "POST",
		async : false,
		url : (SERVER_CTX?SERVER_CTX:"/mobem") + "/system/fixPass",
		data : {
			"oldPass" : oldPass,
			"newPass" : newPass
		},
		success : function(response, status, jqXHR){
			var i = 3;
			var json = response.transJSON();
			var msg = json.msg
			var flag = json.flag;
			if(flag == "1"){
				$("#fix").addClass("disabled");
				$("#ret").addClass("disabled");
				$(".message").success(msg + "," + i + "秒后跳转至登录页面……");				
				show(msg,i,isuser);
			} else {
				$(".message").message(msg + "," + i + "秒后跳转至登录页面……");				
				show(msg,i,isuser);				
			}
		},
		error : function(XMLHttpRequest, textStatus, errorThrown){
			$(".message").message("操作失败！");
		}
	});
}

function show(msg,i,isuser){
    i-=1 ;
    //$(".message").message(msg + "," + i + "秒后跳转至登录页面……");
	//var div = $(".message").children("div")[0];
	//$(div).removeClass("alert-danger");
	//$(div).addClass("alert-success");
    if(i == 0)
    {
		//window.location.href='init.jsp';
		//window.location.href = "${ctx}/login/logout";
    	if(isuser=='bzuser'){  		
    		window.location.href = "/mobem/login/bzuserlogout";
    	}
    	else
    		window.location.href = "/mobem/login/logout";
    }
    window.setTimeout("show('"+ msg +"',"+ i +",'"+isuser+"')",1000);           
}     
/*
$.fn.message = function(message) {
	 if (this.find("span").length) {
	  this.find("span").text(message);
	 } else {
	  this.html("<div class=\"alert alert-danger alert-dismissable\">" +
	    "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button>" +
	    "<strong>注意：</strong><span>" + message + "</span>" +
	   "</div>");
	 }
	 return this;
}
*/
var handNull = function(str){
	return str || (str == "0" ? "0" : "");
}

var delConfirm = function(content){
	$("#delConfrim h6").html(content);
	$("#delConfrim").modal({backdrop:true});
	$("#delConfrim #delConfirmBtn").unbind("click");
}

var confirmModal = function(content, title){
	$("#delConfrim h6").html(content);
	$("#delConfrim h4").html(title);
	$("#delConfrim").modal({backdrop:true});
	$("#delConfrim #delConfirmBtn").unbind("click");
}

var ztreesynccfg = {
		async: {
			enable: true,
			url: function(treeId, treeNode) {
				return "../org/asynctree?parentid="+(treeNode?treeNode.id:"xxxxxxxxxx");
			}
		},				
		callback: {
			beforeExpand: function(treeId, treeNode) {
				if (!treeNode.isAjaxing) {
					var zTree = $.fn.zTree.getZTreeObj(treeId);
					zTree.reAsyncChildNodes(treeNode, "refresh", true);
					return true;
				} else {
						alert("zTree 正在下载数据中，请稍后展开节点。。。");
						return false;
					}
			},	
			onAsyncSuccess: function(event, treeId, treeNode, msg) {
				if (!msg || msg.length == 0) {
					return;
				}
				var zTree = $.fn.zTree.getZTreeObj(treeId),
				totalCount = treeNode.count;
				if (treeNode.children.length = 0) {
					return;
				} else {
					zTree.updateNode(treeNode);
					zTree.selectNode(treeNode.children[0]);
				}
			}
		}			
	};

var ztreedefaultsetting = {	
		data: {
			simpleData: {
			},
			key: {
				name: "name"
			}
		},
		callback: {			
		},
		view: {
			selectedMulti: false,
			showLine:true
		}			
	};	