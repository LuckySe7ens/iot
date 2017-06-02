$.fn.dataSettingInitPage = function(menuId){
	$(MAINDIV).load(MENU_PAGES[menuId] + SPACE + TABLE_DIV,function(){

		var vos = PAGES_CONTENT.vos;
		var bean = vos[0];

		var dbty = bean.dbtype;
		if(dbty=="ORACLE"){
			$("#dbtype").find("option[value='oracle']").attr("selected",true);   
		}
		if(dbty=="MYSQL"){
			$("#dbtype").find("option[value='mysql']").attr("selected",true);   
		}
		$("#checking").hide();
		$("#dbip").val(bean.ip);
		$("#dbnet").val(bean.net);
		$("#dbname").val(bean.dbname);
		$("#name").val(bean.username);
		$("#pwd").val(bean.password);

	
		$("#checkDBCon").bind("click",function(){
			$("#checkDBCon").hide();
			$("#checking").show();
			var dbtype = $("#dbtype").val();
			var ip = $.trim($("#dbip").val());
			var net = $.trim($("#dbnet").val());
			var dbname=  $.trim($("#dbname").val());
			var name = $.trim($("#name").val());
			var pwd = $.trim($("#pwd").val());
			if(ip == ""){
				alert("数据库地址不能为空！");
				return false;
			}
			if(net == ""){
				alert("数据库端口号不能为空！");
				return false;
			}
			if(dbname == ""){
				alert("数据库/ODBC不能为空！");
				return false;
			}
			if(name == ""){
				alert("用户名不能为空！");
				return false;
			}
			//$("#checkDBCon").attr({"disabled":"disabled"});
			$.ajax({
				type : "POST",
				async : true,
				url : SERVER_CTX + "/sysconf/checkDbCon",
				data : {
					"dbType":dbtype,
					"ip":ip,
					"net":net,
					"dbname":dbname,
					"user":name,
					"pwd":pwd
				},
				
				success : function(response, status, jqXHR){
					$("#checking").hide();
					$("#checkDBCon").show();
					if(response.success){
						alert("测试通过");
					}else{
						alert(response.message);
					}
				},
				error : function(XMLHttpRequest, textStatus, errorThrown){
					$("#checking").hide();
					$("#checkDBCon").show();
					alert("测试未通过，请检查输入！");
				}
			});
		});	
		$("#save").bind("click",function(){
			
			
			var dbtype = $("#dbtype").val();
			var ip = $.trim($("#dbip").val());
			var net = $.trim($("#dbnet").val());
			var dbname=  $.trim($("#dbname").val());
			var name = $.trim($("#name").val());
			var pwd = $.trim($("#pwd").val());
			
			if(dbtype == ""){
				alert("请选择一个数据库类型！");
				return false;
			}
			if(ip == ""){
				alert("数据库地址不能为空！");
				return false;
			}
			if(net == ""){
				alert("数据库端口号不能为空！");
				return false;
			}
			if(dbname == ""){
				alert("数据库/ODBC不能为空！");
				return false;
			}
			if(name == ""){
				alert("用户名不能为空！");
				return false;
			}
			$.ajax({
				type : "POST",
				async : false,
				url : SERVER_CTX + "/sysconf/saveDbSetting",
				data : {
					"dbType":dbtype,
					"ip":ip,
					"net":net,
					"dbname":dbname,
					"user":name,
					"pwd":pwd
				},
				success : function(response, status, jqXHR){
					alert("保存成功！");
				},
				error : function(XMLHttpRequest, textStatus, errorThrown){
					alert("操作失败！");
				}
			});
		});		
		
	});
}