<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>${systemOption.systemTitle}</title>
<script type="text/javascript" charset="UTF-8">  
    $(function(){  
        //触发的下拉框chang事件  
        $("#tclx").change(function(){  
            var tclx = $("#tclx").val();  
            $.ajax({  
                type:"POST",  
                url :"${ctx}/list/listType",  
                data:{                    
                    id:tclx  
                },  
                dataType:"json",  
                success:function(data){  
                    $("#tid").empty();  
                    $("#tid").append("<option value=''>----请选择----</option>");  
                    $.each(data,function(index,item){  
                        console.info("item:"+item.id);  
                        //填充内容  
                        $("#tid").append( "<option value='"+item.id+"'>"+item.text+"</option>");  
                    });  
                }  
            });  
        });  
          
          
        $("#tid").change(function(){              
            var tid = $("#tid").val();              
            $.ajax({  
                type:"POST",  
                url :"${ctx}/tdictypecon/listDictypeAllld",  
                data:{                    
                    tid:tid  
                },  
                dataType:"json",  
                success:function(data){  
                    $("#zdz").empty();  
                    $("#zdz").append("<option value=''>----请选择----</option>");  
                    console.info(data);  
                    $.each(data,function(index,item){  
                        $("#zdz").append( "<option value='"+item.id+"'>"+item.text+"</option>");  
                    });  
                }  
            });  
        });  
          
    });  
      
      
</script>
</head>

<body>
	<table border="1">
		<tr>
			<td>触发控件</td>
			<td colspan="2"><select id="tclx" style="width: 100px;">
					<option value="">请选择</option>
					<option value="7">111</option>
					<option value="7">222</option>
			</select></td>
		</tr>
		<tr>
			<td>一级</td>
			<td><select id="tid" style="width: 100px;"></select></td>
			<td>二级</td>
			<td><select id="zdz" style="width: 100px;"></select></td>
		</tr>
	</table>

</body>
</html>
