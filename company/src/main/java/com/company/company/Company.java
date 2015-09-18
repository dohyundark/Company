package com.company.company;



import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Company extends JavaPlugin {
	List<String> companyname = new ArrayList<String>();
	List<String> companynum = new ArrayList<String>();
	
	@Override
    public void onEnable(){
		
		try{
			this.reloadConfig();
			
			companyname = this.getConfig().getStringList("name");
	        companynum = this.getConfig().getStringList("number");
		}catch (Exception e){
			throw new CompanyDataException("데이터 파일을 읽을 수 없습니다!");
		}
		getLogger().info("회사 파일 로드됨");
		
    }
 
    @Override
    public void onDisable() {
    	try{
    		this.getConfig().set("name", companyname);
			this.getConfig().set("number", companynum);
			
			this.saveConfig();
    	}catch(Exception e){
    		throw new CompanyDataException("데이터 파일에 쓸 수 없습니다!");
    	}
        getLogger().info("회사 파일 저장됨");
    }
    
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	if(cmd.getName().equals("addcompany")){
    		String CEO;
    		try {
    			if (args[1].equals(null)){
    				sender.sendMessage("사업자등록번호는 필수입니다.");
    				return true;
    			}
    			if (args[2].equals(null)){
    				CEO = sender.getName();
    			}else{
    				CEO = args[2];
    			}
    			
    			companyname.add(args[0]);
    			companynum.add(args[1]);
    			
    			this.getConfig().set("name", companyname);
    			this.getConfig().set("number", companynum);
    			this.getConfig().set("company." + args[0] + ".ceo", CEO);
    			
    			String a = args[0] + " (" + args[1] + ")";
        		sender.sendMessage(ChatColor.BLUE + a + " 가 만들어졌습니다!");
        		getLogger().info(sender.getName() + " 이 회사 생성: " + a);
        		
        		Bukkit.broadcastMessage(ChatColor.BLUE + args[0] + "이라는 회사가 생겼어요!");
    		} catch (Exception e){
    			sender.sendMessage(e.getMessage());
    			sender.sendMessage("음, 뭔가 잘못된 것 같아요.");
    		}

    		
    		return true;
    	}
    	
    	if(cmd.getName().equals("delcompany")){
    		try{
    			companynum.remove(companyname.indexOf(args[0]));
    			companyname.remove(args[0]);
    			
    			this.getConfig().set("name", companyname);
    			this.getConfig().set("number", companynum);
    			this.getConfig().set("company." + args[0] + ".ceo", null);
    			this.getConfig().set("company." + args[0] + ".member", null);
    			
    			sender.sendMessage(ChatColor.BLUE + args[0] + " 가 제거되었습니다!");
    			getLogger().info(sender.getName() + " 이 회사 제거: " + args[0]);
    		}catch (Exception e){
    			sender.sendMessage("음, 뭔가 잘못된 것 같아요.");
    		}
    		
    		
    		return true;
    	}
    	
    	if(cmd.getName().equals("listcompany")){
    		sender.sendMessage(ChatColor.YELLOW + "회사 목록: ");
    		try{
    			for (String s : companyname){
    				sender.sendMessage(s + " (" + companynum.get(companyname.indexOf(s)) + ")");
    			}
    		}catch(CommandException e){
    			sender.sendMessage(e.getMessage());
    			return true;
    		}
    		getLogger().info(sender.getName() + " 이 회사 목록 요청.");
    		return true;
    	}
    	if(cmd.getName().equals("reloadcompany")){
    		this.reloadConfig();
    		companyname = this.getConfig().getStringList("name");
	        companynum = this.getConfig().getStringList("number");
    		sender.sendMessage(ChatColor.BLUE + "불러오기 완료");
    		
    		this.saveConfig();
    		sender.sendMessage(ChatColor.BLUE + "저장 완료");
    		
    		return true;
    	}
    	if(cmd.getName().equals("savecompany")){
    		this.saveConfig();
    		sender.sendMessage(ChatColor.BLUE + "저장 완료");
    		
    		return true;
    	}
    	if(cmd.getName().equals("company")){
    		switch(args[0]){
    			case("addmember"):
    				List<String> member = new ArrayList<String>();
    				member = this.getConfig().getStringList("company." + args[1] + ".member");
    				member.add(args[1]);
    				this.getConfig().set("company." + args[1] + ".member", member);
    				break;
    			case("delmember"):
    				List<String> remember = new ArrayList<String>();
    				remember = this.getConfig().getStringList("company." + args[1] + ".member");
    				remember.remove(args[1]);
    				this.getConfig().set("company." + args[1] + ".member", remember);
    				break;
    		}
    	}
    	return false; 
    
    }
    
}
