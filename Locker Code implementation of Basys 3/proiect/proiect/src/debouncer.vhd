library IEEE;
use IEEE.STD_LOGIC_1164.all;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity debouncer is
	port(BTN:in std_logic;
		CLK:in std_logic;
		R:in std_logic;
		BTN_NOU:out std_logic);
end entity;				 

architecture arh of debouncer is
signal n1,n2,n3,n4,n5,n6,n7,n8,n9,n10,n11,n12,n13,n14,n15,n16,n17,n18,n19,n20,n21,n22,n23,n24,n25,n26: std_logic;
begin	
	process(CLK,R)
	begin
		if R='1' then  
			n1<='0'; 
			n2<='0';
			n3<='0';
			n4<='0';
			n5<='0';
			n6<='0';  
			n7<='0'; 
			n8<='0';
			n9<='0';
			n10<='0';	
			n11<='0';
			n12<='0';  
			n13<='0'; 
			n14<='0';
			n15<='0';
			n16<='0';
			n17<='0';
			n18<='0';  
			n19<='0'; 
			n20<='0';
			n21<='0';
			n22<='0';	
			n23<='0';
			n24<='0'; 
			n25<='0';
			n26<='0';
		elsif CLK='1' and CLK'event then
			n1<=BTN; 
			n2<=n1;
			n3<=n2;
			n4<=n3;
			n5<=n4;	   
			n6<=n5;  
			n7<=n6; 
			n8<=n7;
			n9<=n8;
			n10<=n9;  	  
			n11<=n10;
			n12<=n11; 
			n13<=n12; 
			n14<=n13;
			n15<=n14;
			n16<=n15;
			n17<=n16;
			n18<=n17;  
			n19<=n18; 
			n20<=n19;
			n21<=n20;
			n22<=n21;	
			n23<=n22;
			n24<=n23; 
			n25<=n24;
			n26<=n25;
		end if;
	end	 process;
	BTN_NOU<= n1 and n2 and n3 and n4 and n5 and n6 and n7 and n8 and n9 and n10 and  n11 and n12 and n13 and n14 and n15 and n16 and n17 and n18 and n19 and n20 and n21 and n22 and  n23 and n24 and n25 and n26;
end architecture;	