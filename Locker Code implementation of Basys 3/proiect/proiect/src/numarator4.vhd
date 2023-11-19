library IEEE;
use IEEE.STD_LOGIC_1164.ALL;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity numarator4 is 
   port( clk_enable: in std_logic;
 	 clk: in std_logic;
 	 reset: in std_logic;
 	 Q: out std_logic_vector(1 downto 0) :="00");
end numarator4;
 
architecture numarator4 of numarator4 is
signal clk_enable_nou: std_logic; 
signal clk_nou: std_logic;
component debouncer is
	port(BTN:in std_logic;
		CLK:in std_logic;
		R:in std_logic;
		BTN_NOU:out std_logic);
end component;

component divizor4 is
	port(clk:in std_logic;
		clk_nou: out std_logic);
end component;	

begin
U1: debouncer port map(clk_enable,clk,reset,clk_enable_nou);  
U2: divizor4 port map(clk,clk_nou); 

process(clk_nou,reset,clk_enable_nou)	 
variable temp: std_logic_vector(1 downto 0) :="00";
variable ok: integer :=0;
   begin
      if reset='1' then
         temp := "00";
      elsif(rising_edge(clk_nou)) then
         if clk_enable_nou='1' then					
			 if ok=0 then
            	if temp="11" then
              		temp:="00"; 
            	else
               		temp := temp + 1;
            	end if;
			ok:=1;
			else ok:=1;
			end if;
			else temp:=temp; ok:=0;
			end if;
         end if; 
	  Q <= temp;
   end process;
end numarator4;	 