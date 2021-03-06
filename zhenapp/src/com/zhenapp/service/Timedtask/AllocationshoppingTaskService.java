package com.zhenapp.service.Timedtask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhenapp.po.Custom.TPhoneInfoCustom;
import com.zhenapp.po.Custom.TTaskDetailInfoCustom;
import com.zhenapp.po.Custom.TTaskDetailinfoTempCustom;
import com.zhenapp.service.PhoneInfoService;
import com.zhenapp.service.SysconfInfoService;
import com.zhenapp.service.TaskDetailInfoService;
import com.zhenapp.service.TaskDetailInfoTempService;
import com.zhenapp.service.TaskInfoService;
@Transactional
@Service
public class AllocationshoppingTaskService {
	@Autowired
	private SysconfInfoService sysconfInfoService;
	@Autowired
	private PhoneInfoService phoneInfoService;
	@Autowired
	private TaskInfoService taskInfoService;
	@Autowired
	private TaskDetailInfoTempService taskDetailInfoTempService;
	@Autowired
	private TaskDetailInfoService taskDetailInfoService;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
	SimpleDateFormat HHmm = new SimpleDateFormat("HHmm");
	
	
	public String allocateiontaskshopping(TPhoneInfoCustom tPhoneInfoCustom,String tasknumstr) throws Exception{
		StringBuffer sb=new StringBuffer();
		HashMap<String, Object> hashmap = new HashMap<String, Object>();
		hashmap.put("phoneid", tPhoneInfoCustom.getPhoneid());
		hashmap.put("isshopping", 1);
		hashmap.put("today", yyyyMMdd.format(new Date()));
		hashmap.put("HHmm", HHmm.format(new Date().getTime() + 2*60*1000));
		hashmap.put("tasknumstr", tasknumstr);
		TTaskDetailInfoCustom tTaskDetailInfoCustomtype1shopping = taskDetailInfoService.requesttaskByphoneid_temp(hashmap);
		if(tTaskDetailInfoCustomtype1shopping!=null){
			tTaskDetailInfoCustomtype1shopping.setPhoneid(tPhoneInfoCustom.getPhoneid());
			TTaskDetailinfoTempCustom tTaskDetailinfoTempCustom = TTaskDetailinfoTempCustom.setTTaskDetailinfoTempCustom(tTaskDetailInfoCustomtype1shopping);
			taskDetailInfoTempService.insertDetailinfo(tTaskDetailinfoTempCustom);
			sb = TTaskDetailInfoCustom.Mosaicstr(tTaskDetailInfoCustomtype1shopping);
			hashmap.put("result", sb.toString());
			hashmap.put("taskdetailid", tTaskDetailinfoTempCustom.getTaskdetailid());
			hashmap.put("updatetime", sdf.format(new Date()));
			hashmap.put("updateuser", "api手机端修改字符串");
			taskDetailInfoService.updateTaskDetailresultByid(hashmap);
		}
		return sb.toString();
	}
}
