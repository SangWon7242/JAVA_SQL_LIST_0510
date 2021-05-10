package com.sbs.example.textboard.controller;

import java.util.Scanner;

import com.sbs.example.textboard.Container;

public abstract class Controller {
	
	protected Scanner sc;
	// 상속 받은 놈들만 쓸 수 있도록
	
	public Controller() {
		this.sc = Container.sc;
	}
	
}
