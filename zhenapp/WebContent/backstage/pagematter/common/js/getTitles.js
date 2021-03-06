var myDate = new Date();
	var hour = myDate.getHours();
	var llmax = 1000;//最大流量数
	var gwcmax = 0;//最大购物车数
	var scmax = 0;//最大收藏数量
	var keywords=1;//关键词数量
	var days=1;//天数
	var subtractll = 0;//统计消耗积分时要去掉的当天不发布的流量数
	
	;$(function() {
		$('#datefrom').datebox({
			onSelect : function(date){
				var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
				var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0" + (date.getMonth() + 1);
				var day2 = myDate.getDate() > 9 ? myDate.getDate() : "0" + myDate.getDate();
				var month2 = (myDate.getMonth() + 1) > 9 ? (myDate.getMonth() + 1) : "0" + (myDate.getMonth() + 1);
				var datevalue1 = date.getFullYear() + "" + month + "" +day;
				var datevalue2 = myDate.getFullYear() + "" + month2 + "" +day2;
				if(datevalue1 < datevalue2){
					$.messager.alert('消息提示', '不允许回忆从前哦!', 'info', function () {
						$('#datefrom').datebox('setValue',"");
					});
				}
			},
		});
		
		$('#dateto').datebox({
			onSelect : function(date){
				var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
				var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0" + (date.getMonth() + 1);
				var datevalue1 = date.getFullYear() + "" + month + "" +day;
				var datevalue2 = $("input[name='datefrom']")[0].value;
				var datevalue3 = datevalue2.replace("-","").replace("-","");
				if(datevalue3.length <1){
					$.messager.alert('消息提示', '请先选择开始时间!', 'info', function () {
						$('#dateto').datebox('setValue',"");
					});
					return false;
				}
				if(datevalue1 < datevalue3){
					$.messager.alert('消息提示', '结束日期不允许小于开始日期!', 'info', function () {
						$('#dateto').datebox('setValue',"");
					});
				}
				days = parseInt(datevalue1)-parseInt(datevalue3)+1;
				sum();
			},
		});
		//开始日期验证
		$("input[name='datefrom']").validatebox({
			required : true,
			missingMessage : '请输入开始日期',
			invalidMessage : '开始日期不得为空',
		});
		//结束日期验证
		$("input[name='dateto']").validatebox({
			required : true,
			missingMessage : '请输入结束日期',
			invalidMessage : '结束日期不得为空',
		});
		//宝贝id验证
		$('#taskkeynum').validatebox({
			required : true,
			missingMessage : '请输入宝贝id',
			invalidMessage : '宝贝id不得为空',
		});
		//发布流量数验证
		$('#flowcount').validatebox({
			required : true,
			missingMessage : '请输入发布流量数',
			invalidMessage : '发布流量数不得为空',
		});
		//发布收藏数验证
		$('#collectioncount').validatebox({
			required : true,
			missingMessage : '请输入发布收藏数',
			invalidMessage : '发布收藏数不得为空',
		});
		//发布购物车数验证
		$('#shoppingcount').validatebox({
			required : true,
			missingMessage : '请输入发布购物车数',
			invalidMessage : '发布购物车数不得为空',
		});
		
		
		/*
		发布流量
		*/
		$("#subbtn").click(function () {
			if (!$('#taskkeynum').validatebox('isValid')) {
				$.messager.alert('消息提示', '请输入宝贝id!', 'info', function () {
					$('#taskkeynum').focus();
				});
				return false;
			}
			if (!$('#flowcount').validatebox('isValid')) {
				$.messager.alert('消息提示', '请输入发布流量数!', 'info', function () {
					$('#flowcount').focus();
				});
				return false;
			}
			if (!$('#collectioncount').validatebox('isValid')) {
				$.messager.alert('消息提示', '请输入发布收藏数!', 'info', function () {
					$('#collectioncount').focus();
				});
				return false;
			}
			if($('#collectioncount').val()>$('#flowcount').val()){
				$.messager.alert('消息提示', '发布的收藏数必须小于或等于流量数!', 'info', function () {
					$('#collectioncount').focus();
				});
				return false;
			}
			if (!$('#shoppingcount').validatebox('isValid')) {
				$.messager.alert('消息提示', '请输入发布购物车数!', 'info', function () {
					$('#taskkeynum').focus();
				});
				return false;
			}
			if($('#shoppingcount').val()>$('#flowcount').val()){
				$.messager.alert('消息提示', '发布的加购物车数必须小于或等于流量数!', 'info', function () {
					$('#shoppingcount').focus();
				});
				return false;
			}
			if($("input[name='datefrom']")[0].value.length < 1){
				$.messager.alert('消息提示', '请输入开始日期!', 'info', function () {
					$('#datefrom').focus();
				});
				return false;
			}
			if($("input[name='dateto']")[0].value.length < 1){
				$.messager.alert('消息提示', '请输入结束日期!', 'info', function () {
					$('#dateto').focus();
				});
				return false;
			}
			var taskkeywords = [];
			var inputtaskkeywords = $("input[name='taskkeywords']");
			for(var i=0;i<inputtaskkeywords.length;i++){
				if(inputtaskkeywords[i].value.length<1){
					$.messager.alert('消息提示', '关键词不允许为空', 'info');
					return false;
				}
			}
			inputtaskkeywords.each(function(){
				var me = $(this);
				taskkeywords.push(me.val());//保存value到一个数组中
			});
			var taskhourcounts = [];
			var inputtaskhourcounts = $("input[name='taskhourcounts']");
			inputtaskhourcounts.each(function(){
				var me = $(this);
				taskhourcounts.push(me.val());//保存value到一个数组中
			});
			$.ajax({
				url : uri+"/task/insertTaskInfo",
				type : "POST",
				data : {
					taskkeynum : $("#taskkeynum").val(),
					taskkeywords : taskkeywords.join('===='),
					tasktype:"33",
					taskstartdate:$("input[name='datefrom']")[0].value,
					taskenddate:$("input[name='dateto']")[0].value,
					taskhourcounts : taskhourcounts.join(','),
					taskminprice : $("#taskminprice").val(),
					taskmaxprice : $("#taskmaxprice").val(),
					tasksearchtype : $("input[name='tasksearchType']:checked").val(),
					flowcount : $("#flowcount").val(),
					collectioncount : $("#collectioncount").val(),
					shoppingcount : $("#shoppingcount").val(),
					subtractpoints : $("#sum").text(),
				},
				success:function(data,state){
					if(data.data=="insertsuccess"){
						$.messager.alert('消息提示', '任务发布成功!', 'info', function () {
							window.location.href=uri+"/task/findPriceToTask";
						});
					}else if(data.data=="refuse"){
						$.messager.alert('消息提示', '系统维护暂停任务发布!', 'info', function () {
							window.location.href=uri+"/task/findPriceToTask";
						});
					}else if(data.data=="low"){
						$.messager.alert('消息提示', '余额不足,任务发布失败!', 'info', function () {
							window.location.href=uri+"/task/findPriceToTask";
						});
					}
				}
			});
		});
	});
	
	function subtract(){
		for (var i = 0; i <= hour; i++) {
			subtractll = parseInt(subtractll) + parseInt($("#hour_" + i).val());
		}
	}
	
	function sum(){
		var temp1 = $("#flowcount")[0].value;
		if(temp1.length<1){
			temp1=0;
		}
		$("#lls_1").html(temp1);
		$("#lls_3").html(parseInt($('#lls_2').text())*temp1);
		var temp2 = $("#collectioncount")[0].value;
		if(temp2.length<1){
			temp2=0;
		}
		$("#scs_1").html(temp2);
		$("#scs_3").html(parseInt($('#scs_2').text())*temp2);
		var temp3 = $("#shoppingcount")[0].value;
		if(temp3.length<1){
			temp3=0;
		}
		$("#gwcs_1").html(temp3);
		$("#gwcs_3").html(parseInt($('#gwcs_2').text())*temp3);
		
		if(temp1<temp2 || temp1 < temp3){
			$.messager.alert('消息提示', '发布流量数不得小于收藏数和加购物车数!', 'info');
		}
		
		$("#sum").html(parseInt($("#lls_3").text())+parseInt($("#scs_3").text())+parseInt($("#gwcs_3").text()));
		$("#sum").html(parseInt($("#sum").text())*keywords*days);
		subtract();
		$("#sum").html(parseInt($("#sum").text())-(parseInt(subtractll)*parseInt($("#scs_2").text())));
		subtract();
		$("#sum").html(parseInt($("#sum").text())-parseInt(subtractll));
	}
	
	
	/*
	检查每小时输入的数字是否为数字
	*/
	function checkNum(obj) {
		var number = /^\d+$/;
		var temp = obj.value;
		if (!number.test(temp)) {
			alert("每时数量必须为0到150之间的数字");
			obj.value = "0";
		}else{
			var sum=0;
			for(var i=0;i<24;i++){
				sum=sum+parseInt($("#hour_" +  i).val());
			}
			$("#flowcount").val(sum);
			var number = /^\d+$/;
			var temp = $("#flowcount")[0].value;
			if (number.test(temp)) {
				sum();
			}
		}
	}
	/*
	检查输入的流量数
	*/
	function fpll(obj) {
		var number = /^\d+$/;
		var temp = obj.value;
		if(temp>llmax){
			$.messager.alert('消息提示', '该宝贝id发布流量数不能大于允许发布的最大流量数!', 'info', function () {
				$("#flowcount").val(llmax);
				return false;
			});
		}
		if (number.test(temp)) {
			sum();
			var ys = temp / (24 - hour-1);
			var fps = temp % (24 - hour-1);
			for (var i = hour+1; i < 24; i++) {
				$("#hour_" + i).val(parseInt(ys));
			}
			for (var i = 0; i < fps; i++) {
				var str = parseInt($("#hour_" + (hour+1 + i)).val()) + 1;
				$("#hour_" + (hour+1 + i)).val(str);
			}
		}
	}
	function fpsc(obj) {
		var number = /^\d+$/;
		var temp = obj.value;
		if(temp.length<1){
			$('#collectioncount').val("0");
			temp=0;
		}
		if (number.test(temp)) {
			sum();
		}
	}
	function fpgwc(obj) {
		var number = /^\d+$/;
		var temp = obj.value;
		if(temp.length<1){
			$('#shoppingcount').val("0");
			temp=0;
		}
		if (number.test(temp)) {
			sum();
		}
	}
	/*
	检查宝贝id当天可以发布多少流量数
	*/
	function checkkeynum(obj){
		if(obj.value.length<1){
			llmax=1000;
		}else{
			$.ajax({
				url : uri+"/phone/findAllPhoneInfoBykeynum/"+obj.value,
				type : "POST",
				success:function(data,state){
					llmax=data.count;
					if($("#flowcount").val()>llmax){
						$.messager.alert('消息提示', '该宝贝id发布流量数不能大于允许发布的最大流量数!', 'info', function () {
							$("#flowcount").val(llmax);
							fpll($("#flowcount")[0]);
						});
					}
					
				}
			});
		}
		$("#span").html("最多可发布流量数:"+llmax);
	}
	
	/*
	添加多关键词
	 */
	function addinput(){ 
		var trNumber=document.getElementById("tab_keyword").rows.length; 
		if(trNumber<4){
			var newTr=document.getElementById("tab_keyword").insertRow(trNumber); 
			newTr.insertCell(0).innerHTML="<input type='text' name='taskkeywords' class='form-control' placeholder='请输入关键词' />"; 
			newTr.insertCell(1).innerHTML="<input type='button' class='easyui-linkbutton' iconCls='icon-remove' onclick='delRow(this)' value='删除' />";
		}
		keywords = $("input[name='taskkeywords']").length;
		sum();
	}
	/*
	删除添加的多关键词
	 */
	function delRow(r){ 
		document.getElementById("tab_keyword").deleteRow(r.parentNode.parentNode.rowIndex);
		keywords = $("input[name='taskkeywords']").length;
		sum();
	}