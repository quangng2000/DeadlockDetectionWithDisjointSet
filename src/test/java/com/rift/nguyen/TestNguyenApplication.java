package com.rift.nguyen;

import org.springframework.boot.SpringApplication;

public class TestNguyenApplication {

	public static void main(String[] args) {
		SpringApplication.from(NguyenApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
