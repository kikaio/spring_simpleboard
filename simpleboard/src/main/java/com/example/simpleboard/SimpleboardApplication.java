package com.example.simpleboard;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.example.simpleboard.entity.Privilege;
import com.example.simpleboard.entity.Role;
import com.example.simpleboard.enums.AdminPrivilegeEnums;
import com.example.simpleboard.enums.PrivilegeEnums;
import com.example.simpleboard.enums.RoleEnums;
import com.example.simpleboard.repository.PrivilegeRepository;
import com.example.simpleboard.repository.RoleRepository;

@SpringBootApplication
public class SimpleboardApplication {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PrivilegeRepository privilegeRepository;

	public static void main(String[] args) {
		SpringApplication.run(SimpleboardApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init()
	{

		HashMap<PrivilegeEnums, Privilege> privMap = new HashMap<>();
		//기본적인 권한 목록만 추가해준다.
		for(var enumVal : PrivilegeEnums.values())
		{
			var priv = privilegeRepository.findByName(enumVal.name()).orElse(null);
			if(priv == null)
			{
				priv = Privilege.builder()
					.name(enumVal.name())
					.descPrivilige(enumVal.descPriv)
					.build()
				;
				privilegeRepository.save(priv);
			}
			privMap.put(enumVal, priv);
		}

		HashMap<AdminPrivilegeEnums, Privilege> adminPrivMap = new HashMap<>();
		for(var enumVal : AdminPrivilegeEnums.values())
		{
			var priv = privilegeRepository.findByName(enumVal.name()).orElse(null);
			if(priv == null)
			{
				priv = Privilege.builder()
					.name(enumVal.name())
					.descPrivilige(enumVal.descPriv)
					.build()
				;
				privilegeRepository.save(priv);
			}
			adminPrivMap.put(enumVal, priv);
		}


		for(var enumVal : RoleEnums.values())
		{
			var role = roleRepository.findByName(enumVal.name()).orElse(null);
			switch(enumVal)
			{
				case ROLE_ADMIN:
				{
					if(role == null)
					{
						role = Role.builder()
							.name(enumVal.name())
							.descRole(enumVal.descRole)	
							.build()
						;
						roleRepository.save(role);
					}

					if(role.getPrivileges().isEmpty() && privMap.isEmpty()==false)
					{
						for(var priv : privMap.values())
						{
							role.addPrivilege(priv);
						}
						roleRepository.save(role);
					}
					break;
				}
				case ROLE_USER:
				{
					if(role == null)
					{
						role = Role.builder()
							.name(enumVal.name())
							.descRole(enumVal.descRole)	
							.build()
						;
						roleRepository.save(role);
					}

					if(role.getPrivileges().isEmpty() && adminPrivMap.isEmpty()==false)
					{
						for(var priv : adminPrivMap.values())
						{
							role.addPrivilege(priv);
						}
						roleRepository.save(role);
					}
					break;
				}
				default:
					break;
			}
		}
	}
}
