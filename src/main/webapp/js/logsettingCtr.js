$.fn.logSettingInitPage = function(menuId){
	$(MAINDIV).load(MENU_PAGES[menuId] + SPACE + TABLE_DIV,function(){
		//refreshTbody1(menuId,PAGES_CONTENT,0,10);
		var vos = PAGES_CONTENT.vos;
		var bean = vos[0];
		//$("#filepath").attr("disabled","disabled");
		$("#filepath").val(bean.logPath);
		$("#debuglevel").val(bean.debugLevel);

		/*
		$("tbody input:checkbox").bind("click",function(){
			var ischecked = $(this).attr("checked");
			$("tbody input:checkbox").each(function(){
				$(this).attr("checked",false);
			});
			$(this).attr("checked",ischecked);
		});
		*/
		$("#saveLogSetting").bind("click",function(){
			
			var filepath = $.trim($("#filepath").val());
			var debuglevel = $.trim($("#debuglevel").val());
			if(filepath == ""){
				alert("日志存储位置不能为空！");
				return false;
			}
			$.ajax({
				type : "POST",
				async : false,
				url : SERVER_CTX + "/sysconf/saveLogSetting",
				data : {
					"filepath" : filepath,
					"debuglevel" : debuglevel
				},
				success : function(response, status, jqXHR){
					alert("配置成功");
				},
				error : function(XMLHttpRequest, textStatus, errorThrown){
					alert("操作失败！");
				}
			});
		});		
		
	});
}