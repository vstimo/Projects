library IEEE;
use IEEE.STD_LOGIC_1164.all;
use IEEE.STD_LOGIC_ARITH.ALL;
use IEEE.STD_LOGIC_UNSIGNED.ALL;

entity divizor is
	port(clk:in std_logic;
		clk_nou: out std_logic);
end entity;				 

architecture divizor of divizor is
signal n: std_logic_vector(25 downto 0) :=(others =>'0');
begin	
	process(clk)
	begin
	if(clk='1' and clk'event) then n<=n+1;
	end if;
	clk_nou<=n(25);
	end process;
end architecture;	