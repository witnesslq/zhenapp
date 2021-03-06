package com.zhenapp.service.imp;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhenapp.mapper.Custom.TAgentInfoCustomMapper;
import com.zhenapp.po.Custom.TAgentInfoCustom;
import com.zhenapp.service.AgentInfoService;
@Service
public class AgentInfoServiceImp implements AgentInfoService{

	@Autowired
	private TAgentInfoCustomMapper tAgentInfoCustomMapper;
	
	@Override
	public List<TAgentInfoCustom> findAgentBypage(HashMap<String, Object> pagemap) throws Exception {
		// TODO Auto-generated method stub
		return tAgentInfoCustomMapper.findAgentBypage(pagemap);
	}

	@Override
	public List<TAgentInfoCustom> findAllAgentBypage(
			HashMap<String, Object> pagemap) throws Exception {
		// TODO Auto-generated method stub
		return tAgentInfoCustomMapper.findAllAgentBypage(pagemap);
	}

	@Override
	public TAgentInfoCustom findAgentByuserid(String userid) throws Exception {
		// TODO Auto-generated method stub
		return tAgentInfoCustomMapper.findAgentByuserid(userid);
	}

	@Override
	public TAgentInfoCustom findAgentByAgentid(String agentid) throws Exception {
		// TODO Auto-generated method stub
		return tAgentInfoCustomMapper.findAgentByAgentid(agentid);
	}

	@Override
	public int findTotalAgentBypage(HashMap<String, Object> pagemap)
			throws Exception {
		// TODO Auto-generated method stub
		return tAgentInfoCustomMapper.findTotalAgentBypage(pagemap);
	}

	@Override
	public TAgentInfoCustom findAgentBywww(String www) throws Exception {
		return tAgentInfoCustomMapper.findAgentBywww(www);
	}

	@Override
	public int saveAgentInfo(TAgentInfoCustom tAgentInfoCustom)
			throws Exception {
		return tAgentInfoCustomMapper.saveAgentInfo(tAgentInfoCustom);
	}

	@Override
	public int updateAgentByid(TAgentInfoCustom tAgentInfoCustom)
			throws Exception {
		return tAgentInfoCustomMapper.updateAgentByid(tAgentInfoCustom);
	}

	@Override
	public int updateagentstateByPk(HashMap<String, Object> hashmap) throws Exception {
		// TODO Auto-generated method stub
		return tAgentInfoCustomMapper.updateagentstateByPk(hashmap);
	}

}
