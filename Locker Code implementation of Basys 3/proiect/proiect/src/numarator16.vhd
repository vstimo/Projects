library IEEE;																																										library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity numarator16v2 is 
   port( clk_up_enable: in std_logic;
   		 clk_down_enable: in std_logic;
 		 clk: in std_logic;
 		 reset: in std_logic;
		 reset_ac: in std_logic;
 	 	 q: out std_logic_vector(3 downto 0) :="0000");
end entity;
 
architecture arh of numarator16v2 is 

signal clk_up_enable_nou: std_logic;  
signal clk_down_enable_nou: std_logic;	
signal reset_ac_nou: std_logic;
signal clk_nou: std_logic;
signal reset_ac_D:std_logic;

component debouncer 
	port(BTN:in std_logic;
		CLK:in std_logic;
		R:in std_logic;
		BTN_NOU:out std_logic);
end component;	

component divizor 
	port(clk:in std_logic;
		clk_nou: out std_logic);
end component;

component bistabil is
	port(d,clk,r: in std_logic;
	q:out std_logic);
end component ;

begin
nn1: debouncer port map(clk_up_enable,clk,reset,clk_up_enable_nou); 
nn2: debouncer port map(clk_down_enable,clk,reset,clk_down_enable_nou);
nnE: debouncer port map(reset_ac,clk,reset,reset_ac_nou);
nn4: bistabil port map(reset_ac_nou,clk,reset,reset_ac_D);
nn3: divizor port map(clk,clk_nou); 

process(clk,reset,clk_up_enable_nou,clk_down_enable_nou,reset_ac_D)	 
variable temp: std_logic_vector(3 downto 0) :="0000";
   begin
      if reset='1' then
         temp := "0000";
	  elsif reset_ac_D='1' then
		  temp :="0000";
      elsif(rising_edge(clk_nou)) then
         if clk_up_enable_nou='1' then
			 temp:= temp + '1';	
		 else temp:= temp;
			if clk_down_enable_nou='1' then
			temp:= temp - '1'; 
			else temp:= temp;  
			end if;	 
		end if;
      end if; 
	  q <= temp;
end process;
end architecture;	 